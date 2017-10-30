package com.clock.zc.punchtheclock.view.smart.footer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.view.smart.api.RefreshFooter;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.footer.ballpulse.BallPulseView;
import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;


public class BallPulseFooter extends ViewGroup implements RefreshFooter {
    private BallPulseView mBallPulseView;
    private SpinnerStyle mSpinnerStyle;

    public BallPulseFooter(@NonNull Context context) {
        super(context);
        this.mSpinnerStyle = SpinnerStyle.Translate;
        this.initView(context, (AttributeSet)null, 0);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mSpinnerStyle = SpinnerStyle.Translate;
        this.initView(context, attrs, 0);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSpinnerStyle = SpinnerStyle.Translate;
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mBallPulseView = new BallPulseView(context);
        this.addView(this.mBallPulseView, -2, -2);
        this.setMinimumHeight(DensityUtil.dp2px(60.0F));
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BallPulseFooter);
        int primaryColor = ta.getColor(R.styleable.BallPulseFooter_srlPrimaryColor, 0);
        int accentColor = ta.getColor(R.styleable.BallPulseFooter_srlAccentColor, 0);
        if(primaryColor != 0) {
            this.mBallPulseView.setNormalColor(primaryColor);
        }

        if(accentColor != 0) {
            this.mBallPulseView.setAnimatingColor(accentColor);
        }

        this.mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.BallPulseFooter_srlClassicsSpinnerStyle, this.mSpinnerStyle.ordinal())];
        ta.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        this.mBallPulseView.measure(widthSpec, heightSpec);
        this.setMeasuredDimension(resolveSize(this.mBallPulseView.getMeasuredWidth(), widthMeasureSpec), resolveSize(this.mBallPulseView.getMeasuredHeight(), heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pwidth = this.getMeasuredWidth();
        int pheight = this.getMeasuredHeight();
        int cwidth = this.mBallPulseView.getMeasuredWidth();
        int cheight = this.mBallPulseView.getMeasuredHeight();
        int left = pwidth / 2 - cwidth / 2;
        int top = pheight / 2 - cheight / 2;
        this.mBallPulseView.layout(left, top, left + cwidth, top + cheight);
    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {
    }

    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {
    }

    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {
    }

    public void onStartAnimator(RefreshLayout layout, int footerHeight, int extendHeight) {
        this.mBallPulseView.startAnim();
    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
    }

    public int onFinish(RefreshLayout layout, boolean success) {
        this.mBallPulseView.stopAnim();
        return 0;
    }

    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if(colors.length > 1) {
            this.mBallPulseView.setNormalColor(colors[1]);
            this.mBallPulseView.setAnimatingColor(colors[0]);
        } else if(colors.length > 0) {
            this.mBallPulseView.setNormalColor(ColorUtils.compositeColors(0x99ffffff, colors[0]));
            this.mBallPulseView.setAnimatingColor(colors[0]);
        }

    }

    @NonNull
    public View getView() {
        return this;
    }

    public SpinnerStyle getSpinnerStyle() {
        return this.mSpinnerStyle;
    }

    public BallPulseFooter setSpinnerStyle(SpinnerStyle mSpinnerStyle) {
        this.mSpinnerStyle = mSpinnerStyle;
        return this;
    }

    public BallPulseFooter setIndicatorColor(@ColorInt int color) {
        this.mBallPulseView.setIndicatorColor(color);
        return this;
    }

    public BallPulseFooter setNormalColor(@ColorInt int color) {
        this.mBallPulseView.setNormalColor(color);
        return this;
    }

    public BallPulseFooter setAnimatingColor(@ColorInt int color) {
        this.mBallPulseView.setAnimatingColor(color);
        return this;
    }
}