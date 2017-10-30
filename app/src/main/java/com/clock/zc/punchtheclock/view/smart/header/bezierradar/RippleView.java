package com.clock.zc.punchtheclock.view.smart.header.bezierradar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.ColorInt;
import android.view.View;

public class RippleView extends View {
    private int mRadius;
    private Paint mPaint = new Paint();
    private ValueAnimator mAnimator;

    public RippleView(Context context) {
        super(context);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.FILL);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void setFrontColor(@ColorInt int color) {
        this.mPaint.setColor(color);
    }

    public void startReveal() {
        if(this.mAnimator == null) {
            int bigRadius = (int)Math.sqrt(Math.pow((double)this.getHeight(), 2.0D) + Math.pow((double)this.getWidth(), 2.0D));
            this.mAnimator = ValueAnimator.ofInt(new int[]{0, bigRadius});
            this.mAnimator.setDuration(400L);
            this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RippleView.this.mRadius = ((Integer)animation.getAnimatedValue()).intValue();
                    RippleView.this.invalidate();
                }
            });
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                }
            });
        }

        this.mAnimator.start();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float)(this.getWidth() / 2), (float)(this.getHeight() / 2), (float)this.mRadius, this.mPaint);
    }
}