package com.clock.zc.punchtheclock.view.smart.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;


public class FalsifyHeader extends View implements RefreshHeader {
    protected RefreshKernel mRefreshKernel;
    protected Boolean mPureScrollMode;

    public FalsifyHeader(Context context) {
        super(context);
    }

    public FalsifyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FalsifyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(21)
    public FalsifyHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.isInEditMode()) {
            int d = DensityUtil.dp2px(5.0F);
            Paint paint = new Paint();
            paint.setStyle(Style.STROKE);
            paint.setColor(1157627903);
            paint.setStrokeWidth((float)DensityUtil.dp2px(1.0F));
            paint.setPathEffect(new DashPathEffect(new float[]{(float)d, (float)d, (float)d, (float)d}, 1.0F));
            canvas.drawRect((float)d, (float)d, (float)(this.getWidth() - d), (float)(this.getBottom() - d), paint);
            TextView textView = new TextView(this.getContext());
            textView.setText(this.getClass().getSimpleName() + " 虚假区域\n运行时代表下拉Header的高度【" + DensityUtil.px2dp((float)this.getHeight()) + "dp】\n而不会显示任何东西");
            textView.setTextColor(1157627903);
            textView.setGravity(17);
            textView.measure(MeasureSpec.makeMeasureSpec(this.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(this.getHeight(), MeasureSpec.EXACTLY));
            textView.layout(0, 0, this.getWidth(), this.getHeight());
            textView.draw(canvas);
        }

    }

    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        this.mRefreshKernel = kernel;
    }

    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
    }

    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
    }

    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
    }

    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        if(this.mRefreshKernel != null) {
            this.mRefreshKernel.resetStatus();
        }

    }

    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch(newState) {
            case None:
            case PullDownToRefresh:
                if(this.mPureScrollMode != null && this.mPureScrollMode.booleanValue() != refreshLayout.isEnablePureScrollMode()) {
                    refreshLayout.setEnablePureScrollMode(this.mPureScrollMode.booleanValue());
                }
            case PullToUpLoad:
            case PullUpCanceled:
            case ReleaseToRefresh:
            case ReleaseToLoad:
            default:
                break;
            case PullDownCanceled:
                this.mPureScrollMode = Boolean.valueOf(refreshLayout.isEnablePureScrollMode());
                if(!this.mPureScrollMode.booleanValue()) {
                    refreshLayout.setEnablePureScrollMode(true);
                }
        }

    }

    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
    }

    @NonNull
    public View getView() {
        return this;
    }

    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }
}
