package com.clock.zc.punchtheclock.util;

import android.content.res.Resources;

/**
 * Created by Zc on 2017/9/20.
 */

public class Utils {
    /**
     * 密度
     */
    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }
}
