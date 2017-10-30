package com.clock.zc.punchtheclock.view.smart.constant;

/**
 * Created by Zc on 2017/10/17.
 */
public enum DimensionStatus {
    DefaultUnNotify(false),
    Default(true),
    XmlWrap(true),
    XmlExact(true),
    XmlLayoutUnNotify(false),
    XmlLayout(true),
    CodeExactUnNotify(false),
    CodeExact(true),
    DeadLockUnNotify(false),
    DeadLock(true);

    public final boolean notifyed;

    private DimensionStatus(boolean notifyed) {
        this.notifyed = notifyed;
    }

    public DimensionStatus unNotify() {
        if(this.notifyed) {
            DimensionStatus prev = values()[this.ordinal() - 1];
            return !prev.notifyed?prev:DefaultUnNotify;
        } else {
            return this;
        }
    }

    public DimensionStatus notifyed() {
        return !this.notifyed?values()[this.ordinal() + 1]:this;
    }

    public boolean canReplaceWith(DimensionStatus status) {
        return this.ordinal() < status.ordinal() || (!this.notifyed || CodeExact == this) && this.ordinal() == status.ordinal();
    }

    public boolean gteReplaceWith(DimensionStatus status) {
        return this.ordinal() >= status.ordinal();
    }
}

