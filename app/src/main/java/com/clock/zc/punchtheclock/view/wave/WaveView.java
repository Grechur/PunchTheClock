package com.clock.zc.punchtheclock.view.wave;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class WaveView extends View implements OnPreDrawListener {
    private static final long DROP_CIRCLE_ANIMATOR_DURATION = 500L;
    private static final long DROP_VERTEX_ANIMATION_DURATION = 500L;
    private static final long DROP_BOUNCE_ANIMATOR_DURATION = 500L;
    private static final int DROP_REMOVE_ANIMATOR_DURATION = 200;
    private static final int WAVE_ANIMATOR_DURATION = 1000;
    private static final float MAX_WAVE_HEIGHT = 0.2F;
    private static final int SHADOW_COLOR = -1728053248;
    private float mDropCircleRadius = 100.0F;
    private Paint mPaint;
    private Path mWavePath;
    private Path mDropTangentPath;
    private Path mDropCirclePath;
    private Path mShadowPath;
    private RectF mDropRect;
    private int mWidth;
    private float mCurrentCircleCenterY;
    private int mMaxDropHeight;
    private boolean mIsManualRefreshing = false;
    private boolean mDropHeightUpdated = false;
    private int mUpdateMaxDropHeight;
    private ValueAnimator mDropVertexAnimator;
    private ValueAnimator mDropBounceVerticalAnimator;
    private ValueAnimator mDropBounceHorizontalAnimator;
    private ValueAnimator mDropCircleAnimator;
    private ValueAnimator mDisappearCircleAnimator;
    private ValueAnimator mWaveReverseAnimator;
    private static final float[][] BEGIN_PHASE_POINTS = new float[][]{{0.1655F, 0.0F}, {0.4188F, -0.0109F}, {0.4606F, -0.0049F}, {0.4893F, 0.0F}, {0.4893F, 0.0F}, {0.5F, 0.0F}};
    private static final float[][] APPEAR_PHASE_POINTS = new float[][]{{0.1655F, 0.0F}, {0.5237F, 0.0553F}, {0.4557F, 0.0936F}, {0.3908F, 0.1302F}, {0.4303F, 0.2173F}, {0.5F, 0.2173F}};
    private static final float[][] EXPAND_PHASE_POINTS = new float[][]{{0.1655F, 0.0F}, {0.5909F, 0.0F}, {0.4557F, 0.1642F}, {0.3941F, 0.2061F}, {0.4303F, 0.2889F}, {0.5F, 0.2889F}};
    private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            WaveView.this.postInvalidate();
        }
    };

    public WaveView(Context context) {
        super(context);
        this.getViewTreeObserver().addOnPreDrawListener(this);
        this.initView();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mWidth = w;
        this.mDropCircleRadius = (float)w / 14.4F;
        this.updateMaxDropHeight((int)Math.min((float)Math.min(w, h), (float)this.getHeight() - this.mDropCircleRadius));
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onPreDraw() {
        this.getViewTreeObserver().removeOnPreDrawListener(this);
        if(this.mDropHeightUpdated) {
            this.updateMaxDropHeight(this.mUpdateMaxDropHeight);
        }

        return false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.mWavePath, this.mPaint);
        if(!this.isInEditMode()) {
            this.mWavePath.rewind();
            this.mDropTangentPath.rewind();
            this.mDropCirclePath.rewind();
        }

        float circleCenterY = ((Float)this.mDropCircleAnimator.getAnimatedValue()).floatValue();
        float circleCenterX = (float)this.mWidth / 2.0F;
        this.mDropRect.setEmpty();
        float scale = ((Float)this.mDisappearCircleAnimator.getAnimatedValue()).floatValue();
        float vertical = ((Float)this.mDropBounceVerticalAnimator.getAnimatedValue()).floatValue();
        float horizontal = ((Float)this.mDropBounceHorizontalAnimator.getAnimatedValue()).floatValue();
        this.mDropRect.set(circleCenterX - this.mDropCircleRadius * (1.0F + vertical) * scale + this.mDropCircleRadius * horizontal / 2.0F, circleCenterY + this.mDropCircleRadius * (1.0F + horizontal) * scale - this.mDropCircleRadius * vertical / 2.0F, circleCenterX + this.mDropCircleRadius * (1.0F + vertical) * scale - this.mDropCircleRadius * horizontal / 2.0F, circleCenterY - this.mDropCircleRadius * (1.0F + horizontal) * scale + this.mDropCircleRadius * vertical / 2.0F);
        float vertex = ((Float)this.mDropVertexAnimator.getAnimatedValue()).floatValue();
        this.mDropTangentPath.moveTo(circleCenterX, vertex);
        double q = (Math.pow((double)this.mDropCircleRadius, 2.0D) + (double)(circleCenterY * vertex) - Math.pow((double)circleCenterY, 2.0D)) / (double)(vertex - circleCenterY);
        double b = -2.0D * (double)this.mWidth / 2.0D;
        double c = Math.pow(q - (double)circleCenterY, 2.0D) + Math.pow((double)circleCenterX, 2.0D) - Math.pow((double)this.mDropCircleRadius, 2.0D);
        double p1 = (-b + Math.sqrt(b * b - 4.0D * c)) / 2.0D;
        double p2 = (-b - Math.sqrt(b * b - 4.0D * c)) / 2.0D;
        this.mDropTangentPath.lineTo((float)p1, (float)q);
        this.mDropTangentPath.lineTo((float)p2, (float)q);
        this.mDropTangentPath.close();
        this.mShadowPath.set(this.mDropTangentPath);
        this.mShadowPath.addOval(this.mDropRect, Direction.CCW);
        this.mDropCirclePath.addOval(this.mDropRect, Direction.CCW);
        if(this.mDropVertexAnimator.isRunning()) {
            ;
        }

        canvas.drawPath(this.mDropTangentPath, this.mPaint);
        canvas.drawPath(this.mDropCirclePath, this.mPaint);
    }

    protected void onDetachedFromWindow() {
        if(this.mDisappearCircleAnimator != null) {
            this.mDisappearCircleAnimator.end();
            this.mDisappearCircleAnimator.removeAllUpdateListeners();
        }

        if(this.mDropCircleAnimator != null) {
            this.mDropCircleAnimator.end();
            this.mDropCircleAnimator.removeAllUpdateListeners();
        }

        if(this.mDropVertexAnimator != null) {
            this.mDropVertexAnimator.end();
            this.mDropVertexAnimator.removeAllUpdateListeners();
        }

        if(this.mWaveReverseAnimator != null) {
            this.mWaveReverseAnimator.end();
            this.mWaveReverseAnimator.removeAllUpdateListeners();
        }

        if(this.mDropBounceHorizontalAnimator != null) {
            this.mDropBounceHorizontalAnimator.end();
            this.mDropBounceHorizontalAnimator.removeAllUpdateListeners();
        }

        if(this.mDropBounceVerticalAnimator != null) {
            this.mDropBounceVerticalAnimator.end();
            this.mDropBounceVerticalAnimator.removeAllUpdateListeners();
        }

        super.onDetachedFromWindow();
    }

    private void initView() {
        this.setUpPaint();
        this.setUpPath();
        this.resetAnimator();
        this.mDropRect = new RectF();
        this.setLayerType(1, (Paint)null);
    }

    private void setUpPaint() {
        float density = this.getResources().getDisplayMetrics().density;
        this.mPaint = new Paint();
        this.mPaint.setColor(-14575885);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setShadowLayer((float)((int)(0.5F + 2.0F * density)), 0.0F, 0.0F, -1728053248);
    }

    private void setUpPath() {
        this.mWavePath = new Path();
        this.mDropTangentPath = new Path();
        this.mDropCirclePath = new Path();
        this.mShadowPath = new Path();
    }

    private void resetAnimator() {
        this.mDropVertexAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 0.0F});
        this.mDropBounceVerticalAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 0.0F});
        this.mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 0.0F});
        this.mDropCircleAnimator = ValueAnimator.ofFloat(new float[]{-1000.0F, -1000.0F});
        this.mDropCircleAnimator.start();
        this.mDisappearCircleAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 1.0F});
        this.mDisappearCircleAnimator.setDuration(1L);
        this.mDisappearCircleAnimator.start();
    }

    private void onPreDragWave() {
        if(this.mWaveReverseAnimator != null && this.mWaveReverseAnimator.isRunning()) {
            this.mWaveReverseAnimator.cancel();
        }

    }

    public void manualRefresh() {
        if(!this.mIsManualRefreshing) {
            this.mIsManualRefreshing = true;
            this.mDropCircleAnimator = ValueAnimator.ofFloat(new float[]{(float)this.mMaxDropHeight, (float)this.mMaxDropHeight});
            this.mDropCircleAnimator.start();
            this.mDropVertexAnimator = ValueAnimator.ofFloat(new float[]{(float)this.mMaxDropHeight - this.mDropCircleRadius, (float)this.mMaxDropHeight - this.mDropCircleRadius});
            this.mDropVertexAnimator.start();
            this.mCurrentCircleCenterY = (float)this.mMaxDropHeight;
            this.postInvalidate();
        }
    }

    public void beginPhase(float move1) {
        this.onPreDragWave();
        this.mWavePath.moveTo(0.0F, 0.0F);
        this.mWavePath.cubicTo((float)this.mWidth * BEGIN_PHASE_POINTS[0][0], BEGIN_PHASE_POINTS[0][1], (float)this.mWidth * BEGIN_PHASE_POINTS[1][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[1][1] + move1), (float)this.mWidth * BEGIN_PHASE_POINTS[2][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[2][1] + move1));
        this.mWavePath.cubicTo((float)this.mWidth * BEGIN_PHASE_POINTS[3][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[3][1] + move1), (float)this.mWidth * BEGIN_PHASE_POINTS[4][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[4][1] + move1), (float)this.mWidth * BEGIN_PHASE_POINTS[5][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[5][1] + move1));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * BEGIN_PHASE_POINTS[4][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[4][1] + move1), (float)this.mWidth - (float)this.mWidth * BEGIN_PHASE_POINTS[3][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[3][1] + move1), (float)this.mWidth - (float)this.mWidth * BEGIN_PHASE_POINTS[2][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[2][1] + move1));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * BEGIN_PHASE_POINTS[1][0], (float)this.mWidth * (BEGIN_PHASE_POINTS[1][1] + move1), (float)this.mWidth - (float)this.mWidth * BEGIN_PHASE_POINTS[0][0], BEGIN_PHASE_POINTS[0][1], (float)this.mWidth, 0.0F);
        this.postInvalidateOnAnimation();
    }

    public void postInvalidateOnAnimation() {
        if(VERSION.SDK_INT >= 16) {
            super.postInvalidateOnAnimation();
        } else {
            super.invalidate();
        }

    }

    public void appearPhase(float move1, float move2) {
        this.onPreDragWave();
        this.mWavePath.moveTo(0.0F, 0.0F);
        this.mWavePath.cubicTo((float)this.mWidth * APPEAR_PHASE_POINTS[0][0], (float)this.mWidth * APPEAR_PHASE_POINTS[0][1], (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, APPEAR_PHASE_POINTS[2][0]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]));
        this.mWavePath.cubicTo((float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]), (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, APPEAR_PHASE_POINTS[4][0]), (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]), (float)this.mWidth * APPEAR_PHASE_POINTS[5][0], (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[0][1] + move1 + move2, APPEAR_PHASE_POINTS[5][1]));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, APPEAR_PHASE_POINTS[4][0]), (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]), (float)this.mWidth - (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]), (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]), (float)this.mWidth - (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, APPEAR_PHASE_POINTS[2][0]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]), (float)this.mWidth - (float)this.mWidth * APPEAR_PHASE_POINTS[0][0], (float)this.mWidth * APPEAR_PHASE_POINTS[0][1], (float)this.mWidth, 0.0F);
        this.mCurrentCircleCenterY = (float)this.mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + this.mDropCircleRadius;
        this.postInvalidateOnAnimation();
    }

    public void expandPhase(float move1, float move2, float move3) {
        this.onPreDragWave();
        this.mWavePath.moveTo(0.0F, 0.0F);
        this.mWavePath.cubicTo((float)this.mWidth * EXPAND_PHASE_POINTS[0][0], (float)this.mWidth * EXPAND_PHASE_POINTS[0][1], (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]) + move3, EXPAND_PHASE_POINTS[1][0]), (float)this.mWidth * Math.max(Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]) - move3, EXPAND_PHASE_POINTS[1][1]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, EXPAND_PHASE_POINTS[2][0]), (float)this.mWidth * Math.min(Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]) + move3, EXPAND_PHASE_POINTS[2][1]));
        this.mWavePath.cubicTo((float)this.mWidth * Math.min(Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]) + move3, EXPAND_PHASE_POINTS[3][0]), (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3, EXPAND_PHASE_POINTS[3][1]), (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, EXPAND_PHASE_POINTS[4][0]), (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]) + move3, EXPAND_PHASE_POINTS[4][1]), (float)this.mWidth * EXPAND_PHASE_POINTS[5][0], (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[0][1] + move1 + move2, APPEAR_PHASE_POINTS[5][1]) + move3, EXPAND_PHASE_POINTS[5][1]));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, EXPAND_PHASE_POINTS[4][0]), (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]) + move3, EXPAND_PHASE_POINTS[4][1]), (float)this.mWidth - (float)this.mWidth * Math.min(Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]) + move3, EXPAND_PHASE_POINTS[3][0]), (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3, EXPAND_PHASE_POINTS[3][1]), (float)this.mWidth - (float)this.mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, EXPAND_PHASE_POINTS[2][0]), (float)this.mWidth * Math.min(Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]) + move3, EXPAND_PHASE_POINTS[2][1]));
        this.mWavePath.cubicTo((float)this.mWidth - (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]) + move3, EXPAND_PHASE_POINTS[1][0]), (float)this.mWidth * Math.max(Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]) - move3, EXPAND_PHASE_POINTS[1][1]), (float)this.mWidth - (float)this.mWidth * EXPAND_PHASE_POINTS[0][0], (float)this.mWidth * EXPAND_PHASE_POINTS[0][1], (float)this.mWidth, 0.0F);
        this.mCurrentCircleCenterY = (float)this.mWidth * Math.min(Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3, EXPAND_PHASE_POINTS[3][1]) + this.mDropCircleRadius;
        this.postInvalidateOnAnimation();
    }

    private void updateMaxDropHeight(int height) {
        if(500.0F * ((float)this.mWidth / 1440.0F) > (float)height) {
            Log.w("WaveView", "DropHeight is more than " + 500.0F * ((float)this.mWidth / 1440.0F));
        } else {
            this.mMaxDropHeight = (int)Math.min((float)height, (float)this.getHeight() - this.mDropCircleRadius);
            if(this.mIsManualRefreshing) {
                this.mIsManualRefreshing = false;
                this.manualRefresh();
            }

        }
    }

    public void startDropAnimation() {
        this.mDisappearCircleAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 1.0F});
        this.mDisappearCircleAnimator.setDuration(1L);
        this.mDisappearCircleAnimator.start();
        this.mDropCircleAnimator = ValueAnimator.ofFloat(new float[]{500.0F * ((float)this.mWidth / 1440.0F), (float)this.mMaxDropHeight});
        this.mDropCircleAnimator.setDuration(500L);
        this.mDropCircleAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                WaveView.this.mCurrentCircleCenterY = ((Float)animation.getAnimatedValue()).floatValue();
                WaveView.this.postInvalidateOnAnimation();
            }
        });
        this.mDropCircleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mDropCircleAnimator.start();
        this.mDropVertexAnimator = ValueAnimator.ofFloat(new float[]{0.0F, (float)this.mMaxDropHeight - this.mDropCircleRadius});
        this.mDropVertexAnimator.setDuration(500L);
        this.mDropVertexAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mDropVertexAnimator.start();
        this.mDropBounceVerticalAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        this.mDropBounceVerticalAnimator.setDuration(500L);
        this.mDropBounceVerticalAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mDropBounceVerticalAnimator.setInterpolator(new DropBounceInterpolator());
        this.mDropBounceVerticalAnimator.setStartDelay(500L);
        this.mDropBounceVerticalAnimator.start();
        this.mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        this.mDropBounceHorizontalAnimator.setDuration(500L);
        this.mDropBounceHorizontalAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mDropBounceHorizontalAnimator.setInterpolator(new DropBounceInterpolator());
        this.mDropBounceHorizontalAnimator.setStartDelay(625L);
        this.mDropBounceHorizontalAnimator.start();
    }

    public void startDisappearCircleAnimation() {
        this.mDisappearCircleAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F});
        this.mDisappearCircleAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mDisappearCircleAnimator.setDuration(200L);
        this.mDisappearCircleAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                WaveView.this.resetAnimator();
                WaveView.this.mIsManualRefreshing = false;
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        this.mDisappearCircleAnimator.start();
    }

    public void startWaveAnimation(float h) {
        h = Math.min(h, 0.2F) * (float)this.mWidth;
        this.mWaveReverseAnimator = ValueAnimator.ofFloat(new float[]{h, 0.0F});
        this.mWaveReverseAnimator.setDuration(1000L);
        this.mWaveReverseAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float h = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                WaveView.this.mWavePath.moveTo(0.0F, 0.0F);
                WaveView.this.mWavePath.quadTo(0.25F * (float)WaveView.this.mWidth, 0.0F, 0.333F * (float)WaveView.this.mWidth, h * 0.5F);
                WaveView.this.mWavePath.quadTo((float)WaveView.this.mWidth * 0.5F, h * 1.4F, 0.666F * (float)WaveView.this.mWidth, h * 0.5F);
                WaveView.this.mWavePath.quadTo(0.75F * (float)WaveView.this.mWidth, 0.0F, (float)WaveView.this.mWidth, 0.0F);
                WaveView.this.postInvalidate();
            }
        });
        this.mWaveReverseAnimator.setInterpolator(new BounceInterpolator());
        this.mWaveReverseAnimator.start();
    }

    public void animationDropCircle() {
        if(!this.mDisappearCircleAnimator.isRunning()) {
            this.startDropAnimation();
            this.startWaveAnimation(0.1F);
        }
    }

    public float getCurrentCircleCenterY() {
        return this.mCurrentCircleCenterY;
    }

    public void setMaxDropHeight(int maxDropHeight) {
        if(this.mDropHeightUpdated) {
            this.updateMaxDropHeight(maxDropHeight);
        } else {
            this.mUpdateMaxDropHeight = maxDropHeight;
            this.mDropHeightUpdated = true;
            if(this.getViewTreeObserver().isAlive()) {
                this.getViewTreeObserver().removeOnPreDrawListener(this);
                this.getViewTreeObserver().addOnPreDrawListener(this);
            }
        }

    }

    public boolean isDisappearCircleAnimatorRunning() {
        return this.mDisappearCircleAnimator.isRunning();
    }

    public void setShadowRadius(int radius) {
        this.mPaint.setShadowLayer((float)radius, 0.0F, 0.0F, -1728053248);
    }

    public void setShadow(int radius, int color) {
        this.mPaint.setShadowLayer((float)radius, 0.0F, 0.0F, color);
    }

    public void setWaveColor(@ColorInt int color) {
        this.mPaint.setColor(color);
        this.invalidate();
    }

    public void setWaveARGBColor(int a, int r, int g, int b) {
        this.mPaint.setARGB(a, r, g, b);
        this.invalidate();
    }
}