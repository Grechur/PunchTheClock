package com.clock.zc.punchtheclock.view.smart.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;

public class RefreshHeaderWrapper implements RefreshHeader {
    private View mWrapperView;
    private SpinnerStyle mSpinnerStyle;

    public RefreshHeaderWrapper(View wrapper) {
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

    @NonNull
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

            return params != null && params.height == -1?(this.mSpinnerStyle = SpinnerStyle.Scale):(this.mSpinnerStyle = SpinnerStyle.Translate);
        }
    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        LayoutParams params = this.mWrapperView.getLayoutParams();
        if(params instanceof com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams) {
            kernel.requestDrawBackgoundForHeader(((com.clock.zc.punchtheclock.view.smart.SmartRefreshLayout.LayoutParams)params).backgroundColor);
        }

    }

    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
    }

    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
    }

    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
    }

    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
    }
}