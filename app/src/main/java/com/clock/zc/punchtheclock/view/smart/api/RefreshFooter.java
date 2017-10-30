package com.clock.zc.punchtheclock.view.smart.api;


public interface RefreshFooter extends RefreshInternal {
    void onPullingUp(float var1, int var2, int var3, int var4);

    void onPullReleasing(float var1, int var2, int var3, int var4);

    void onLoadmoreReleased(RefreshLayout var1, int var2, int var3);

    boolean setLoadmoreFinished(boolean var1);
}

