package youth.freecoder.graduationthesis.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import youth.freecoder.graduationthesis.server.ServerConnection;
import youth.freecoder.graduationthesis.utils.CommonTextUtils;

public class PresenceService extends Service {
    private XMPPConnection connection;
    private static String TAG = "PresenceService";

    /**
     * 服务创建后第一个调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        connection = ServerConnection.getConnection();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务创建后第二个调用
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getOnlineUsers();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getOnlineUsers() {
        //是否已经认证，必须登陆后才能接收Presence包
        if (connection != null && connection.isConnected() &&
                connection.isAuthenticated()) {
            //得到用户名，去除@以及服务器的后缀部分
            final String user = CommonTextUtils.getUsernameByJID(connection.getUser());
            //条件过滤器，过滤Presence包

            PacketFilter filter = new AndFilter(new PacketTypeFilter(Presence.class));
            //监听器
            PacketListener listener = new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    //Presence时packet的子类
                    if (packet instanceof Presence) {
                        Presence presence = (Presence) packet;
                        //发送包的用户
                        String from = presence.getFrom();
                        //接收包的用户
                        String to = presence.getTo();
                        if (!from.equals(to)) {
                            /**
                             * Presence.Type有7种状态
                             */
                            if (presence.getType().equals(Presence.Type.subscribe)) {
                                // Accept all subscription requests.
//                                Presence response = new Presence(Presence.Type.subscribed);
//                                response.setTo(CommonTextUtils.getServerUser(from));
//                                connection.sendPacket(response);

                            } else if (presence.getType().equals(Presence.Type.subscribed)) {
                                //好友申请回执，同意添加好友
//                                ServerConnection.addUser(connection.getRoster(),
//                                        CommonTextUtils.getServerUser(from),
//                                        CommonTextUtils.getUsernameByJID(from));

                            } else if (presence.getType().equals(Presence.Type.unsubscribe)) {
                                //好友申请回执，拒绝添加好友
                            } else if (presence.getType().equals(Presence.Type.unsubscribed)) {
                                //删除好友
                            } else if (presence.getType().equals(Presence.Type.unavailable)) {
                                //好友下线
                                //广播好友上线通知
                                Intent intent = new Intent();
                                intent.setAction("youth.free.coder.contacts.offline");
                                intent.putExtra("offline", from);
                                sendBroadcast(intent);

                            } else {
                                //广播好友上线通知
                                Intent intent = new Intent();
                                intent.setAction("youth.free.coder.contacts.online");
                                intent.putExtra("online", from);
                                sendBroadcast(intent);
                            }
                        }
                    }
                }
            };
            //注册监听
            connection.addPacketListener(listener, filter);
        }
    }

}
