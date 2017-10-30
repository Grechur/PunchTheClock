package com.clock.zc.punchtheclock.view.smart.footer.ballpulse;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BallPulseView extends View {
    public static final int DEFAULT_SIZE = 50;
    private Paint mPaint;
    private int normalColor;
    private int animatingColor;
    private float circleSpacing;
    private float[] scaleFloats;
    private boolean mIsStarted;
    private ArrayList<ValueAnimator> mAnimators;
    private Map<ValueAnimator, AnimatorUpdateListener> mUpdateListeners;

    public BallPulseView(Context context) {
        this(context, (AttributeSet)null);
    }

    public BallPulseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallPulseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.normalColor = -1118482;
        this.animatingColor = -1615546;
        this.scaleFloats = new float[]{1.0F, 1.0F, 1.0F};
        this.mIsStarted = false;
        this.mUpdateListeners = new HashMap();
        this.circleSpacing = (float) DensityUtil.dp2px(4.0F);
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int default_size = DensityUtil.dp2px(50.0F);
        this.setMeasuredDimension(resolveSize(default_size, widthMeasureSpec), resolveSize(default_size, heightMeasureSpec));
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.mAnimators != null) {
            for(int i = 0; i < this.mAnimators.size(); ++i) {
                ((ValueAnimator)this.mAnimators.get(i)).cancel();
            }
        }

    }

    protected void onDraw(Canvas canvas) {
        float radius = ((float)Math.min(this.getWidth(), this.getHeight()) - this.circleSpacing * 2.0F) / 6.0F;
        float x = (float)(this.getWidth() / 2) - (radius * 2.0F + this.circleSpacing);
        float y = (float)(this.getHeight() / 2);

        for(int i = 0; i < 3; ++i) {
            canvas.save();
            float translateX = x + radius * 2.0F * (float)i + this.circleSpacing * (float)i;
            canvas.translate(translateX, y);
            canvas.scale(this.scaleFloats[i], this.scaleFloats[i]);
            canvas.drawCircle(0.0F, 0.0F, radius, this.mPaint);
            canvas.restore();
        }

    }

    private boolean isStarted() {
        return this.mIsStarted;
    }
    int i;
    private void createAnimators() {
        this.mAnimators = new ArrayList();
        int[] delays = new int[]{120, 240, 360};
        for(i = 0; i < 3; ++i) {
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(new float[]{1.0F, 0.3F, 1.0F});
            scaleAnim.setDuration(750L);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay((long)delays[i]);
            this.mUpdateListeners.put(scaleAnim, new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    BallPulseView.this.scaleFloats[i] = ((Float)animation.getAnimatedValue()).floatValue();
                    BallPulseView.this.postInvalidate();
                }
            });
            this.mAnimators.add(scaleAnim);
        }

    }

    public void setIndicatorColor(@ColorInt int color) {
        this.mPaint.setColor(color);
    }

    public void setNormalColor(@ColorInt int color) {
        this.normalColor = color;
    }

    public void setAnimatingColor(@ColorInt int color) {
        this.animatingColor = color;
    }

    public void startAnim() {
        if(this.mAnimators == null) {
            this.createAnimators();
        }

        if(this.mAnimators != null) {
            if(!this.isStarted()) {
                for(int i = 0; i < this.mAnimators.size(); ++i) {
                    ValueAnimator animator = (ValueAnimator)this.mAnimators.get(i);
                    AnimatorUpdateListener updateListener = (AnimatorUpdateListener)this.mUpdateListeners.get(animator);
                    if(updateListener != null) {
                        animator.addUpdateListener(updateListener);
                    }

                    animator.start();
                }

                this.mIsStarted = true;
                this.setIndicatorColor(this.animatingColor);
            }
        }
    }

    public void stopAnim() {
        if(this.mAnimators != null && this.mIsStarted) {
            this.mIsStarted = false;
            Iterator var1 = this.mAnimators.iterator();

            while(var1.hasNext()) {
                ValueAnimator animator = (ValueAnimator)var1.next();
                if(animator != null) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }

            this.scaleFloats = new float[]{1.0F, 1.0F, 1.0F};
        }

        this.setIndicatorColor(this.normalColor);
    }
}
