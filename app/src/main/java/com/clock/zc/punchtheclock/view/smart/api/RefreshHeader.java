package com.clock.zc.punchtheclock.view.smart.api;

public interface RefreshHeader extends RefreshInternal {
    void onPullingDown(float var1, int var2, int var3, int var4);

    void onReleasing(float var1, int var2, int var3, int var4);

    void onRefreshReleased(RefreshLayout var1, int var2, int var3);
}

