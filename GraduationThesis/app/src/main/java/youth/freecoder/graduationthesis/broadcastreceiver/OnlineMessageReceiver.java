package youth.freecoder.graduationthesis.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import youth.freecoder.graduationthesis.db.DBUtils;
import youth.freecoder.graduationthesis.entity.MyMessage;

/**
 * Created by freecoder on 15/3/15.
 */
public class OnlineMessageReceiver extends BroadcastReceiver {
    public OnlineMessageReceiver() {
        super();
    }

    /**
     * 同步到本地数据库
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        MyMessage myMessage =
                (MyMessage) intent.getParcelableExtra("online_message");
        DBUtils dbUtils = new DBUtils(context);
        dbUtils.insert(myMessage);
    }
}
