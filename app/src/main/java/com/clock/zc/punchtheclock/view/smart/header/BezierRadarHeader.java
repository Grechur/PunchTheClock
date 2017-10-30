package com.clock.zc.punchtheclock.view.smart.header;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.header.bezierradar.RippleView;
import com.clock.zc.punchtheclock.view.smart.header.bezierradar.RoundDotView;
import com.clock.zc.punchtheclock.view.smart.header.bezierradar.RoundProgressView;
import com.clock.zc.punchtheclock.view.smart.header.bezierradar.WaveView;
import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;


public class BezierRadarHeader extends FrameLayout implements RefreshHeader {
    private WaveView mWaveView;
    private RippleView mRippleView;
    private RoundDotView mDotView;
    private RoundProgressView mProgressView;
    private boolean mEnableHorizontalDrag;

    public BezierRadarHeader(Context context) {
        this(context, (AttributeSet)null);
    }

    public BezierRadarHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierRadarHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mEnableHorizontalDrag = false;
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.setMinimumHeight(DensityUtil.dp2px(100.0F));
        this.mWaveView = new WaveView(this.getContext());
        this.mRippleView = new RippleView(this.getContext());
        this.mDotView = new RoundDotView(this.getContext());
        this.mProgressView = new RoundProgressView(this.getContext());
        if(this.isInEditMode()) {
            this.addView(this.mWaveView, -1, -1);
            this.addView(this.mProgressView, -1, -1);
            this.mWaveView.setHeadHeight(1000);
        } else {
            this.addView(this.mWaveView, -1, -1);
            this.addView(this.mDotView, -1, -1);
            this.addView(this.mProgressView, -1, -1);
            this.addView(this.mRippleView, -1, -1);
            this.mProgressView.setScaleX(0.0F);
            this.mProgressView.setScaleY(0.0F);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BezierRadarHeader);
        this.mEnableHorizontalDrag = ta.getBoolean(R.styleable.BezierRadarHeader_srlEnableHorizontalDrag, this.mEnableHorizontalDrag);
        int primaryColor = ta.getColor(R.styleable.BezierRadarHeader_srlPrimaryColor, 0);
        int accentColor = ta.getColor(R.styleable.BezierRadarHeader_srlAccentColor, 0);
        if(primaryColor != 0) {
            this.setPrimaryColor(primaryColor);
        }

        if(accentColor != 0) {
            this.setAccentColor(accentColor);
        }

        ta.recycle();
    }

    public BezierRadarHeader setPrimaryColor(@ColorInt int color) {
        this.mWaveView.setWaveColor(color);
        this.mProgressView.setBackColor(color);
        return this;
    }

    public BezierRadarHeader setAccentColor(@ColorInt int color) {
        this.mDotView.setDotColor(color);
        this.mRippleView.setFrontColor(color);
        this.mProgressView.setFrontColor(color);
        return this;
    }

    public BezierRadarHeader setPrimaryColorId(@ColorRes int colorId) {
        this.setPrimaryColor(ContextCompat.getColor(this.getContext(), colorId));
        return this;
    }

    public BezierRadarHeader setAccentColorId(@ColorRes int colorId) {
        this.setAccentColor(ContextCompat.getColor(this.getContext(), colorId));
        return this;
    }

    public BezierRadarHeader setEnableHorizontalDrag(boolean enable) {
        this.mEnableHorizontalDrag = enable;
        if(!enable) {
            this.mWaveView.setWaveOffsetX(-1);
        }

        return this;
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if(colors.length > 0) {
            this.setPrimaryColor(colors[0]);
        }

        if(colors.length > 1) {
            this.setAccentColor(colors[1]);
        }

    }

    @NonNull
    public View getView() {
        return this;
    }

    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    public boolean isSupportHorizontalDrag() {
        return this.mEnableHorizontalDrag;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        this.mWaveView.setWaveOffsetX(offsetX);
        this.mWaveView.invalidate();
    }

    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        this.mWaveView.setHeadHeight(Math.min(headerHeight, offset));
        this.mWaveView.setWaveHeight((int)(1.9F * (float)Math.max(0, offset - headerHeight)));
        this.mDotView.setFraction(percent);
    }

    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        this.onPullingDown(percent, offset, headerHeight, extendHeight);
    }

    public void onRefreshReleased(final RefreshLayout layout, int headerHeight, int extendHeight) {
        this.mWaveView.setHeadHeight(headerHeight);
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{this.mWaveView.getWaveHeight(), 0, -((int)((double)this.mWaveView.getWaveHeight() * 0.8D)), 0, -((int)((float)this.mWaveView.getWaveHeight() * 0.4F)), 0});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                BezierRadarHeader.this.mWaveView.setWaveHeight(((Integer)animation.getAnimatedValue()).intValue() / 2);
                BezierRadarHeader.this.mWaveView.invalidate();
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(800L);
        animator.start();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F});
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                BezierRadarHeader.this.mDotView.setVisibility(INVISIBLE);
                BezierRadarHeader.this.mProgressView.animate().scaleX(1.0F);
                BezierRadarHeader.this.mProgressView.animate().scaleY(1.0F);
                layout.getLayout().postDelayed(new Runnable() {
                    public void run() {
                        BezierRadarHeader.this.mProgressView.startAnim();
                    }
                }, 200L);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300L);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                BezierRadarHeader.this.mDotView.setAlpha(((Float)animation.getAnimatedValue()).floatValue());
            }
        });
        valueAnimator.start();
    }

    public void onStartAnimator(RefreshLayout layout, int headerHeight, int extendHeight) {
    }

    public int onFinish(RefreshLayout layout, boolean success) {
        this.mProgressView.stopAnim();
        this.mProgressView.animate().scaleX(0.0F);
        this.mProgressView.animate().scaleY(0.0F);
        this.mRippleView.setVisibility(VISIBLE);
        this.mRippleView.startReveal();
        return 400;
    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch(newState) {
            case None:

                mRippleView.setVisibility(GONE);
                mDotView.setAlpha(1);
                mDotView.setVisibility(VISIBLE);
                break;
            case PullDownToRefresh:
                this.mProgressView.setScaleX(0.0F);
                this.mProgressView.setScaleY(0.0F);
            case PullToUpLoad:
            case PullDownCanceled:
            case PullUpCanceled:
        }

    }
}