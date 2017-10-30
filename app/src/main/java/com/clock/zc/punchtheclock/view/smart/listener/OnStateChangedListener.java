package com.clock.zc.punchtheclock.view.smart.listener;

import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;

/**
 * Created by Zc on 2017/10/17.
 */

public interface OnStateChangedListener {
    void onStateChanged(RefreshLayout var1, RefreshState var2, RefreshState var3);
}

