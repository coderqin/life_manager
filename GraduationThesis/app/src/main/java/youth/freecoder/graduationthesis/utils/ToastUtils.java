package youth.freecoder.graduationthesis.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by freecoder on 15/3/14.
 */
public class ToastUtils {

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
