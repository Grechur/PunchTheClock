package com.clock.zc.punchtheclock.view.smart.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.clock.zc.punchtheclock.view.smart.api.RefreshFooter;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;

public class RefreshFooterWrapper implements RefreshFooter {
    private View mWrapperView;
    private SpinnerStyle mSpinnerStyle;

    public RefreshFooterWrapper(View wrapper) {
        this.mWrapperView = wrapper;
    }

    @NonNull
    public View getView() {
        return this.mWrapperView;
    }

    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
    }

    public SpinnerStyle getSpinnerStyle() {
        if(this.mSpinnerStyle != null) {
            return this.mSpinnerStyle;
        } else {
            LayoutParams params = this.mWrapperView.getLayoutParams();
            if(params instanceof com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams) {
                this.mSpinnerStyle = ((com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams)params).spinnerStyle;
                if(this.mSpinnerStyle != null) {
                    return this.mSpinnerStyle;
                }
            }

            return params != null && params.height == 0?(this.mSpinnerStyle = SpinnerStyle.Scale):(this.mSpinnerStyle = SpinnerStyle.Translate);
        }
    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        LayoutParams params = this.mWrapperView.getLayoutParams();
        if(params instanceof com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams) {
            kernel.requestDrawBackgoundForFooter(((com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams)params).backgroundColor);
        }

    }

    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {
    }

    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {
    }

    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {
    }

    public void onStartAnimator(RefreshLayout layout, int footerHeight, int extendHeight) {
    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
    }

    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }
}