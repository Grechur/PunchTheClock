package com.clock.zc.punchtheclock.view.smart.constant;

public enum RefreshState {
    None,
    PullDownToRefresh,
    PullToUpLoad,
    PullDownCanceled,
    PullUpCanceled,
    ReleaseToRefresh,
    ReleaseToLoad,
    RefreshReleased,
    LoadReleased,
    Refreshing,
    Loading,
    RefreshFinish,
    LoadFinish;

    private RefreshState() {
    }

    public boolean isDraging() {
        return this.ordinal() >= PullDownToRefresh.ordinal() && this.ordinal() <= ReleaseToLoad.ordinal() && this != PullDownCanceled && this != PullUpCanceled;
    }

    public boolean isHeader() {
        return (this.ordinal() & 1) == 1;
    }

    public boolean isFooter() {
        return (this.ordinal() & 1) == 0 && this.ordinal() > 0;
    }
}
