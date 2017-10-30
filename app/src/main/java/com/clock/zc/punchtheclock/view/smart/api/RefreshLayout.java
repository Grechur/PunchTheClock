package com.clock.zc.punchtheclock.view.smart.api;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.listener.OnLoadmoreListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnMultiPurposeListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnRefreshListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnRefreshLoadmoreListener;

public interface RefreshLayout {
    RefreshLayout setFooterHeight(float var1);

    RefreshLayout setFooterHeightPx(int var1);

    RefreshLayout setHeaderHeight(float var1);

    RefreshLayout setHeaderHeightPx(int var1);

    RefreshLayout setDragRate(float var1);

    RefreshLayout setHeaderMaxDragRate(float var1);

    RefreshLayout setFooterMaxDragRate(float var1);

    RefreshLayout setHeaderTriggerRate(float var1);

    RefreshLayout setFooterTriggerRate(float var1);

    RefreshLayout setReboundInterpolator(Interpolator var1);

    RefreshLayout setReboundDuration(int var1);

    RefreshLayout setEnableLoadmore(boolean var1);

    RefreshLayout setEnableRefresh(boolean var1);

    RefreshLayout setEnableHeaderTranslationContent(boolean var1);

    RefreshLayout setEnableFooterTranslationContent(boolean var1);

    RefreshLayout setDisableContentWhenRefresh(boolean var1);

    RefreshLayout setDisableContentWhenLoading(boolean var1);

    RefreshLayout setEnableAutoLoadmore(boolean var1);

    RefreshLayout setLoadmoreFinished(boolean var1);

    RefreshLayout setRefreshFooter(RefreshFooter var1);

    RefreshLayout setRefreshFooter(RefreshFooter var1, int var2, int var3);

    RefreshLayout setRefreshHeader(RefreshHeader var1);

    RefreshLayout setRefreshHeader(RefreshHeader var1, int var2, int var3);

    RefreshLayout setRefreshContent(View var1);

    RefreshLayout setRefreshContent(View var1, int var2, int var3);

    RefreshLayout setEnableOverScrollBounce(boolean var1);

    RefreshLayout setEnablePureScrollMode(boolean var1);

    RefreshLayout setEnableScrollContentWhenLoaded(boolean var1);

    RefreshLayout setEnableLoadmoreWhenContentNotFull(boolean var1);

    RefreshLayout setEnableNestedScroll(boolean var1);

    RefreshLayout setOnRefreshListener(OnRefreshListener var1);

    RefreshLayout setOnLoadmoreListener(OnLoadmoreListener var1);

    RefreshLayout setOnRefreshLoadmoreListener(OnRefreshLoadmoreListener var1);

    RefreshLayout setOnMultiPurposeListener(OnMultiPurposeListener var1);

    RefreshLayout setPrimaryColorsId(@ColorRes int... var1);

    RefreshLayout setPrimaryColors(int... var1);

    RefreshLayout setScrollBoundaryDecider(ScrollBoundaryDecider var1);

    RefreshLayout finishRefresh();

    RefreshLayout finishLoadmore();

    RefreshLayout finishRefresh(int var1);

    RefreshLayout finishRefresh(boolean var1);

    RefreshLayout finishRefresh(int var1, boolean var2);

    RefreshLayout finishLoadmore(int var1);

    RefreshLayout finishLoadmore(boolean var1);

    RefreshLayout finishLoadmore(int var1, boolean var2);

    @Nullable
    RefreshHeader getRefreshHeader();

    @Nullable
    RefreshFooter getRefreshFooter();

    RefreshState getState();

    ViewGroup getLayout();

    boolean isRefreshing();

    boolean isLoading();

    boolean autoRefresh();

    boolean autoRefresh(int var1);

    boolean autoRefresh(int var1, float var2);

    boolean autoLoadmore();

    boolean autoLoadmore(int var1);

    boolean autoLoadmore(int var1, float var2);

    boolean isEnableRefresh();

    boolean isEnableLoadmore();

    boolean isLoadmoreFinished();

    boolean isEnableAutoLoadmore();

    boolean isEnableOverScrollBounce();

    boolean isEnablePureScrollMode();

    boolean isEnableScrollContentWhenLoaded();
}