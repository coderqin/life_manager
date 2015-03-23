package youth.freecoder.graduationthesis.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

/**
 * Created by freecoder on 15/2/28.
 */
public class BitmapUtils {

    /**
     * 大图片缩放加载
     * 避免内存溢出
     *
     * @param context
     * @param resource
     * @return
     */
    public static Bitmap getScaleBitmap(Activity context, int resource) {
        WindowManager wm = context.getWindowManager();
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //不去解析真实的位图，只是获取这个图的头文件信息
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        //缩放比例
        int scaleX = bitmapWidth / screenWidth;
        int scaleY = bitmapHeight / screenHeight;
        int scale = 1;

        if (scaleX > scaleY && scaleY > 1) {
            scale = scaleX;
        }
        if (scaleX < scaleY && scaleX > 1) {
            scale = scaleY;
        }
        //加载缩放图片到内存
        opts.inSampleSize = scale;
        //真正地去解析这个位图
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        return bitmap;
    }
}
