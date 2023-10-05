package in.co.vibrant.bindalsugar.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by jaimatadi on 22-Apr-16.
 */
public class ImageUtil {

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1,Bitmap bitmap) throws Exception
    {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
        //Bitmap bitmap = BitmapFactory.decodeFile(path);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
            /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(20);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) - 100;
        int y = (bitmap.getHeight() + bounds.height()) - 70;

        canvas.drawText(gText, x, y, paint);
        paint.getTextBounds(gText1, 0, gText1.length(), bounds);
        int x1 = (bitmap.getWidth() - bounds.width()) - 100;
        int y1 = (bitmap.getHeight() + bounds.height()) - 40;
        canvas.drawText(gText1, x1, y1, paint);
        return bitmap;
    }


    public Bitmap drawTextToFile(Context gContext, String gText, String gText1,String path) throws Exception
    {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
            /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(120);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) - 200;
        int y = (bitmap.getHeight() + bounds.height()) - 260;

        canvas.drawText(gText, x, y, paint);
        paint.getTextBounds(gText1, 0, gText1.length(), bounds);
        int x1 = (bitmap.getWidth() - bounds.width()) - 200;
        int y1 = (bitmap.getHeight() + bounds.height()) - 110;
        canvas.drawText(gText1, x1, y1, paint);
        return bitmap;
    }


    public Bitmap ShrinkBitmapByPath(String file, int width, int height) throws Exception
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public Bitmap ShrinkBitmapByBmp(Bitmap bitmap, int width, int height) throws Exception
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        //Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        //bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
}
