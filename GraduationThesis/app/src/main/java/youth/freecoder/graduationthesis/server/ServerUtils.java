package youth.freecoder.graduationthesis.server;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import youth.freecoder.graduationthesis.db.DBUtils;
import youth.freecoder.graduationthesis.entity.MyMessage;
import youth.freecoder.graduationthesis.entity.User;
import youth.freecoder.graduationthesis.listener.MessageReceiver;
import youth.freecoder.graduationthesis.services.PresenceService;
import youth.freecoder.graduationthesis.utils.CommonTextUtils;

/**
 * Created by freecoder on 15/3/7.
 */
public class ServerUtils {
    private static String TAG = "ServerUtils";

    /**
     * 服务器登陆验证
     * 设置在线状态
     * 添加消息监听器
     *
     * @param username 用户名
     * @param password 密  码
     * @return
     */
    public static void loginMatch(final String username, final String password, final Context context)
            throws XMPPException {
        ServerConnection.getConnection().login(username, password);
        Log.i("App", "users connected successfully.......");
        //异步获取离线信息
//        GetOfflineTask getOfflineTask = new GetOfflineTask(username, context);
//        getOfflineTask.execute();
        getOffLine(username, ServerConnection.getConnection(), context);
        /**
         *启动服务添加Presence包监听
         */
        context.startService(new Intent(context, PresenceService.class));
        Presence presence = new Presence(Presence.Type.available);
        ServerConnection.getConnection().sendPacket(presence);
        setConnectionMessageListener(ServerConnection.getConnection(), context);
    }

    /**
     * 添加在线消息接收的监听器
     */
    private static void setConnectionMessageListener(XMPPConnection connection, Context context) {
        PacketFilter filter =
                new MessageTypeFilter(Message.Type.chat);
        connection.addPacketListener(new MessageReceiver(context), filter);

    }

    /**
     * 得到组下所有好友列表
     */
    public static List<User> getUserList(Collection<RosterEntry> rosterEntries, XMPPConnection connection) {
        List<User> userList = new ArrayList<User>();
        User user;
        for (RosterEntry entry : rosterEntries) {
            user = new User(entry.getName(), 0, UserState(connection.getRoster(),
                    CommonTextUtils.getServerUserJIDByName(entry.getName(), connection)));
            userList.add(user);
        }
        return userList;
    }

    /**
     * 得到未分组好友列表
     *
     * @return
     */
    public static List<User> getUnGroupUserList(Roster roster) {
        List<User> userList = new ArrayList<User>();
        User user;
        for (RosterEntry entry : roster.getUnfiledEntries()) {
            user = new User(entry.getName(), 0, UserState(roster, entry.getName()));
            userList.add(user);
        }
        return userList;
    }

    /**
     * 获取用户状态
     * 0 离线 1 在线
     *
     * @param roster
     * @param user
     * @return
     */
    private static int UserState(Roster roster, String user) {
        Presence presence = roster.getPresence(user);
        if (presence.isAvailable()) {
            return 1;
        }
        return 0;
    }

    /**
     * 异步获取离线信息的私有类
     */
    private static class GetOfflineTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;
        private String username;

        public GetOfflineTask(String username, Context context) {
            this.username = username;
            this.context = context;
        }

        /**
         * 后台运行的方法，可以运行非UI线程,处理比较耗时的方法
         *
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(Void... params) {
            /**
             * 获取离线消息
             */
            try {
                getOffLine(username, ServerConnection.getConnection(), context);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "离线消息获取开始执行.............");
        }

        /**
         * 运行在UI线程,在doInBackground()之后执行
         * doInBackground()执行完毕
         *
         * @param integer
         */
        @Override
        protected void onPostExecute(Integer integer) {
            Log.i(TAG, "离线消息获取结束.............");
        }

        /**
         * 更新进度
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    /**
     * 获取离线消息
     */
    private static void getOffLine(final String username, final XMPPConnection connection, final Context context) throws XMPPException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Message> msgList = new ArrayList<Message>();
                    final List<MyMessage> offlineMessageList = new ArrayList<MyMessage>();
                    final OfflineMessageManager offlineMessageManager = new OfflineMessageManager(
                            ServerConnection.getConnection());
                    Message message;
                    Iterator<Message> iterator = offlineMessageManager.getMessages();
                    while (iterator.hasNext()) {
                        message = iterator.next();
                        /**
                         * 筛选出属于登陆用户的离线消息
                         */
                        if (message.getTo().equals(CommonTextUtils.getServerUserJIDByName(username, connection))) {
                            /**
                             * 封装离线信息
                             */
                            MyMessage myMessage = new MyMessage(
                                    message.getFrom(),
                                    message.getTo(),
                                    1,
                                    message.getBody(),
                                    String.valueOf(message.getProperty("SendTime")),
                                    2
                            );
                            offlineMessageList.add(myMessage);
                        }
                    }

                    /**
                     * 添加数据到数据库
                     */
                    if (offlineMessageList.size() > 0) {
                        DBUtils dbUtils = new DBUtils(context);
                        dbUtils.insert(offlineMessageList);
                    }
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
