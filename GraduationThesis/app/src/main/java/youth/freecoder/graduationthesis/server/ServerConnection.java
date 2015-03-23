package youth.freecoder.graduationthesis.server;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import youth.freecoder.graduationthesis.constant.ConstantValue;

/**
 * Created by freecoder on 15/3/7.
 */
public class ServerConnection {
    //XMPPConnection连接对象
    public static XMPPConnection xmppConnection = null;


    /**
     * 获取XMPPConnection对象
     *
     * @return
     */
    public static XMPPConnection getConnection() {
        if (xmppConnection == null) {
            //配置连接
            ConnectionConfiguration connectionConfiguration =
                    new ConnectionConfiguration(ConstantValue.IP_ADDRESS, ConstantValue.IP_PORT);
            connectionConfiguration.setSASLAuthenticationEnabled(true);
            //Class.forName("org.jivesoftware.smack.ReconnectionManager");
            //connectionConfiguration.setReconnectionAllowed(true);
            connectionConfiguration.setSendPresence(false);
            xmppConnection = new XMPPConnection(connectionConfiguration);
            try {
                xmppConnection.connect();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
        return xmppConnection;
    }

    /**
     * 关闭XMPPConnection对象
     */
    public static void closeXMPPConnection() {
        if (xmppConnection != null) {
            xmppConnection.disconnect();
            xmppConnection = null;
        }
    }

    /**
     * 自动重连服务器
     */
    public static void autoReconnectionServer() {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 服务器注册用户
     *
     * @return
     */
    public static int registerUser(String username, String password, String email) throws XMPPException {
        int register_result = -1;
        IQ result = null;
        boolean flag = false;
        //连接服务器
        ServerConnection.getConnection().connect();
        /**
         * 设置注册的用户以及属性字段
         */
        Registration registration = new Registration();
        registration.setType(IQ.Type.SET);
        //得到服务器名
        registration.setTo(getConnection().getServiceName());
        //设置用户名和密码
        registration.setUsername(username);
        registration.setPassword(password);
        registration.addAttribute("email", email);
        registration.addAttribute("android", "createUser_android");

        /**
         * 设置包过滤
         */
        AndFilter andFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector packetCollector = getConnection().createPacketCollector(andFilter);
        getConnection().sendPacket(registration);

        result = (IQ) packetCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());

        //停止获取结果
        packetCollector.cancel();

        if (result == null) {
            //服务器无响应
            register_result = 0;
        } else if (result.getType() == IQ.Type.ERROR) {
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                //账号已存在
                register_result = 1;
            } else {
                //注册失败
                register_result = 2;
            }
        } else if (result.getType() == IQ.Type.RESULT) {
            //注册成功
            register_result = 3;
        }
        return register_result;
    }

    /**
     * 获取所有分组
     *
     * @return
     */
    public static List<RosterGroup> getGroups() {
        Roster roster = null;
        if (xmppConnection == null) {
            xmppConnection = getConnection();
        }
        roster = xmppConnection.getRoster();
        List<RosterGroup> groupList = new ArrayList<RosterGroup>();
        Collection<RosterGroup> rosterGroups = roster.getGroups();
        Iterator<RosterGroup> rosterGroupIterator = rosterGroups.iterator();
        while (rosterGroupIterator.hasNext()) {
            groupList.add(rosterGroupIterator.next());
        }
        return groupList;
    }

    /**
     * 添加分组
     */
    public static boolean addGroup(Roster roster, String groupName) {
        try {
            roster.createGroup(groupName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 添加好友
     * 无分组
     *
     * @param userName 用户名
     * @param name     组名
     * @return
     */
    public static boolean addUser(Roster roster, String userName, String name) {
        try {
            roster.createEntry(userName, name, null);
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加好友
     * 有分组
     *
     * @param userName 用户名
     * @param name     组名
     * @return
     */
    public static boolean addUser(Roster roster, String userName, String name, String groupName) {
        try {
            roster.createEntry(userName, name, new String[]{groupName});
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
