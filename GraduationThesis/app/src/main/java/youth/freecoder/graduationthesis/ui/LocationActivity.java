package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.utils.ToastUtils;

public class LocationActivity extends Activity implements LocationSource,
        AMapLocationListener, View.OnClickListener, PoiSearch.OnPoiSearchListener,
        AMap.CancelableCallback, AdapterView.OnItemClickListener {
    private static String TAG = "AMLocationTest";
    private List<PoiItem> poiItems;// poi数据
    private PoiResult poiResult; //poi返回的结果
    private LatLonPoint lp;  //几何点对象类
    private PoiSearch poiSearch;  //本类为POI（Point Of Interest，兴趣点）搜索的“入口”类
    private PoiSearch.Query query;  //POI查询条件类
    private int currentPage = 0; // 当前页数
    private PoiOverlay poiOverlay;  //poi图层
    private LatLng latLng;   //经纬度位置
    private AMapLocation nowAMapLocation; //位置
    private MyAdapter myAdapter;


    /**
     * 定义AMap 地图对象的操作方法与接口
     */
    private AMap aMap;
    /**
     * 一个显示地图的视图（View）。它负责从服务端获取地图数据。
     * 当屏幕焦点在这个视图上时，它将会捕捉键盘事件（如果手机配有实体键盘）及屏幕触控手势事件。
     * 使用这个类必须按照它的生命周期进行操控，
     * 你必须参照以下方法
     * onCreate(Bundle)、 onResume()、onPause()、onDestroy()、onSaveInstanceState(Bundle)、
     * onLowMemory() 当MapView初始化完成后，用户可以通过getMap()方法获得一个AMap 对象。
     * 如果MapView 没有初始成功，则执行getMap()将返回null。
     * 显示地图更方便的方法是使用MapFragment或者SupportMapFragment（如果要低于API 12（Android 3.1）的平台上运行）。
     */
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy managerProxy;


    private TextView cancel;
    private EditText search_content;
    private ImageButton search_btn;
    private ListView locationList;
    private LinearLayout relocation;
    private RelativeLayout aMap_region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_view);
        mapView = (MapView) findViewById(R.id.map);
        cancel = (TextView) findViewById(R.id.search_cancel);
        search_content = (EditText) findViewById(R.id.search_location);
        search_btn = (ImageButton) findViewById(R.id.search_location_btn);
        locationList = (ListView) findViewById(R.id.location_list);
        relocation = (LinearLayout) findViewById(R.id.relocation_btn);
        aMap_region = (RelativeLayout) findViewById(R.id.aMap_region);
        //此方法必须重写
        mapView.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        cancel.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        relocation.setOnClickListener(this);
    }

    /**
     * 设置一些aMap的属性
     */
    private void setUpMap() {
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 设置指南针是否显示
        aMap.getUiSettings().setCompassEnabled(true);
        // 设置地图LOGO的位置 右下
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        //设置缩放按钮不显示
        aMap.getUiSettings().setZoomControlsEnabled(false);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            this.nowAMapLocation = aMapLocation;
            if (aMapLocation != null
                    && aMapLocation.getAMapException().getErrorCode() == 0) {
                //mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                //定义了一个可视区域的移动.两个参数，一个位置，一个缩放级别（4-20）
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                /**在指定的持续时间内，动画地移动地图到指定的位置，完成时调用可选的回调方法。
                 *如果运动过程中调用getCameraPosition()，它将返回可视区域飞行中的当前位置。
                 */
                aMap.animateCamera(cameraUpdate, 1000, this);
            } else {
                Log.e("AMapErr", "Location ERR:" + aMapLocation.getAMapException().getErrorCode());
            }
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (managerProxy == null) {
            managerProxy = LocationManagerProxy.getInstance(this);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用destroy()方法
            // 其中如果间隔时间为-1，则定位只定一次
            // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
            managerProxy.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (managerProxy != null) {
            managerProxy.removeUpdates(this);
            managerProxy.destroy();
        }
        managerProxy = null;
    }


    /**
     * 进行POI搜索
     */
    private void doSearchQuery(AMapLocation mapLocation, String keywords) {
        if (mapLocation != null) {
            aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
            if (lp == null) {
                lp = new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude());
            }
            /**
             * 第一个表示搜索字符串，第二个表示poi搜索类型，第三个表示城市
             */
            query = new PoiSearch.Query(keywords, "", mapLocation.getCity());
            query.setPageSize(20);
            // 设置查第一页
            query.setPageNum(currentPage);
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            //设置搜索区域为lp点为圆心,其周围2000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));
            //异步搜索
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * POI搜索回调方法
     *
     * @param result
     * @param
     */
    @Override
    public void onPoiSearched(PoiResult result, int resultCode) {
        if (resultCode == 0) {
            //搜索poi结果
            if (result != null && result.getQuery() != null) {
                //是否是同一条
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    //取得第一页的PoiItem数据，页数从0开始
                    poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiItem数据时，会返回含有搜索关键字的城市信息
                    //有结果
                    if (poiItems != null && poiItems.size() > 0) {
                        myAdapter = new MyAdapter(poiItems, LocationActivity.this);
                        locationList.setAdapter(myAdapter);
                        locationList.setOnItemClickListener(this);
                        // 设置定位监听
                        aMap.setLocationSource(this);
                        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
                        aMap.setMyLocationEnabled(true);
//                        //清理之前的图标
//                        aMap.clear();
//                        //通过此构造函数创建Poi图层
//                        poiOverlay = new PoiOverlay(aMap, poiItems);
//
//                        //去掉PoiOverlay上所有的Marker
////                        poiOverlay.removeFromMap();
//                        //添加Marker到地图中
//                        poiOverlay.addToMap();
//                        //移动镜头到当前的视角
//                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtils.show(LocationActivity.this, "无结果");
                    }
                }
            } else {
                ToastUtils.show(LocationActivity.this, "无结果");
            }
        } else if (resultCode == 27) {
            ToastUtils.show(LocationActivity.this, "网络错误");
        } else if (resultCode == 32) {
            ToastUtils.show(LocationActivity.this, "错误的地图码key");
        } else {
            ToastUtils.show(LocationActivity.this, "其他错误" + resultCode);
        }

    }

    /**
     * POI详情回调
     */
    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int resultCode) {
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String Information = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            Information += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtils.show(LocationActivity.this, Information);

    }

    /**
     * 这2个类是在动画停止时从主线程回调
     */
    @Override
    public void onFinish() {
        aMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker));
        markerOptions.position(latLng);
        aMap.addMarker(markerOptions);
        if (nowAMapLocation != null) {
            //无关键字
            doSearchQuery(nowAMapLocation, "");
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                this.onBackPressed();
                break;
            case R.id.search_location_btn:
                String keyword = search_content.getText().toString().trim();
                if (keyword == null || "".equals(keyword)) {
                    ToastUtils.show(LocationActivity.this, "输入内容不能为空！");
                } else {
                    if (nowAMapLocation != null) {
                        //将地图区域隐匿,位置不留
                        aMap_region.setVisibility(View.GONE);
                        doSearchQuery(nowAMapLocation, keyword);
                    } else {
                        ToastUtils.show(LocationActivity.this, "异常！请退出后重新定位");
                    }
                }
                break;
            case R.id.relocation_btn:
                if (nowAMapLocation != null) {
                    onLocationChanged(nowAMapLocation);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 自定义数据适配器
     */
    private class MyAdapter extends BaseAdapter {
        private List<PoiItem> poiItems;
        private Context context;

        public MyAdapter(List<PoiItem> poiItems, Context context) {
            this.poiItems = poiItems;
            this.context = context;
        }

        @Override
        public Object getItem(int position) {
            return poiItems.get(position);
        }

        @Override
        public int getCount() {
            return poiItems.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflate = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            ItemView itemView;
            if (convertView == null) {
                convertView = layoutInflate.inflate(R.layout.signin_item_view, null);
                itemView = new ItemView();
                itemView.poiName = (TextView) convertView.findViewById(R.id.poi_name);
                itemView.poiAddress = (TextView) convertView.findViewById(R.id.poi_address);
                convertView.setTag(itemView);
            } else {
                itemView = (ItemView) convertView.getTag();
            }
            itemView.poiName.setText(poiItems.get(position).getSnippet());
            itemView.poiAddress.setText(poiItems.get(position).getProvinceName() + "," +
                    poiItems.get(position).getCityName() + "," +
                    poiItems.get(position).getAdName());
            return convertView;
        }
    }

    /**
     * 缓存对象
     */
    private class ItemView {
        private TextView poiName;
        private TextView poiAddress;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String address = poiItems.get(position).getSnippet();
        Log.i(TAG, "click address:" + address);
        if (!TextUtils.isEmpty(address)) {
            Intent intent = new Intent(LocationActivity.this, MoodActivity.class);
//            intent.setAction("freecoder.mood.action");
            intent.putExtra("selected_address", address);
//            setResult(ConstantValue.RESULT_CODE);
            startActivity(intent);
            LocationActivity.this.finish();
        }
    }
}
