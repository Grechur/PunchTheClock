package com.clock.zc.punchtheclock.view.smart.header.bezierradar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    private int waveHeight;
    private int headHeight;
    private Path path;
    private Paint paint;
    private int mOffsetX;

    public WaveView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOffsetX = -1;
        this.initView();
    }

    private void initView() {
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setColor(-14736346);
        this.paint.setAntiAlias(true);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void setWaveColor(@ColorInt int color) {
        this.paint.setColor(color);
    }

    public int getHeadHeight() {
        return this.headHeight;
    }

    public void setHeadHeight(int headHeight) {
        this.headHeight = headHeight;
    }

    public int getWaveHeight() {
        return this.waveHeight;
    }

    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        this.path.reset();
        this.path.lineTo(0.0F, (float)this.headHeight);
        this.path.quadTo(this.mOffsetX >= 0?(float)this.mOffsetX:(float)(width / 2), (float)(this.headHeight + this.waveHeight), (float)width, (float)this.headHeight);
        this.path.lineTo((float)width, 0.0F);
        canvas.drawPath(this.path, this.paint);
    }

    public void setWaveOffsetX(int offset) {
        this.mOffsetX = offset;
    }
}
