package youth.freecoder.graduationthesis.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.entity.Mood;

/**
 * Created by freecoder on 15/3/12.
 */
public class HomePageActivity extends Fragment implements View.OnClickListener {
    private View homeView = null;
    private ListView listView;
    private ImageView location_sign;
    private MyAdapter myAdapter;

    /**
     * 主页
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.homepage_view, null);
        return homeView;
    }

    @Override
    public void onStart() {
        super.onStart();
        location_sign = (ImageView) homeView.findViewById(R.id.sign_location_btn);
        location_sign.setOnClickListener(this);
        myAdapter = new MyAdapter(getMoodList(), this.getActivity().getApplicationContext());
        listView = (ListView) homeView.findViewById(R.id.mood_list);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    /**
     * 获取数据
     */
    private List<Mood> getMoodList() {
        List<Mood> moodList = new ArrayList<Mood>();
        Mood mood = new Mood();
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("王宝强");
        mood.setFriend_stars("2级");
        mood.setTime_interval("2小时前");
        mood.setMood_content("这是我今天第一次发表说说，我很高兴，你知道不？");
        moodList.add(mood);
        mood = new Mood();
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);
        mood.setFriend_icon(R.drawable.home_friend_icon);
        mood.setFriend_username("黄丽玲");
        mood.setFriend_stars("3级");
        mood.setTime_interval("1小时前");
        mood.setMood_content("Happy New Year!");
        moodList.add(mood);

        return moodList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_location_btn:
                startActivity(new Intent(getActivity(), LocationActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * 自定义数据适配器
     */
    private class MyAdapter extends BaseAdapter {
        private List<Mood> moodList;
        private Context context;

        public MyAdapter(List<Mood> moodList, Context context) {
            this.moodList = moodList;
            this.context = context;
        }

        @Override
        public Object getItem(int position) {
            return moodList.get(position);
        }

        @Override
        public int getCount() {
            return moodList.size();
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
                convertView = layoutInflate.inflate(R.layout.homepage_item_view, null);
                itemView = new ItemView();
                itemView.friend_icon = (ImageView) convertView.findViewById(R.id.home_friend_icon);
                itemView.username = (TextView) convertView.findViewById(R.id.friend_username);
                itemView.stars = (TextView) convertView.findViewById(R.id.friend_stars);
                itemView.interval = (TextView) convertView.findViewById(R.id.published_time);
                itemView.content = (TextView) convertView.findViewById(R.id.mood_content);
                convertView.setTag(itemView);
            } else {
                itemView = (ItemView) convertView.getTag();
            }

            itemView.friend_icon.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(),
                            moodList.get(position).getFriend_icon())
            );
            itemView.username.setText(moodList.get(position).getFriend_username());
            itemView.stars.setText(moodList.get(position).getFriend_stars());
            itemView.interval.setText(moodList.get(position).getTime_interval());
            itemView.content.setText(moodList.get(position).getMood_content());

            return convertView;
        }
    }

    /**
     * 缓存对象
     */
    private class ItemView {
        private ImageView friend_icon;
        private TextView username;
        private TextView stars;
        private TextView interval;
        private TextView content;
    }
}
