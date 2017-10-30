package com.clock.zc.punchtheclock.view.smart.listener;

import com.clock.zc.punchtheclock.view.smart.api.RefreshFooter;
import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;

/**
 * Created by Zc on 2017/10/17.
 */

public interface OnMultiPurposeListener extends OnRefreshLoadmoreListener, OnStateChangedListener {
    void onHeaderPulling(RefreshHeader var1, float var2, int var3, int var4, int var5);

    void onHeaderReleasing(RefreshHeader var1, float var2, int var3, int var4, int var5);

    void onHeaderStartAnimator(RefreshHeader var1, int var2, int var3);

    void onHeaderFinish(RefreshHeader var1, boolean var2);

    void onFooterPulling(RefreshFooter var1, float var2, int var3, int var4, int var5);

    void onFooterReleasing(RefreshFooter var1, float var2, int var3, int var4, int var5);

    void onFooterStartAnimator(RefreshFooter var1, int var2, int var3);

    void onFooterFinish(RefreshFooter var1, boolean var2);
}
