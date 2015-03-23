package youth.freecoder.graduationthesis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.db.DBUtils;
import youth.freecoder.graduationthesis.entity.MyMessage;
import youth.freecoder.graduationthesis.utils.CommonTextUtils;

public class MessagePageActivity extends Fragment {
    private ListView MessageListView;
    private View MessagePageView = null;
    private MyAdapter myAdapter;
    private List<MyMessage> messages = new ArrayList<MyMessage>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MessagePageView = inflater.inflate(R.layout.message_view, null);
        return MessagePageView;
    }


    @Override
    public void onStart() {
        super.onStart();
        InitView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 视图控件初始化
     */
    private void InitView() {
        MessageListView = (ListView) MessagePageView.findViewById(R.id.message_list_view);
        messages = getCurrentMessages();
        myAdapter = new MyAdapter(messages, getActivity());
        MessageListView.setAdapter(myAdapter);
        MessageListView.setOnItemClickListener(new MyOnItemClickListener());
        myAdapter.refresh(messages);
    }

    /**
     * 查询离线信息
     *
     * @return
     */
    private List<MyMessage> getCurrentMessages() {
        DBUtils dbUtils = new DBUtils(getActivity());
        return dbUtils.query();
    }


    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("click_user",
                    CommonTextUtils.getUsernameByJID(myAdapter.messages.get(position).getFrom()));
            startActivity(intent);
        }
    }


    /**
     * 自定义数据适配器
     */
    private class MyAdapter extends BaseAdapter {
        private List<MyMessage> messages;
        private Context context;

        /**
         * 初始化，只加载最后一次离线信息到列表
         *
         * @param MessageList
         * @param context
         */
        private MyAdapter(List<MyMessage> MessageList, Context context) {
            this.messages = MessageList;
            this.context = context;
        }

        private void refresh(List<MyMessage> MessageList) {
            this.messages = MessageList;
            this.notifyDataSetChanged();
            MessageListView.setSelection(getCount());
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ItemView itemView;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.message_item_view, null);
                itemView = new ItemView();
                itemView.contact_name = (TextView) convertView.findViewById(R.id.contacts_name);
                itemView.contact_content = (TextView) convertView.findViewById(R.id.contact_content);
                convertView.setTag(itemView);
            } else {
                itemView = (ItemView) convertView.getTag();
            }
            itemView.contact_name.setText(
                    CommonTextUtils.getUsernameByJID(messages.get(position).getFrom()));
            itemView.contact_content.setText(messages.get(position).getContent());
            return convertView;
        }
    }

    private class ItemView {
        private TextView contact_name;
        private TextView contact_content;
    }

}