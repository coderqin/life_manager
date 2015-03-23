package youth.freecoder.graduationthesis.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.constant.ConstantValue;
import youth.freecoder.graduationthesis.db.DBUtils;
import youth.freecoder.graduationthesis.entity.MyMessage;
import youth.freecoder.graduationthesis.server.ServerConnection;
import youth.freecoder.graduationthesis.utils.CommonTextUtils;

public class ChatActivity extends Activity {
    private TextView chatFriendName;
    private ListView chatContent;
    private EditText inputContent;
    private Button sendBtn;
    private ImageView chat_turn_back;
    private String toChat;
    private ChatManager chatManager;
    private List<MyMessage> messageList = new ArrayList<MyMessage>();
    private MyAdapter myAdapter;

    private String TAG = "Test";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAdapter.refresh(messageList);
    }

    private void init() {


        chatFriendName = (TextView) findViewById(R.id.chat_friend_name);
        chatContent = (ListView) findViewById(R.id.chat_content_view);
        inputContent = (EditText) findViewById(R.id.chat_input_content);
        sendBtn = (Button) findViewById(R.id.chat_sendbtn);
        chat_turn_back = (ImageView) findViewById(R.id.chat_turn_back);
        chat_turn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent.getStringExtra("tochatUser") != null) {
            toChat = intent.getStringExtra("tochatUser");
        }
        if (intent.getStringExtra("click_user") != null) {
            toChat = intent.getStringExtra("click_user");
        }
        chatFriendName.setText(toChat);
        messageList = getCurrentMessageList(toChat, ConstantValue.NOW_USERNAME);
        myAdapter = new MyAdapter(messageList, this);
        chatContent.setAdapter(myAdapter);


        /**
         * 发送消息
         */
        chatManager = ServerConnection.getConnection().getChatManager();
        final Chat chat = chatManager.createChat(
                CommonTextUtils.getServerUserJIDByName(toChat, ServerConnection.getConnection()), new MessageListener() {
                    @Override
                    //接收消息
                    public void processMessage(Chat chat, Message message) {
                        if (message.getBody() != null) {
                            messageList.add(new MyMessage(
                                    message.getFrom(),
                                    message.getTo(),
                                    1,
                                    message.getBody(),
                                    String.valueOf(message.getProperty("SendTime")),
                                    1
                            ));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter.refresh(messageList);
                                }
                            });

                        }
                    }
                });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendContent = inputContent.getText().toString();
                if (!TextUtils.isEmpty(sendContent)) {
                    //发送消息
                    try {
                        Message message = new Message();
                        message.setBody(sendContent);
                        message.setTo(CommonTextUtils.getServerUserJIDByName(toChat, ServerConnection.getConnection()));
                        message.setProperty("SendTime", CommonTextUtils.getNowDate());
                        chat.sendMessage(message);
                        inputContent.setText("");
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                    messageList.add(new MyMessage(
                            CommonTextUtils.getServerUserJIDByName(ConstantValue.NOW_USERNAME,
                                    ServerConnection.getConnection()),
                            CommonTextUtils.getServerUserJIDByName(toChat,
                                    ServerConnection.getConnection()),
                            0,
                            sendContent,
                            CommonTextUtils.getNowDate(),
                            1
                    ));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.refresh(messageList);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBUtils dbUtils = new DBUtils(ChatActivity.this);
        dbUtils.insert(messageList);
        messageList.clear();
    }

    private class MyAdapter extends BaseAdapter {
        private List<MyMessage> myMessageList;
        private Context context;
        private LayoutInflater layoutInflater;

        public MyAdapter(List<MyMessage> myMessageList, Context context) {
            this.context = context;
            this.myMessageList = myMessageList;
            chatContent.setSelection(getCount());
        }

        public void refresh(List<MyMessage> myMessageList) {
            this.myMessageList = myMessageList;
            this.notifyDataSetChanged();
            chatContent.setSelection(getCount());
        }

        @Override
        public int getCount() {
            return myMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return myMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyMessage myMessage = myMessageList.get(position);
            if (myMessage.getType() == 0) {
                convertView = this.layoutInflater.inflate(R.layout.sended_message_view, null);
            } else {
                convertView = this.layoutInflater.inflate(R.layout.received_message_view, null);
            }

            ItemView itemView = new ItemView(
                    (TextView) convertView.findViewById(R.id.first_receive_time),
                    (TextView) convertView.findViewById(R.id.send_content)
            );
            itemView.getTimeView().setText(myMessage.getDate());
            itemView.getContentView().setText(myMessage.getContent());

            return convertView;
        }
    }

    private class ItemView {
        private TextView timeView;
        private TextView contentView;

        private ItemView(TextView timeView, TextView contentView) {
            this.timeView = timeView;
            this.contentView = contentView;
        }

        public TextView getTimeView() {
            return timeView;
        }

        public void setTimeView(TextView timeView) {
            this.timeView = timeView;
        }

        public TextView getContentView() {
            return contentView;
        }

        public void setContentView(TextView contentView) {
            this.contentView = contentView;
        }
    }


    private List<MyMessage> getCurrentMessageList(String from, String to) {
        from = CommonTextUtils.getServerUserJIDByName(from, ServerConnection.getConnection());
        to = CommonTextUtils.getServerUserJIDByName(to, ServerConnection.getConnection());
        DBUtils dbUtils = new DBUtils(this);
        return dbUtils.queryOneCouple(from, to);
    }


}





