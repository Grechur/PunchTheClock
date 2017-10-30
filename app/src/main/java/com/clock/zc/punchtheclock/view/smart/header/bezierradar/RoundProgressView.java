package com.clock.zc.punchtheclock.view.smart.header.bezierradar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;

public class RoundProgressView extends View {
    private Paint mPath;
    private Paint mPantR;
    private ValueAnimator mAnimator;
    private int endAngle = 0;
    private int stratAngle = 270;
    private int mRadius = 0;
    private int mOutsideCircle = 0;
    private RectF mRect = new RectF(0.0F, 0.0F, 0.0F, 0.0F);

    public RoundProgressView(Context context) {
        super(context);
        this.initView();
    }

    private void initView() {
        this.mPath = new Paint();
        this.mPantR = new Paint();
        this.mPath.setAntiAlias(true);
        this.mPantR.setAntiAlias(true);
        this.mPath.setColor(-1);
        this.mPantR.setColor(1426063360);
        DensityUtil density = new DensityUtil();
        this.mRadius = density.dip2px(20.0F);
        this.mOutsideCircle = density.dip2px(7.0F);
        this.mPath.setStrokeWidth((float)density.dip2px(3.0F));
        this.mPantR.setStrokeWidth((float)density.dip2px(3.0F));
        this.mAnimator = ValueAnimator.ofInt(new int[]{0, 360});
        this.mAnimator.setDuration(720L);
        this.mAnimator.setRepeatCount(-1);
        this.mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                RoundProgressView.this.endAngle = ((Integer)animation.getAnimatedValue()).intValue();
                RoundProgressView.this.postInvalidate();
            }
        });
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAnimator.removeAllUpdateListeners();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void setBackColor(@ColorInt int backColor) {
        this.mPantR.setColor(backColor & 16777215 | 1426063360);
    }

    public void setFrontColor(@ColorInt int color) {
        this.mPath.setColor(color);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        if(this.isInEditMode()) {
            this.stratAngle = 0;
            this.endAngle = 270;
        }

        this.mPath.setStyle(Style.FILL);
        canvas.drawCircle((float)(width / 2), (float)(height / 2), (float)this.mRadius, this.mPath);
        this.mPath.setStyle(Style.STROKE);
        canvas.drawCircle((float)(width / 2), (float)(height / 2), (float)(this.mRadius + this.mOutsideCircle), this.mPath);
        this.mPantR.setStyle(Style.FILL);
        this.mRect.set((float)(width / 2 - this.mRadius), (float)(height / 2 - this.mRadius), (float)(width / 2 + this.mRadius), (float)(height / 2 + this.mRadius));
        canvas.drawArc(this.mRect, (float)this.stratAngle, (float)this.endAngle, true, this.mPantR);
        this.mRadius += this.mOutsideCircle;
        this.mPantR.setStyle(Style.STROKE);
        this.mRect.set((float)(width / 2 - this.mRadius), (float)(height / 2 - this.mRadius), (float)(width / 2 + this.mRadius), (float)(height / 2 + this.mRadius));
        canvas.drawArc(this.mRect, (float)this.stratAngle, (float)this.endAngle, false, this.mPantR);
        this.mRadius -= this.mOutsideCircle;
    }

    public void startAnim() {
        if(this.mAnimator != null) {
            this.mAnimator.start();
        }

    }

    public void stopAnim() {
        if(this.mAnimator != null && this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }

    }
}
