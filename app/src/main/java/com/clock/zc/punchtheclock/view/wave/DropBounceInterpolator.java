package com.clock.zc.punchtheclock.view.wave;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class DropBounceInterpolator implements Interpolator {
    public DropBounceInterpolator() {
    }

    public DropBounceInterpolator(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float v) {
        return v < 0.25F?-38.4F * (float)Math.pow((double)v - 0.125D, 2.0D) + 0.6F:((double)v >= 0.5D && (double)v < 0.75D?-19.2F * (float)Math.pow((double)v - 0.625D, 2.0D) + 0.3F:0.0F);
    }
}
