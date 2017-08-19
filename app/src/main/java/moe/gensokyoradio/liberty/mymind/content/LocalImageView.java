package moe.gensokyoradio.liberty.mymind.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/*
 *     This file is part of MyMind.
 * 
 *     MyMind is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyMind is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyMind. If not, see <http://www.gnu.org/licenses/>.
 */

public class LocalImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "LocalImageView";
    private String path;

    public LocalImageView(Context context) {
        super(context);
    }

    public LocalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        Log.i(TAG, "Bitmap requested max size: " + "width: " + width + ", height: " + height);
        Log.i(TAG, "Loading image: " + path);
        Bitmap bitmap = decodeSampledBitmapFromFile(path, width, height);
        if (bitmap != null) {
            Log.i(TAG, "Bitmap size: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            this.setAdjustViewBounds(true);
            this.setMaxWidth(width);
            this.setImageBitmap(bitmap);
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.i(TAG, "Calculated sample size of image: " + "inSampleSize: " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.i(TAG, "Raw size of image: " + "width: " + width + ", height: " + height);
        int inSampleSize = 1;

        while ((width / inSampleSize) >= reqWidth || (height / inSampleSize) >= reqHeight) {
            inSampleSize *= 2;
        }

        Log.i(TAG, "Calculated size of image: " + "width: " + width / inSampleSize + ", height: " + height / inSampleSize);
        return inSampleSize;
    }
}
