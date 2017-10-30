package com.clock.zc.punchtheclock.view.smart.header.bezierradar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.view.View;

import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;

public class RoundDotView extends View {
    private int num = 7;
    private Paint mPath = new Paint();
    private float mRadius;
    private float fraction;

    public RoundDotView(Context context) {
        super(context);
        this.mPath.setAntiAlias(true);
        this.mPath.setColor(-1);
        this.mRadius = (float) DensityUtil.dp2px(7.0F);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void setDotColor(@ColorInt int color) {
        this.mPath.setColor(color);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        float wide = (float)(width / this.num) * this.fraction - (this.fraction > 1.0F?(this.fraction - 1.0F) * (float)(width / this.num) / this.fraction:0.0F);
        float high = (float)height - (this.fraction > 1.0F?(this.fraction - 1.0F) * (float)height / 2.0F / this.fraction:0.0F);

        for(int i = 0; i < this.num; ++i) {
            float index = 1.0F + (float)i - (1.0F + (float)this.num) / 2.0F;
            float alpha = 255.0F * (1.0F - 2.0F * (Math.abs(index) / (float)this.num));
            float x = DensityUtil.px2dp((float)height);
            this.mPath.setAlpha((int)((double)alpha * (1.0D - 1.0D / Math.pow((double)x / 800.0D + 1.0D, 15.0D))));
            float radius = this.mRadius * (1.0F - 1.0F / (x / 10.0F + 1.0F));
            canvas.drawCircle((float)(width / 2) - radius / 2.0F + wide * index, high / 2.0F, radius, this.mPath);
        }

    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
    }
}