package com.clock.zc.punchtheclock.view.smart.util;

public class DelayedRunable implements Runnable {
    public long delayMillis;
    public Runnable runnable = null;

    public DelayedRunable(Runnable runnable) {
        this.runnable = runnable;
    }

    public DelayedRunable(Runnable runnable, long delayMillis) {
        this.runnable = runnable;
        this.delayMillis = delayMillis;
    }

    public void run() {
        if(this.runnable != null) {
            this.runnable.run();
            this.runnable = null;
        }

    }
}
