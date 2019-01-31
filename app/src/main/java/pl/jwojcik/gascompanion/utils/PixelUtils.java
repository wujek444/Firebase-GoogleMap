package pl.jwojcik.gascompanion.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class PixelUtils {

    public static int dpToPx(Context context, int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        int dp = Math.round(px / getPixelScaleFactor(context));
        return  dp;
    }

    public static float getPixelScaleFactor(Context context) {
        return (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
