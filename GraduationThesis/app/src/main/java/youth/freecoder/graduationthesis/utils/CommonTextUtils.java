package youth.freecoder.graduationthesis.utils;

import android.text.TextUtils;

import org.jivesoftware.smack.XMPPConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by freecoder on 15/3/6.
 */
public class CommonTextUtils {


    public static String getUsernameByJID(String jid) {
        if (TextUtils.isEmpty(jid)) {
            return null;
        } else if (!jid.contains("@")) {
            return jid;
        }
        return jid.split("@")[0];
    }

    public static String getServerUserJIDByName(String username, XMPPConnection connection) {
        if (!TextUtils.isEmpty(username)) {
            return username.trim().concat("@" + connection.getServiceName() + "/Smack");
        }
        return null;
    }

    public static String getServerUser(String name) {
        if (!TextUtils.isEmpty(name)) {
            return name.concat("/Smack");
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        return format.format(new Date(time));
    }
}
