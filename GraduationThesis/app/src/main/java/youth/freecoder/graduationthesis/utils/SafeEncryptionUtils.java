package youth.freecoder.graduationthesis.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by freecoder on 15/3/7.
 */
public class SafeEncryptionUtils {
    private static MessageDigest messageDigest = null;

    /**
     * 常用MD5加密算法
     *
     * @param str
     * @return
     */
    private static String getMD5Str(String str) {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //加密
        byte[] md5 = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < md5.length; i++) {
            if (Integer.toHexString((0xFF & md5[i])).length() == 1) {
                stringBuffer.append("0").append(Integer.toHexString(0xFF & md5[i]));
            } else {
                stringBuffer.append(Integer.toHexString(0xFF & md5[i]));
            }
        }
        //16位加密，从第9位到25位
        return stringBuffer.substring(8, 24).toString().toLowerCase();
    }

    /**
     * 静态公共方法: 将传入字符串加密
     *
     * @param args
     * @return
     */
    public static String getCommonMD5code(String args) {
        return getMD5Str(args);
    }

    /**
     * 获取手机设备号MD5加密串，唯一标识
     *
     * @param context
     * @return
     */
    public static String getMD5DevicesID(Context context) {

        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceId = telephonyManager.getDeviceId();
        return getMD5Str(DeviceId);
    }
}
