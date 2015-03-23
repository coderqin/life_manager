package youth.freecoder.graduationthesis.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.constant.ConstantValue;
import youth.freecoder.graduationthesis.server.ServerConnection;

public class MainMenuActivity extends FragmentActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost fragmentTabHost;
    //定义一个布局
    private LayoutInflater layoutInflater;
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {
            HomePageActivity.class, FriendsPageActivity.class, MessagePageActivity.class};
    //定义数组来存放按钮图片
    private int tabImages[] = {
            R.drawable.home_btn,
            R.drawable.friends_btn,
            R.drawable.message_btn
    };
    //Tab选项卡的文字
    private String tabTexts[] = {"主页", "好友", "消息",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_view);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        Intent dataIntent = getIntent();
        ConstantValue.NOW_USERNAME = dataIntent.getStringExtra("username");

        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(tabTexts[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        imageView.setImageResource(tabImages[index]);

        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(tabTexts[index]);
        textView.setTextColor(Color.BLACK);
        return view;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /**
         * 下线通知
         */
        Presence presence = new Presence(Presence.Type.unavailable);
        ServerConnection.getConnection().sendPacket(presence);
        ServerConnection.closeXMPPConnection();

    }
}
