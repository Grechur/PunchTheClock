package com.clock.zc.punchtheclock.view.smart.util;

import android.view.animation.Interpolator;

public class ViscousFluidInterpolator implements Interpolator {
    private static final float VISCOUS_FLUID_SCALE = 8.0F;
    private static final float VISCOUS_FLUID_NORMALIZE = 1.0F / viscousFluid(1.0F);
    private static final float VISCOUS_FLUID_OFFSET;

    public ViscousFluidInterpolator() {
    }

    private static float viscousFluid(float x) {
        x *= 8.0F;
        if(x < 1.0F) {
            x -= 1.0F - (float)Math.exp((double)(-x));
        } else {
            float start = 0.36787945F;
            x = 1.0F - (float)Math.exp((double)(1.0F - x));
            x = start + x * (1.0F - start);
        }

        return x;
    }

    public float getInterpolation(float input) {
        float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
        return interpolated > 0.0F?interpolated + VISCOUS_FLUID_OFFSET:interpolated;
    }

    static {
        VISCOUS_FLUID_OFFSET = 1.0F - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0F);
    }
}
