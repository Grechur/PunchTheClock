package com.clock.zc.punchtheclock.view.smart.api;

import android.support.annotation.NonNull;

public interface RefreshKernel {
    @NonNull
    RefreshLayout getRefreshLayout();

    @NonNull
    RefreshContent getRefreshContent();

    RefreshKernel setStatePullUpToLoad();

    RefreshKernel setStateReleaseToLoad();

    RefreshKernel setStateReleaseToRefresh();

    RefreshKernel setStatePullDownToRefresh();

    RefreshKernel setStatePullDownCanceled();

    RefreshKernel setStatePullUpCanceled();

    RefreshKernel setStateLoding();

    RefreshKernel setStateRefresing();

    RefreshKernel setStateLodingFinish();

    RefreshKernel setStateRefresingFinish();

    RefreshKernel resetStatus();

    RefreshKernel moveSpinner(int var1, boolean var2);

    RefreshKernel animSpinner(int var1);

    int getSpinner();

    RefreshKernel requestDrawBackgoundForHeader(int var1);

    RefreshKernel requestDrawBackgoundForFooter(int var1);

    RefreshKernel requestHeaderNeedTouchEventWhenRefreshing(boolean var1);

    RefreshKernel requestFooterNeedTouchEventWhenLoading(boolean var1);

    RefreshKernel requestRemeasureHeightForHeader();

    RefreshKernel requestRemeasureHeightForFooter();
}
