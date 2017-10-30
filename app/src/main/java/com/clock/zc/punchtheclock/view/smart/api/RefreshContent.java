package com.clock.zc.punchtheclock.view.smart.api;

import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public interface RefreshContent {
    void moveSpinner(int var1);

    boolean canRefresh();

    boolean canLoadmore();

    int getMeasuredWidth();

    int getMeasuredHeight();

    void measure(int var1, int var2);

    void layout(int var1, int var2, int var3, int var4, boolean var5);

    View getView();

    View getScrollableView();

    LayoutParams getLayoutParams();

    void onActionDown(MotionEvent var1);

    void onActionUpOrCancel();

    void setupComponent(RefreshKernel var1, View var2, View var3);

    void onInitialHeaderAndFooter(int var1, int var2);

    void setScrollBoundaryDecider(ScrollBoundaryDecider var1);

    void setEnableLoadmoreWhenContentNotFull(boolean var1);

    AnimatorUpdateListener onLoadingFinish(RefreshKernel var1, int var2, int var3, int var4);
}
