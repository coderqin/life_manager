package youth.freecoder.graduationthesis.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by freecoder on 15/3/16.
 */
public class ContactsOffline extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String whoOnline = intent.getStringExtra("offline");
        Log.i("ContactsOffline", whoOnline + "下线了.......");
    }

    public ContactsOffline() {
        super();
    }
}
