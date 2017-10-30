package com.clock.zc.punchtheclock.view.wave;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;


import static android.view.View.MeasureSpec.EXACTLY;

public class WaveSwipeHeader extends ViewGroup implements RefreshHeader {
    private static final float MAX_PROGRESS_ROTATION_RATE = 0.8F;
    private WaveView mWaveView;
    private RefreshState mState;
    private WaveSwipeHeader.ProgressAnimationImageView mCircleView;
    private float mLastFirstBounds;

    public WaveSwipeHeader(Context context) {
        super(context);
        this.initView(context, (AttributeSet)null);
    }

    public WaveSwipeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public WaveSwipeHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    @RequiresApi(21)
    public WaveSwipeHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.addView(this.mWaveView = new WaveView(context));
        this.addView(this.mCircleView = new WaveSwipeHeader.ProgressAnimationImageView(this.getContext()));
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveSwipeHeader);
        int primaryColor = ta.getColor(R.styleable.WaveSwipeHeader_wshPrimaryColor, 0);
        int accentColor = ta.getColor(R.styleable.WaveSwipeHeader_wshAccentColor, 0);
        if(primaryColor != 0) {
            this.mWaveView.setWaveColor(primaryColor);
        }

        if(accentColor != 0) {
            this.mCircleView.setProgressColorSchemeColors(new int[]{accentColor});
        } else {
            this.mCircleView.setProgressColorSchemeColors(new int[]{-1});
        }

        if(ta.hasValue(R.styleable.WaveSwipeHeader_wshShadowRadius)) {
            int radius = ta.getDimensionPixelOffset(R.styleable.WaveSwipeHeader_wshShadowRadius, 0);
            int color = ta.getColor(R.styleable.WaveSwipeHeader_wshShadowColor, -16777216);
            this.mWaveView.setShadow(radius, color);
        }

        ta.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        this.mCircleView.measure();
        this.mWaveView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), EXACTLY), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), EXACTLY));
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mWaveView.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
        int thisWidth = this.getMeasuredWidth();
        int circleWidth = this.mCircleView.getMeasuredWidth();
        int circleHeight = this.mCircleView.getMeasuredHeight();
        this.mCircleView.layout((thisWidth - circleWidth) / 2, -circleHeight, (thisWidth + circleWidth) / 2, 0);
        if(this.isInEditMode()) {
            this.onPullingDown(0.99F, DensityUtil.dp2px(99.0F), DensityUtil.dp2px(100.0F), DensityUtil.dp2px(100.0F));
        }

    }

    public void setColorSchemeColors(int... colors) {
        this.mCircleView.setProgressColorSchemeColors(colors);
    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
        if(this.mState != RefreshState.Refreshing) {
            float dragPercent = Math.min(1.0F, percent);
            float adjustedPercent = (float)Math.max((double)dragPercent - 0.4D, 0.0D) * 5.0F / 3.0F;
            float tensionSlingshotPercent = percent > 3.0F?2.0F:(percent > 1.0F?percent - 1.0F:0.0F);
            float tensionPercent = (4.0F - tensionSlingshotPercent) * tensionSlingshotPercent / 8.0F;
            float rotation;
            if(percent < 1.0F) {
                rotation = adjustedPercent * 0.8F;
                this.mCircleView.setProgressStartEndTrim(0.0F, Math.min(0.8F, rotation));
                this.mCircleView.setArrowScale(Math.min(1.0F, adjustedPercent));
            }

            rotation = (-0.25F + 0.4F * adjustedPercent + tensionPercent * 2.0F) * 0.5F;
            this.mCircleView.setProgressRotation(rotation);
            this.mCircleView.setTranslationY(this.mWaveView.getCurrentCircleCenterY());
            float seed = 1.0F * (float)offset / (float)Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
            float firstBounds = seed * (5.0F - 2.0F * seed) / 3.5F;
            float secondBounds = firstBounds - WaveSwipeHeader.VERTICAL_DRAG_THRESHOLD.FIRST.val;
            float finalBounds = (firstBounds - WaveSwipeHeader.VERTICAL_DRAG_THRESHOLD.SECOND.val) / 5.0F;
            this.mLastFirstBounds = firstBounds;
            if(firstBounds < WaveSwipeHeader.VERTICAL_DRAG_THRESHOLD.FIRST.val) {
                this.mWaveView.beginPhase(firstBounds);
            } else if(firstBounds < WaveSwipeHeader.VERTICAL_DRAG_THRESHOLD.SECOND.val) {
                this.mWaveView.appearPhase(firstBounds, secondBounds);
            } else {
                this.mWaveView.expandPhase(firstBounds, secondBounds, finalBounds);
            }

        }
    }

    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
    }

    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        this.mLastFirstBounds = 0.0F;
        this.mWaveView.animationDropCircle();
        this.mCircleView.makeProgressTransparent();
        this.mCircleView.startProgress();
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 0.0F});
        animator.setDuration(500L);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                WaveSwipeHeader.this.mCircleView.setTranslationY(WaveSwipeHeader.this.mWaveView.getCurrentCircleCenterY() + (float)WaveSwipeHeader.this.mCircleView.getHeight() / 2.0F);
            }
        });
        animator.start();
    }

    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        this.mState = newState;
        switch(mState) {
            case None:
            case PullDownCanceled:
            case PullUpCanceled:
            default:
                this.mCircleView.showArrow(false);
                this.mCircleView.setProgressRotation(0.0F);
                this.mCircleView.setProgressStartEndTrim(0.0F, 0.0F);
                this.mWaveView.startWaveAnimation(this.mLastFirstBounds);
                this.mLastFirstBounds = 0.0F;
                break;
            case PullDownToRefresh:
                this.mCircleView.showArrow(true);
                this.mCircleView.scaleWithKeepingAspectRatio(1.0F);
                this.mCircleView.makeProgressTransparent();
                break;
            case PullToUpLoad:
                this.mCircleView.showArrow(false);
                this.mCircleView.setProgressRotation(0.0F);
                this.mCircleView.setProgressStartEndTrim(0.0F, 0.0F);
                this.mWaveView.startWaveAnimation(this.mLastFirstBounds);
                this.mLastFirstBounds = 0.0F;
        }

    }

    public int onFinish(RefreshLayout layout, boolean success) {
        Animation scaleDownAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                WaveSwipeHeader.this.mCircleView.scaleWithKeepingAspectRatio(1.0F - interpolatedTime);
            }
        };
        scaleDownAnimation.setDuration(200L);
        this.mCircleView.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                WaveSwipeHeader.this.mCircleView.stopProgress();
                WaveSwipeHeader.this.mCircleView.makeProgressTransparent();
                WaveSwipeHeader.this.mWaveView.startDisappearCircleAnimation();
            }
        });
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(scaleDownAnimation);
        return 0;
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if(colors.length > 0) {
            this.mWaveView.setWaveColor(colors[0]);
            if(colors.length > 1) {
                this.mCircleView.setProgressColorSchemeColors(new int[]{colors[1]});
            }
        }

    }

    @NonNull
    public View getView() {
        return this;
    }

    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.MatchLayout;
    }

    private class ProgressAnimationImageView extends AnimationImageView {
        private final MaterialProgressDrawable mProgress;

        public ProgressAnimationImageView(Context context) {
            super(context);
            this.mProgress = new MaterialProgressDrawable(context, WaveSwipeHeader.this);
            this.mProgress.setBackgroundColor(0);
            if(DisplayUtil.isOver600dp(this.getContext())) {
                this.mProgress.updateSizes(0);
            }

            this.setImageDrawable(this.mProgress);
        }

        public void measure() {
            int circleDiameter = this.mProgress.getIntrinsicWidth();
            this.measure(this.makeMeasureSpecExactly(circleDiameter), this.makeMeasureSpecExactly(circleDiameter));
        }

        private int makeMeasureSpecExactly(int length) {
            return MeasureSpec.makeMeasureSpec(length, EXACTLY);
        }

        public void makeProgressTransparent() {
            this.mProgress.setAlpha(255);
        }

        public void showArrow(boolean show) {
            this.mProgress.showArrow(show);
        }

        public void setArrowScale(float scale) {
            this.mProgress.setArrowScale(scale);
        }

        public void setProgressAlpha(int alpha) {
            this.mProgress.setAlpha(alpha);
        }

        public void setProgressStartEndTrim(float startAngle, float endAngle) {
            this.mProgress.setStartEndTrim(startAngle, endAngle);
        }

        public void setProgressRotation(float rotation) {
            this.mProgress.setProgressRotation(rotation);
        }

        public void startProgress() {
            this.mProgress.start();
        }

        public void stopProgress() {
            this.mProgress.stop();
        }

        public void setProgressColorSchemeColors(@NonNull int... colors) {
            this.mProgress.setColorSchemeColors(colors);
        }

        public void setProgressColorSchemeColorsFromResource(@IdRes int... resources) {
            Resources res = this.getResources();
            int[] colorRes = new int[resources.length];

            for(int i = 0; i < resources.length; ++i) {
                colorRes[i] = res.getColor(resources[i]);
            }

            WaveSwipeHeader.this.setColorSchemeColors(colorRes);
        }

        public void scaleWithKeepingAspectRatio(float scale) {
            this.setScaleX(scale);
            this.setScaleY(scale);
        }
    }

    private static enum VERTICAL_DRAG_THRESHOLD {
        FIRST(0.1F),
        SECOND(0.16F + FIRST.val),
        THIRD(0.5F + FIRST.val);

        final float val;

        private VERTICAL_DRAG_THRESHOLD(float val) {
            this.val = val;
        }
    }
}