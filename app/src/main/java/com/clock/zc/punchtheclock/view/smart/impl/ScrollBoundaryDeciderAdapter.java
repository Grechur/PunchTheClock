package com.clock.zc.punchtheclock.view.smart.impl;

import android.view.MotionEvent;
import android.view.View;

import com.clock.zc.punchtheclock.view.smart.api.ScrollBoundaryDecider;
import com.clock.zc.punchtheclock.view.smart.util.ScrollBoundaryUtil;


public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {
    protected MotionEvent mActionEvent;
    protected ScrollBoundaryDecider boundary;
    protected boolean mEnableLoadmoreWhenContentNotFull;

    public ScrollBoundaryDeciderAdapter() {
    }

    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary) {
        this.boundary = boundary;
    }

    void setActionEvent(MotionEvent event) {
        this.mActionEvent = event;
    }

    public boolean canRefresh(View content) {
        return this.boundary != null?this.boundary.canRefresh(content): ScrollBoundaryUtil.canRefresh(content, this.mActionEvent);
    }

    public boolean canLoadmore(View content) {
        return this.boundary != null?this.boundary.canLoadmore(content):(this.mEnableLoadmoreWhenContentNotFull?!ScrollBoundaryUtil.canScrollDown(content, this.mActionEvent):ScrollBoundaryUtil.canLoadmore(content, this.mActionEvent));
    }

    public void setEnableLoadmoreWhenContentNotFull(boolean enable) {
        this.mEnableLoadmoreWhenContentNotFull = enable;
    }
}
