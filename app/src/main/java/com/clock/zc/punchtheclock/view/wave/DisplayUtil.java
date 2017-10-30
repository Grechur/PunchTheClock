package com.clock.zc.punchtheclock.view.wave;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtil {
    private DisplayUtil() {
    }

    public static boolean isOver600dp(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return (float)displayMetrics.widthPixels / displayMetrics.density >= 600.0F;
    }
}
