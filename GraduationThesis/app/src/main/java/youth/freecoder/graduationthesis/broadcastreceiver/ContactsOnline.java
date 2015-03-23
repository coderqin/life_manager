package youth.freecoder.graduationthesis.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by freecoder on 15/3/16.
 */
public class ContactsOnline extends BroadcastReceiver {
    public ContactsOnline() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String whoOnline = intent.getStringExtra("online");
        Log.i("ContactsOnline", whoOnline + "上线了.......");
    }
}
