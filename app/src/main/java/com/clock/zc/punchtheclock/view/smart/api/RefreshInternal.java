package com.clock.zc.punchtheclock.view.smart.api;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.listener.OnStateChangedListener;


public interface RefreshInternal extends OnStateChangedListener {
    @NonNull
    View getView();

    SpinnerStyle getSpinnerStyle();

    void setPrimaryColors(@ColorInt int... var1);

    void onInitialized(RefreshKernel var1, int var2, int var3);

    void onHorizontalDrag(float var1, int var2, int var3);

    void onStartAnimator(RefreshLayout var1, int var2, int var3);

    int onFinish(RefreshLayout var1, boolean var2);

    boolean isSupportHorizontalDrag();
}
