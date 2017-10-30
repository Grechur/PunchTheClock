package com.clock.zc.punchtheclock.view.smart;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.view.smart.api.DefaultRefreshFooterCreater;
import com.clock.zc.punchtheclock.view.smart.api.DefaultRefreshHeaderCreater;
import com.clock.zc.punchtheclock.view.smart.api.RefreshContent;
import com.clock.zc.punchtheclock.view.smart.api.RefreshFooter;
import com.clock.zc.punchtheclock.view.smart.api.RefreshHeader;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.api.ScrollBoundaryDecider;
import com.clock.zc.punchtheclock.view.smart.constant.DimensionStatus;
import com.clock.zc.punchtheclock.view.smart.constant.RefreshState;
import com.clock.zc.punchtheclock.view.smart.constant.SpinnerStyle;
import com.clock.zc.punchtheclock.view.smart.footer.BallPulseFooter;
import com.clock.zc.punchtheclock.view.smart.header.BezierRadarHeader;
import com.clock.zc.punchtheclock.view.smart.header.FalsifyHeader;
import com.clock.zc.punchtheclock.view.smart.impl.RefreshContentWrapper;
import com.clock.zc.punchtheclock.view.smart.impl.RefreshFooterWrapper;
import com.clock.zc.punchtheclock.view.smart.impl.RefreshHeaderWrapper;
import com.clock.zc.punchtheclock.view.smart.listener.OnLoadmoreListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnMultiPurposeListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnRefreshListener;
import com.clock.zc.punchtheclock.view.smart.listener.OnRefreshLoadmoreListener;
import com.clock.zc.punchtheclock.view.smart.util.DelayedRunable;
import com.clock.zc.punchtheclock.view.smart.util.DensityUtil;
import com.clock.zc.punchtheclock.view.smart.util.ViscousFluidInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;

public class SmartRefreshLayout extends ViewGroup implements RefreshLayout, NestedScrollingParent, NestedScrollingChild {
    protected int mTouchSlop;
    protected int mSpinner;
    protected int mLastSpinner;
    protected int mTouchSpinner;
    protected int mReboundDuration = 250;
    protected int mScreenHeightPixels;
    protected float mTouchX;
    protected float mTouchY;
    protected float mLastTouchX;
    protected float mLastTouchY;
    protected float mDragRate = 0.5F;
    protected boolean mIsBeingDragged;
    protected boolean mIsSkipContentLayout;
    protected Interpolator mReboundInterpolator;
    protected int mFixedHeaderViewId;
    protected int mFixedFooterViewId;
    protected int mMinimumVelocity;
    protected int mMaximumVelocity;
    protected Scroller mScroller;
    protected VelocityTracker mVelocityTracker;
    protected int[] mPrimaryColors;
    protected boolean mEnableRefresh = true;
    protected boolean mEnableLoadmore = false;
    protected boolean mEnableHeaderTranslationContent = true;
    protected boolean mEnableFooterTranslationContent = true;
    protected boolean mEnablePreviewInEditMode = true;
    protected boolean mEnableOverScrollBounce = true;
    protected boolean mEnableAutoLoadmore = true;
    protected boolean mEnablePureScrollMode = false;
    protected boolean mEnableScrollContentWhenLoaded = true;
    protected boolean mEnableLoadmoreWhenContentNotFull = true;
    protected boolean mDisableContentWhenRefresh = false;
    protected boolean mDisableContentWhenLoading = false;
    protected boolean mLoadmoreFinished = false;
    protected boolean mManualLoadmore = false;
    protected boolean mManualNestedScrolling = false;
    protected OnRefreshListener mRefreshListener;
    protected OnLoadmoreListener mLoadmoreListener;
    protected OnMultiPurposeListener mOnMultiPurposeListener;
    protected ScrollBoundaryDecider mScrollBoundaryDecider;
    protected int[] mParentScrollConsumed = new int[2];
    protected int[] mParentOffsetInWindow = new int[2];
    protected int mTotalUnconsumed;
    protected boolean mNestedScrollInProgress;
    protected NestedScrollingChildHelper mNestedScrollingChildHelper;
    protected NestedScrollingParentHelper mNestedScrollingParentHelper;
    protected int mHeaderHeight;
    protected DimensionStatus mHeaderHeightStatus;
    protected int mFooterHeight;
    protected DimensionStatus mFooterHeightStatus;
    protected int mHeaderExtendHeight;
    protected int mFooterExtendHeight;
    protected float mHeaderMaxDragRate;
    protected float mFooterMaxDragRate;
    protected float mHeaderTriggerRate;
    protected float mFooterTriggerRate;
    protected RefreshHeader mRefreshHeader;
    protected RefreshFooter mRefreshFooter;
    protected RefreshContent mRefreshContent;
    protected Paint mPaint;
    protected Handler handler;
    protected RefreshKernel mKernel;
    protected List<DelayedRunable> mDelayedRunables;
    protected RefreshState mState;
    protected RefreshState mViceState;
    protected long mLastLoadingTime;
    protected long mLastRefreshingTime;
    protected int mHeaderBackgroundColor;
    protected int mFooterBackgroundColor;
    protected boolean mHeaderNeedTouchEventWhenRefreshing;
    protected boolean mFooterNeedTouchEventWhenRefreshing;
    protected static boolean sManualFooterCreater = false;
    protected static DefaultRefreshFooterCreater sFooterCreater = new DefaultRefreshFooterCreater() {
        @NonNull
        public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
            return new BallPulseFooter(context);
        }
    };
    protected static DefaultRefreshHeaderCreater sHeaderCreater = new DefaultRefreshHeaderCreater() {
        @NonNull
        public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
            return new BezierRadarHeader(context);
        }
    };
    MotionEvent mFalsifyEvent;
    protected ValueAnimator reboundAnimator;
    protected AnimatorListener reboundAnimatorEndListener;
    protected AnimatorUpdateListener reboundUpdateListener;

    public SmartRefreshLayout(Context context) {
        super(context);
        this.mHeaderHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mFooterHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mHeaderMaxDragRate = 2.5F;
        this.mFooterMaxDragRate = 2.5F;
        this.mHeaderTriggerRate = 1.0F;
        this.mFooterTriggerRate = 1.0F;
        this.mState = RefreshState.None;
        this.mViceState = RefreshState.None;
        this.mLastLoadingTime = 0L;
        this.mLastRefreshingTime = 0L;
        this.mHeaderBackgroundColor = 0;
        this.mFooterBackgroundColor = 0;
        this.mFalsifyEvent = null;
        this.reboundAnimatorEndListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.reboundAnimator = null;
                if(((Integer)((ValueAnimator)animation).getAnimatedValue()).intValue() == 0 && SmartRefreshLayout.this.mState != RefreshState.None && SmartRefreshLayout.this.mState != RefreshState.Refreshing && SmartRefreshLayout.this.mState != RefreshState.Loading) {
                    SmartRefreshLayout.this.notifyStateChanged(RefreshState.None);
                }

            }
        };
        this.reboundUpdateListener = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), true);
            }
        };
        this.initView(context, (AttributeSet)null);
    }

    public SmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHeaderHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mFooterHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mHeaderMaxDragRate = 2.5F;
        this.mFooterMaxDragRate = 2.5F;
        this.mHeaderTriggerRate = 1.0F;
        this.mFooterTriggerRate = 1.0F;
        this.mState = RefreshState.None;
        this.mViceState = RefreshState.None;
        this.mLastLoadingTime = 0L;
        this.mLastRefreshingTime = 0L;
        this.mHeaderBackgroundColor = 0;
        this.mFooterBackgroundColor = 0;
        this.mFalsifyEvent = null;
        this.reboundAnimatorEndListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.reboundAnimator = null;
                if(((Integer)((ValueAnimator)animation).getAnimatedValue()).intValue() == 0 && SmartRefreshLayout.this.mState != RefreshState.None && SmartRefreshLayout.this.mState != RefreshState.Refreshing && SmartRefreshLayout.this.mState != RefreshState.Loading) {
                    SmartRefreshLayout.this.notifyStateChanged(RefreshState.None);
                }

            }
        };
        this.reboundUpdateListener = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), true);
            }
        };
        this.initView(context, attrs);
    }

    public SmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHeaderHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mFooterHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mHeaderMaxDragRate = 2.5F;
        this.mFooterMaxDragRate = 2.5F;
        this.mHeaderTriggerRate = 1.0F;
        this.mFooterTriggerRate = 1.0F;
        this.mState = RefreshState.None;
        this.mViceState = RefreshState.None;
        this.mLastLoadingTime = 0L;
        this.mLastRefreshingTime = 0L;
        this.mHeaderBackgroundColor = 0;
        this.mFooterBackgroundColor = 0;
        this.mFalsifyEvent = null;
        this.reboundAnimatorEndListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.reboundAnimator = null;
                if(((Integer)((ValueAnimator)animation).getAnimatedValue()).intValue() == 0 && SmartRefreshLayout.this.mState != RefreshState.None && SmartRefreshLayout.this.mState != RefreshState.Refreshing && SmartRefreshLayout.this.mState != RefreshState.Loading) {
                    SmartRefreshLayout.this.notifyStateChanged(RefreshState.None);
                }

            }
        };
        this.reboundUpdateListener = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), true);
            }
        };
        this.initView(context, attrs);
    }

    @RequiresApi(21)
    public SmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mHeaderHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mFooterHeightStatus = DimensionStatus.DefaultUnNotify;
        this.mHeaderMaxDragRate = 2.5F;
        this.mFooterMaxDragRate = 2.5F;
        this.mHeaderTriggerRate = 1.0F;
        this.mFooterTriggerRate = 1.0F;
        this.mState = RefreshState.None;
        this.mViceState = RefreshState.None;
        this.mLastLoadingTime = 0L;
        this.mLastRefreshingTime = 0L;
        this.mHeaderBackgroundColor = 0;
        this.mFooterBackgroundColor = 0;
        this.mFalsifyEvent = null;
        this.reboundAnimatorEndListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.reboundAnimator = null;
                if(((Integer)((ValueAnimator)animation).getAnimatedValue()).intValue() == 0 && SmartRefreshLayout.this.mState != RefreshState.None && SmartRefreshLayout.this.mState != RefreshState.Refreshing && SmartRefreshLayout.this.mState != RefreshState.Loading) {
                    SmartRefreshLayout.this.notifyStateChanged(RefreshState.None);
                }

            }
        };
        this.reboundUpdateListener = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), true);
            }
        };
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.setClipToPadding(false);
        DensityUtil density = new DensityUtil();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mScroller = new Scroller(context);
        this.mScreenHeightPixels = context.getResources().getDisplayMetrics().heightPixels;
        this.mReboundInterpolator = new ViscousFluidInterpolator();
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRefreshLayout);
        ViewCompat.setNestedScrollingEnabled(this, ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableNestedScrolling, false));
        this.mDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlDragRate, this.mDragRate);
        this.mHeaderMaxDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlHeaderMaxDragRate, this.mHeaderMaxDragRate);
        this.mFooterMaxDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlFooterMaxDragRate, this.mFooterMaxDragRate);
        this.mHeaderTriggerRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlHeaderTriggerRate, this.mHeaderTriggerRate);
        this.mFooterTriggerRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlFooterTriggerRate, this.mFooterTriggerRate);
        this.mEnableRefresh = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableRefresh, this.mEnableRefresh);
        this.mReboundDuration = ta.getInt(R.styleable.SmartRefreshLayout_srlReboundDuration, this.mReboundDuration);
        this.mEnableLoadmore = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableLoadmore, this.mEnableLoadmore);
        this.mHeaderHeight = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlHeaderHeight, density.dip2px(100.0F));
        this.mFooterHeight = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlFooterHeight, density.dip2px(60.0F));
        this.mDisableContentWhenRefresh = ta.getBoolean(R.styleable.SmartRefreshLayout_srlDisableContentWhenRefresh, this.mDisableContentWhenRefresh);
        this.mDisableContentWhenLoading = ta.getBoolean(R.styleable.SmartRefreshLayout_srlDisableContentWhenLoading, this.mDisableContentWhenLoading);
        this.mEnableHeaderTranslationContent = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableHeaderTranslationContent, this.mEnableHeaderTranslationContent);
        this.mEnableFooterTranslationContent = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableFooterTranslationContent, this.mEnableFooterTranslationContent);
        this.mEnablePreviewInEditMode = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnablePreviewInEditMode, this.mEnablePreviewInEditMode);
        this.mEnableAutoLoadmore = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableAutoLoadmore, this.mEnableAutoLoadmore);
        this.mEnableOverScrollBounce = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableOverScrollBounce, this.mEnableOverScrollBounce);
        this.mEnablePureScrollMode = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnablePureScrollMode, this.mEnablePureScrollMode);
        this.mEnableScrollContentWhenLoaded = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableScrollContentWhenLoaded, this.mEnableScrollContentWhenLoaded);
        this.mEnableLoadmoreWhenContentNotFull = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableLoadmoreWhenContentNotFull, this.mEnableLoadmoreWhenContentNotFull);
        this.mFixedHeaderViewId = ta.getResourceId(R.styleable.SmartRefreshLayout_srlFixedHeaderViewId, -1);
        this.mFixedFooterViewId = ta.getResourceId(R.styleable.SmartRefreshLayout_srlFixedFooterViewId, -1);
        this.mManualLoadmore = ta.hasValue(R.styleable.SmartRefreshLayout_srlEnableLoadmore);
        this.mManualNestedScrolling = ta.hasValue(R.styleable.SmartRefreshLayout_srlEnableNestedScrolling);
        this.mHeaderHeightStatus = ta.hasValue(R.styleable.SmartRefreshLayout_srlHeaderHeight)?DimensionStatus.XmlLayoutUnNotify:this.mHeaderHeightStatus;
        this.mFooterHeightStatus = ta.hasValue(R.styleable.SmartRefreshLayout_srlFooterHeight)?DimensionStatus.XmlLayoutUnNotify:this.mFooterHeightStatus;
        this.mHeaderExtendHeight = (int)Math.max((float)this.mHeaderHeight * (this.mHeaderMaxDragRate - 1.0F), 0.0F);
        this.mFooterExtendHeight = (int)Math.max((float)this.mFooterHeight * (this.mFooterMaxDragRate - 1.0F), 0.0F);
        int accentColor = ta.getColor(R.styleable.SmartRefreshLayout_srlAccentColor, 0);
        int primaryColor = ta.getColor(R.styleable.SmartRefreshLayout_srlPrimaryColor, 0);
        if(primaryColor != 0) {
            if(accentColor != 0) {
                this.mPrimaryColors = new int[]{primaryColor, accentColor};
            } else {
                this.mPrimaryColors = new int[]{primaryColor};
            }
        }

        ta.recycle();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = this.getChildCount();
        if(count > 3) {
            throw new RuntimeException("最多只支持3个子View，Most only support three sub view");
        } else if(this.mEnablePureScrollMode && count > 1) {
            throw new RuntimeException("PureScrollMode模式只支持一个子View，Most only support one sub view in PureScrollMode");
        } else {
            boolean[] uncertains = new boolean[count];

            int i;
            View view;
            for(i = 0; i < count; ++i) {
                view = this.getChildAt(i);
                if(view instanceof RefreshHeader && this.mRefreshHeader == null) {
                    this.mRefreshHeader = (RefreshHeader)view;
                } else if(view instanceof RefreshFooter && this.mRefreshFooter == null) {
                    this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore;
                    this.mRefreshFooter = (RefreshFooter)view;
                } else if(this.mRefreshContent != null || !(view instanceof AbsListView) && !(view instanceof WebView) && !(view instanceof ScrollView) && !(view instanceof ScrollingView) && !(view instanceof NestedScrollingChild) && !(view instanceof NestedScrollingParent) && !(view instanceof ViewPager)) {
                    uncertains[i] = true;
                } else {
                    this.mRefreshContent = new RefreshContentWrapper(view);
                }
            }

            for(i = 0; i < count; ++i) {
                if(uncertains[i]) {
                    view = this.getChildAt(i);
                    if(count == 1 && this.mRefreshContent == null) {
                        this.mRefreshContent = new RefreshContentWrapper(view);
                    } else if(i == 0 && this.mRefreshHeader == null) {
                        this.mRefreshHeader = new RefreshHeaderWrapper(view);
                    } else if(count == 2 && this.mRefreshContent == null) {
                        this.mRefreshContent = new RefreshContentWrapper(view);
                    } else if(i == 2 && this.mRefreshFooter == null) {
                        this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore;
                        this.mRefreshFooter = new RefreshFooterWrapper(view);
                    } else if(this.mRefreshContent == null) {
                        this.mRefreshContent = new RefreshContentWrapper(view);
                    }
                }
            }

            if(this.isInEditMode()) {
                if(this.mPrimaryColors != null) {
                    if(this.mRefreshHeader != null) {
                        this.mRefreshHeader.setPrimaryColors(this.mPrimaryColors);
                    }

                    if(this.mRefreshFooter != null) {
                        this.mRefreshFooter.setPrimaryColors(this.mPrimaryColors);
                    }
                }

                if(this.mRefreshContent != null) {
                    this.bringChildToFront(this.mRefreshContent.getView());
                }

                if(this.mRefreshHeader != null && this.mRefreshHeader.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                    this.bringChildToFront(this.mRefreshHeader.getView());
                }

                if(this.mRefreshFooter != null && this.mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                    this.bringChildToFront(this.mRefreshFooter.getView());
                }

                if(this.mKernel == null) {
                    this.mKernel = new SmartRefreshLayout.RefreshKernelImpl();
                }
            }

        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!this.isInEditMode()) {
            if(this.mKernel == null) {
                this.mKernel = new SmartRefreshLayout.RefreshKernelImpl();
            }

            if(this.handler == null) {
                this.handler = new Handler();
            }

            if(this.mDelayedRunables != null) {
                Iterator var1 = this.mDelayedRunables.iterator();

                while(var1.hasNext()) {
                    DelayedRunable runable = (DelayedRunable)var1.next();
                    this.handler.postDelayed(runable, runable.delayMillis);
                }

                this.mDelayedRunables.clear();
                this.mDelayedRunables = null;
            }

            if(this.mRefreshHeader == null) {
                if(this.mEnablePureScrollMode) {
                    this.mRefreshHeader = new FalsifyHeader(this.getContext());
                } else {
                    this.mRefreshHeader = sHeaderCreater.createRefreshHeader(this.getContext(), this);
                }

                if(!(this.mRefreshHeader.getView().getLayoutParams() instanceof MarginLayoutParams)) {
                    if(this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale) {
                        this.addView(this.mRefreshHeader.getView(), -1, -1);
                    } else {
                        this.addView(this.mRefreshHeader.getView(), -1, -2);
                    }
                }
            }

            if(this.mRefreshFooter == null) {
                if(this.mEnablePureScrollMode) {
                    this.mRefreshFooter = new RefreshFooterWrapper(new FalsifyHeader(this.getContext()));
                    this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore;
                } else {
                    this.mRefreshFooter = sFooterCreater.createRefreshFooter(this.getContext(), this);
                    this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore && sManualFooterCreater;
                }

                if(!(this.mRefreshFooter.getView().getLayoutParams() instanceof MarginLayoutParams)) {
                    if(this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale) {
                        this.addView(this.mRefreshFooter.getView(), -1, -1);
                    } else {
                        this.addView(this.mRefreshFooter.getView(), -1, -2);
                    }
                }
            }

            if(this.mRefreshContent == null) {
                int i = 0;

                for(int len = this.getChildCount(); i < len; ++i) {
                    View view = this.getChildAt(i);
                    if((this.mRefreshHeader == null || view != this.mRefreshHeader.getView()) && (this.mRefreshFooter == null || view != this.mRefreshFooter.getView())) {
                        this.mRefreshContent = new RefreshContentWrapper(view);
                    }
                }

                if(this.mRefreshContent == null) {
                    this.mRefreshContent = new RefreshContentWrapper(this.getContext());
                    this.mRefreshContent.getView().setLayoutParams(new SmartRefreshLayout.LayoutParams(-1, -1));
                }
            }

            View fixedHeaderView = this.mFixedHeaderViewId > 0?this.findViewById(this.mFixedHeaderViewId):null;
            View fixedFooterView = this.mFixedFooterViewId > 0?this.findViewById(this.mFixedFooterViewId):null;
            this.mRefreshContent.setScrollBoundaryDecider(this.mScrollBoundaryDecider);
            this.mRefreshContent.setEnableLoadmoreWhenContentNotFull(this.mEnableLoadmoreWhenContentNotFull || this.mEnablePureScrollMode);
            this.mRefreshContent.setupComponent(this.mKernel, fixedHeaderView, fixedFooterView);
            if(this.mSpinner != 0) {
                this.notifyStateChanged(RefreshState.None);
                this.mRefreshContent.moveSpinner(this.mSpinner = 0);
            }

            this.bringChildToFront(this.mRefreshContent.getView());
            if(this.mRefreshHeader.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                this.bringChildToFront(this.mRefreshHeader.getView());
            }

            if(this.mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                this.bringChildToFront(this.mRefreshFooter.getView());
            }

            if(this.mRefreshListener == null) {
                this.mRefreshListener = new OnRefreshListener() {
                    public void onRefresh(RefreshLayout refreshlayout) {
                        refreshlayout.finishRefresh(3000);
                    }
                };
            }

            if(this.mLoadmoreListener == null) {
                this.mLoadmoreListener = new OnLoadmoreListener() {
                    public void onLoadmore(RefreshLayout refreshlayout) {
                        refreshlayout.finishLoadmore(2000);
                    }
                };
            }

            if(this.mPrimaryColors != null) {
                this.mRefreshHeader.setPrimaryColors(this.mPrimaryColors);
                this.mRefreshFooter.setPrimaryColors(this.mPrimaryColors);
            }

            try {
                if(!this.mManualNestedScrolling && !this.isNestedScrollingEnabled()) {
                    for(Object parent = this; parent != null; parent = ((ViewParent)parent).getParent()) {
                        if(parent instanceof CoordinatorLayout) {
                            this.setNestedScrollingEnabled(true);
                            this.mManualNestedScrolling = false;
                            break;
                        }
                    }
                }
            } catch (Throwable var4) {
                ;
            }

        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minimumHeight = 0;
        boolean isInEditMode = this.isInEditMode() && this.mEnablePreviewInEditMode;
        View footerView;
        SmartRefreshLayout.LayoutParams lp;
        int widthSpec;
        int heightSpec;
        int measuredHeight;
        if(this.mRefreshHeader != null) {
            footerView = this.mRefreshHeader.getView();
            lp = (SmartRefreshLayout.LayoutParams)footerView.getLayoutParams();
            widthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
            if(this.mHeaderHeightStatus.gteReplaceWith(DimensionStatus.XmlLayoutUnNotify)) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mHeaderHeight - lp.bottomMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else if(this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.MatchLayout) {
                footerView.measure(widthSpec, heightMeasureSpec);
            } else if(lp.height > 0) {
                if(this.mHeaderHeightStatus.canReplaceWith(DimensionStatus.XmlExact)) {
                    this.mHeaderHeightStatus = DimensionStatus.XmlExact;
                    this.mHeaderHeight = lp.height + lp.bottomMargin;
                    this.mHeaderExtendHeight = (int)Math.max((float)this.mHeaderHeight * (this.mHeaderMaxDragRate - 1.0F), 0.0F);
                    this.mRefreshHeader.onInitialized(this.mKernel, this.mHeaderHeight, this.mHeaderExtendHeight);
                }

                heightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else if(lp.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(MeasureSpec.getSize(heightMeasureSpec) - lp.bottomMargin, 0), AT_MOST);
                footerView.measure(widthSpec, heightSpec);
                measuredHeight = footerView.getMeasuredHeight();
                if(measuredHeight > 0 && this.mHeaderHeightStatus.canReplaceWith(DimensionStatus.XmlWrap)) {
                    this.mHeaderHeightStatus = DimensionStatus.XmlWrap;
                    this.mHeaderHeight = footerView.getMeasuredHeight() + lp.bottomMargin;
                    this.mHeaderExtendHeight = (int)Math.max((float)this.mHeaderHeight * (this.mHeaderMaxDragRate - 1.0F), 0.0F);
                    this.mRefreshHeader.onInitialized(this.mKernel, this.mHeaderHeight, this.mHeaderExtendHeight);
                } else if(measuredHeight <= 0) {
                    heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mHeaderHeight - lp.bottomMargin, 0), MeasureSpec.EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                }
            } else if(lp.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mHeaderHeight - lp.bottomMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else {
                footerView.measure(widthSpec, heightMeasureSpec);
            }

            if(this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale && !isInEditMode) {
                measuredHeight = Math.max(0, this.mSpinner);
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(measuredHeight - lp.bottomMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            }

            if(!this.mHeaderHeightStatus.notifyed) {
                this.mHeaderHeightStatus = this.mHeaderHeightStatus.notifyed();
                this.mRefreshHeader.onInitialized(this.mKernel, this.mHeaderHeight, this.mHeaderExtendHeight);
            }

            if(isInEditMode) {
                minimumHeight += footerView.getMeasuredHeight();
            }
        }

        if(this.mRefreshFooter != null) {
            footerView = this.mRefreshFooter.getView();
            lp = (SmartRefreshLayout.LayoutParams)footerView.getLayoutParams();
            widthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
            if(this.mFooterHeightStatus.gteReplaceWith(DimensionStatus.XmlLayoutUnNotify)) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mFooterHeight - lp.topMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else if(this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.MatchLayout) {
                footerView.measure(widthSpec, heightMeasureSpec);
            } else if(lp.height > 0) {
                if(this.mFooterHeightStatus.canReplaceWith(DimensionStatus.XmlExact)) {
                    this.mFooterHeightStatus = DimensionStatus.XmlExact;
                    this.mFooterHeight = lp.height + lp.topMargin;
                    this.mFooterExtendHeight = (int)Math.max((float)this.mFooterHeight * (this.mFooterMaxDragRate - 1.0F), 0.0F);
                    this.mRefreshFooter.onInitialized(this.mKernel, this.mFooterHeight, this.mFooterExtendHeight);
                }

                heightSpec = MeasureSpec.makeMeasureSpec(lp.height - lp.topMargin, MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else if(lp.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(MeasureSpec.getSize(heightMeasureSpec) - lp.topMargin, 0), AT_MOST);
                footerView.measure(widthSpec, heightSpec);
                measuredHeight = footerView.getMeasuredHeight();
                if(measuredHeight > 0 && this.mFooterHeightStatus.canReplaceWith(DimensionStatus.XmlWrap)) {
                    this.mFooterHeightStatus = DimensionStatus.XmlWrap;
                    this.mFooterHeight = footerView.getMeasuredHeight() + lp.topMargin;
                    this.mFooterExtendHeight = (int)Math.max((float)this.mFooterHeight * (this.mFooterMaxDragRate - 1.0F), 0.0F);
                    this.mRefreshFooter.onInitialized(this.mKernel, this.mFooterHeight, this.mFooterExtendHeight);
                } else if(measuredHeight <= 0) {
                    heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mFooterHeight - lp.topMargin, 0), MeasureSpec.EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                }
            } else if(lp.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(this.mFooterHeight - lp.topMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            } else {
                footerView.measure(widthSpec, heightMeasureSpec);
            }

            if(this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale && !isInEditMode) {
                measuredHeight = Math.max(0, -this.mSpinner);
                heightSpec = MeasureSpec.makeMeasureSpec(Math.max(measuredHeight - lp.topMargin, 0), MeasureSpec.EXACTLY);
                footerView.measure(widthSpec, heightSpec);
            }

            if(!this.mFooterHeightStatus.notifyed) {
                this.mFooterHeightStatus = this.mFooterHeightStatus.notifyed();
                this.mRefreshFooter.onInitialized(this.mKernel, this.mFooterHeight, this.mFooterExtendHeight);
            }

            if(isInEditMode) {
                minimumHeight += footerView.getMeasuredHeight();
            }
        }

        if(this.mRefreshContent != null) {
            lp = (SmartRefreshLayout.LayoutParams) this.mRefreshContent.getLayoutParams();
            widthSpec = getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            heightSpec = getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom() + lp.topMargin + lp.bottomMargin + (!isInEditMode || this.mRefreshHeader == null || !this.mEnableHeaderTranslationContent && this.mRefreshHeader.getSpinnerStyle() != SpinnerStyle.FixedBehind?0:this.mHeaderHeight) + (!isInEditMode || this.mRefreshFooter == null || !this.mEnableFooterTranslationContent && this.mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind?0:this.mFooterHeight), lp.height);
            this.mRefreshContent.measure(widthSpec, heightSpec);
            this.mRefreshContent.onInitialHeaderAndFooter(this.mHeaderHeight, this.mFooterHeight);
            minimumHeight += this.mRefreshContent.getMeasuredHeight();
        }

        this.setMeasuredDimension(resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(minimumHeight, heightMeasureSpec));
        this.mLastTouchX = (float)(this.getMeasuredWidth() / 2);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();
        boolean isInEditMode;
        if(this.mRefreshContent != null) {
            isInEditMode = this.isInEditMode() && this.mEnablePreviewInEditMode;
            SmartRefreshLayout.LayoutParams lp = (SmartRefreshLayout.LayoutParams)this.mRefreshContent.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = paddingTop + lp.topMargin;
            int right = left + mRefreshContent.getMeasuredWidth();
            int bottom = top + mRefreshContent.getMeasuredHeight();
            if(isInEditMode && this.mRefreshHeader != null && (this.mEnableHeaderTranslationContent || this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind)) {
                left += this.mHeaderHeight;
                top += this.mHeaderHeight;
            }

            this.mRefreshContent.layout(left, top, right, bottom, this.mIsSkipContentLayout);
            this.mIsSkipContentLayout = false;
        }

        View footerView;
        SmartRefreshLayout.LayoutParams lp;
        if(this.mRefreshHeader != null) {
            isInEditMode = this.isInEditMode() && this.mEnablePreviewInEditMode;
            footerView = this.mRefreshHeader.getView();
            lp = (SmartRefreshLayout.LayoutParams)footerView.getLayoutParams();
            int left = lp.leftMargin;
            int top = lp.topMargin;
            int right = left + footerView.getMeasuredWidth();
            int bottom = top + footerView.getMeasuredHeight();
            if(!isInEditMode) {
                if(this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Translate) {
                    top = top - this.mHeaderHeight + Math.max(0, this.mSpinner);
                    bottom = top + footerView.getMeasuredHeight();
                } else if(this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale) {
                    bottom = top + Math.max(Math.max(0, this.mSpinner) - lp.bottomMargin, 0);
                }
            }

            footerView.layout(left, top, right, bottom);
        }

        if(this.mRefreshFooter != null) {
            isInEditMode = this.isInEditMode() && this.mEnablePreviewInEditMode;
            footerView = this.mRefreshFooter.getView();
            lp = (SmartRefreshLayout.LayoutParams)footerView.getLayoutParams();
            SpinnerStyle style = this.mRefreshFooter.getSpinnerStyle();
            int left = lp.leftMargin;
            int top = lp.topMargin + getMeasuredHeight() - lp.bottomMargin;
            if(!isInEditMode && style != SpinnerStyle.FixedFront && style != SpinnerStyle.FixedBehind) {
                if(style == SpinnerStyle.Scale || style == SpinnerStyle.Translate) {
                    top -= Math.max(Math.max(-this.mSpinner, 0) - lp.topMargin, 0);
                }
            } else {
                top -= this.mFooterHeight;
            }

            int right = left + footerView.getMeasuredWidth();
            int bottom = top + footerView.getMeasuredHeight();
            footerView.layout(top, top, right, bottom);
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.moveSpinner(0, false);
        this.notifyStateChanged(RefreshState.None);
        this.handler.removeCallbacksAndMessages((Object)null);
        this.handler = null;
        this.mKernel = null;
        this.mManualLoadmore = true;
        this.mManualNestedScrolling = true;
    }

    protected void dispatchDraw(Canvas canvas) {
        boolean isInEditMode = this.mEnablePreviewInEditMode && this.isInEditMode();
        if(this.mHeaderBackgroundColor == 0 || this.mSpinner <= 0 && !isInEditMode) {
            if(this.mFooterBackgroundColor != 0 && (this.mSpinner < 0 || isInEditMode)) {
                int height = this.getHeight();
                this.mPaint.setColor(this.mFooterBackgroundColor);
                canvas.drawRect(0.0F, (float)(height - (isInEditMode?this.mFooterHeight:-this.mSpinner)), (float)this.getWidth(), (float)height, this.mPaint);
            }
        } else {
            this.mPaint.setColor(this.mHeaderBackgroundColor);
            canvas.drawRect(0.0F, 0.0F, (float)this.getWidth(), isInEditMode?(float)this.mHeaderHeight:(float)this.mSpinner, this.mPaint);
        }

        super.dispatchDraw(canvas);
    }

    public void computeScroll() {
        int lastCurY = this.mScroller.getCurrY();
        if(this.mScroller.computeScrollOffset()) {
            int finay = this.mScroller.getFinalY();
            if(finay > 0 && this.mRefreshContent.canLoadmore() || finay < 0 && this.mRefreshContent.canRefresh()) {
                int velocity;
                if(VERSION.SDK_INT >= 14) {
                    velocity = (int)this.mScroller.getCurrVelocity();
                } else {
                    velocity = (finay - this.mScroller.getCurrY()) / (this.mScroller.getDuration() - this.mScroller.timePassed());
                }

                long lastTime = AnimationUtils.currentAnimationTimeMillis() - (long)(1000 * Math.abs(this.mScroller.getCurrY() - lastCurY) / velocity);
                if(finay > 0) {
                    if(this.mEnableLoadmore) {
                        if(this.mEnableAutoLoadmore && !this.mLoadmoreFinished) {
                            this.autoLoadmore(0, 1.0F);
                        } else if(this.mEnableOverScrollBounce) {
                            this.animSpinnerBounce(-((int)((double)this.mFooterHeight * Math.pow(1.0D * (double)velocity / (double)this.mMaximumVelocity, 0.5D))));
                        }
                    }
                } else if(this.mEnableRefresh && this.mEnableOverScrollBounce) {
                    this.animSpinnerBounce((int)((double)this.mHeaderHeight * Math.pow(1.0D * (double)velocity / (double)this.mMaximumVelocity, 0.5D)));
                }

                this.mScroller.forceFinished(true);
            } else {
                this.invalidate();
            }
        }

    }

    public boolean dispatchTouchEvent(MotionEvent e) {
        int action = MotionEventCompat.getActionMasked(e);
        boolean pointerUp = action == 6;
        int skipIndex = pointerUp?e.getActionIndex():-1;
        float sumX = 0.0F;
        float sumY = 0.0F;
        int count = e.getPointerCount();

        int i;
        for(i = 0; i < count; ++i) {
            if(skipIndex != i) {
                sumX += e.getX(i);
                sumY += e.getY(i);
            }
        }

        i = pointerUp?count - 1:count;
        float touchX = sumX / (float)i;
        float touchY = sumY / (float)i;
        if((action == 6 || action == 5) && this.mIsBeingDragged) {
            this.mTouchY += touchY - this.mLastTouchY;
        }

        this.mLastTouchX = touchX;
        this.mLastTouchY = touchY;
        if(this.mRefreshContent != null) {
            switch(action) {
                case 0:
                    this.mRefreshContent.onActionDown(e);
                    break;
                case 1:
                case 3:
                    this.mRefreshContent.onActionUpOrCancel();
                case 2:
            }
        }

        if(this.reboundAnimator != null && !this.interceptAnimator(action) || this.mState == RefreshState.Loading && this.mDisableContentWhenLoading || this.mState == RefreshState.Refreshing && this.mDisableContentWhenRefresh) {
            return false;
        } else if(this.mNestedScrollInProgress) {
            int totalUnconsumed = this.mTotalUnconsumed;
            boolean ret = this.superDispatchTouchEvent(e);
            if(action == 2 && totalUnconsumed == this.mTotalUnconsumed) {
                int offsetX = (int)this.mLastTouchX;
                int offsetMax = this.getWidth();
                float percentX = this.mLastTouchX / (float)offsetMax;
                if(this.mSpinner > 0 && this.mRefreshHeader != null && this.mRefreshHeader.isSupportHorizontalDrag()) {
                    this.mRefreshHeader.onHorizontalDrag(percentX, offsetX, offsetMax);
                } else if(this.mSpinner < 0 && this.mRefreshFooter != null && this.mRefreshFooter.isSupportHorizontalDrag()) {
                    this.mRefreshFooter.onHorizontalDrag(percentX, offsetX, offsetMax);
                }
            }

            return ret;
        } else if(!this.isEnabled() || !this.mEnableRefresh && !this.mEnableLoadmore || this.mHeaderNeedTouchEventWhenRefreshing && (this.mState == RefreshState.Refreshing || this.mState == RefreshState.RefreshFinish) || this.mFooterNeedTouchEventWhenRefreshing && (this.mState == RefreshState.Loading || this.mState == RefreshState.LoadFinish)) {
            return this.superDispatchTouchEvent(e);
        } else {
            switch(action) {
                case 0:
                    this.mTouchX = touchX;
                    this.mTouchY = touchY;
                    this.mLastTouchY = touchY;
                    this.mLastSpinner = 0;
                    this.mTouchSpinner = this.mSpinner;
                    this.mIsBeingDragged = false;
                    this.superDispatchTouchEvent(e);
                    return true;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    if(this.mFalsifyEvent != null) {
                        this.mFalsifyEvent = null;
                        long time = e.getEventTime();
                        MotionEvent ec = MotionEvent.obtain(time, time, this.mSpinner == 0?1:3, this.mTouchX, touchY, 0);
                        this.superDispatchTouchEvent(ec);
                    }

                    if(this.overSpinner()) {
                        return true;
                    }
                    break;
                case 2:
                    float dx = touchX - this.mTouchX;
                    float dy = touchY - this.mTouchY;
                    this.mLastTouchY = touchY;
                    if(!this.mIsBeingDragged) {
                        if(Math.abs(dy) < (float)this.mTouchSlop || Math.abs(dx) >= Math.abs(dy)) {
                            return this.superDispatchTouchEvent(e);
                        }

                        if(dy <= 0.0F || this.mSpinner >= 0 && (!this.mEnableRefresh || !this.mRefreshContent.canRefresh())) {
                            if(dy >= 0.0F || this.mSpinner <= 0 && (!this.mEnableLoadmore || !this.mRefreshContent.canLoadmore())) {
                                return this.superDispatchTouchEvent(e);
                            }

                            if(this.mSpinner > 0) {
                                this.setStatePullDownToRefresh();
                            } else {
                                this.setStatePullUpToLoad();
                            }

                            this.mIsBeingDragged = true;
                            this.mTouchY = touchY + (float)this.mTouchSlop;
                            dy = touchY - this.mTouchY;
                            e.setAction(3);
                            this.superDispatchTouchEvent(e);
                        } else {
                            if(this.mSpinner < 0) {
                                this.setStatePullUpToLoad();
                            } else {
                                this.setStatePullDownToRefresh();
                            }

                            this.mIsBeingDragged = true;
                            this.mTouchY = touchY - (float)this.mTouchSlop;
                            dy = touchY - this.mTouchY;
                            e.setAction(3);
                            this.superDispatchTouchEvent(e);
                        }
                    }

                    if(this.mIsBeingDragged) {
                        float spinner = dy + (float)this.mTouchSpinner;
                        if(this.mRefreshContent != null && this.getViceState().isHeader() && (spinner < 0.0F || this.mLastSpinner < 0) || this.getViceState().isFooter() && (spinner > 0.0F || this.mLastSpinner > 0)) {
                            long time = e.getEventTime();
                            if(this.mFalsifyEvent == null) {
                                this.mFalsifyEvent = MotionEvent.obtain(time, time, 0, this.mTouchX + dx, this.mTouchY, 0);
                                this.superDispatchTouchEvent(this.mFalsifyEvent);
                            }

                            MotionEvent em = MotionEvent.obtain(time, time, 2, this.mTouchX + dx, this.mTouchY + spinner, 0);
                            this.superDispatchTouchEvent(em);
                            if(this.getViceState().isHeader() && spinner < 0.0F || this.getViceState().isFooter() && spinner > 0.0F) {
                                this.mLastSpinner = (int)spinner;
                                if(this.mSpinner != 0) {
                                    this.moveSpinnerInfinitely(0.0F);
                                }

                                return true;
                            }

                            this.mLastSpinner = (int)spinner;
                            this.mFalsifyEvent = null;
                            MotionEvent ec = MotionEvent.obtain(time, time, 3, this.mTouchX, this.mTouchY + spinner, 0);
                            this.superDispatchTouchEvent(ec);
                        }

                        if(this.getViceState().isDraging()) {
                            this.moveSpinnerInfinitely(spinner);
                            return true;
                        }
                    }
            }

            return this.superDispatchTouchEvent(e);
        }
    }

    protected boolean superDispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case 0:
                if(this.mVelocityTracker == null) {
                    this.mVelocityTracker = VelocityTracker.obtain();
                } else {
                    this.mVelocityTracker.clear();
                }

                this.mVelocityTracker.addMovement(ev);
                this.mScroller.forceFinished(true);
                break;
            case 1:
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                float velocity = -this.mVelocityTracker.getYVelocity();
                if(Math.abs(velocity) > (float)this.mMinimumVelocity && this.mSpinner == 0 && this.mTouchSpinner == 0) {
                    this.mScroller.fling(0, this.getScrollY(), 0, (int)velocity, 0, 0, -2147483647, 2147483647);
                    this.mScroller.computeScrollOffset();
                    this.invalidate();
                }
                break;
            case 2:
                if(this.mVelocityTracker == null) {
                    this.mVelocityTracker = VelocityTracker.obtain();
                }

                this.mVelocityTracker.addMovement(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    protected boolean interceptAnimator(int action) {
        if(this.reboundAnimator != null && action == 0) {
            if(this.mState != RefreshState.LoadFinish && this.mState != RefreshState.RefreshFinish) {
                if(this.mState == RefreshState.PullDownCanceled) {
                    this.setStatePullDownToRefresh();
                } else if(this.mState == RefreshState.PullUpCanceled) {
                    this.setStatePullUpToLoad();
                }

                this.reboundAnimator.cancel();
                this.reboundAnimator = null;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
        View target = this.mRefreshContent.getScrollableView();
        if((VERSION.SDK_INT >= 21 || !(target instanceof AbsListView)) && (target == null || ViewCompat.isNestedScrollingEnabled(target))) {
            super.requestDisallowInterceptTouchEvent(b);
        }

    }

    protected void notifyStateChanged(RefreshState state) {
        RefreshState oldState = this.mState;
        if(oldState != state) {
            this.mState = state;
            this.mViceState = state;
            if(this.mRefreshFooter != null) {
                this.mRefreshFooter.onStateChanged(this, oldState, state);
            }

            if(this.mRefreshHeader != null) {
                this.mRefreshHeader.onStateChanged(this, oldState, state);
            }

            if(this.mOnMultiPurposeListener != null) {
                this.mOnMultiPurposeListener.onStateChanged(this, oldState, state);
            }
        }

    }

    protected void setStatePullUpToLoad() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.PullToUpLoad);
        } else {
            this.setViceState(RefreshState.PullToUpLoad);
        }

    }

    protected void setStateReleaseToLoad() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.ReleaseToLoad);
        } else {
            this.setViceState(RefreshState.ReleaseToLoad);
        }

    }

    protected void setStateReleaseToRefresh() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.ReleaseToRefresh);
        } else {
            this.setViceState(RefreshState.ReleaseToRefresh);
        }

    }

    protected void setStatePullDownToRefresh() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.PullDownToRefresh);
        } else {
            this.setViceState(RefreshState.PullDownToRefresh);
        }

    }

    protected void setStatePullDownCanceled() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.PullDownCanceled);
            this.resetStatus();
        } else {
            this.setViceState(RefreshState.PullDownCanceled);
        }

    }

    protected void setStatePullUpCanceled() {
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            this.notifyStateChanged(RefreshState.PullUpCanceled);
            this.resetStatus();
        } else {
            this.setViceState(RefreshState.PullUpCanceled);
        }

    }

    protected void setStateLodingFinish() {
        this.notifyStateChanged(RefreshState.LoadFinish);
    }

    protected void setStateRefresingFinish() {
        this.notifyStateChanged(RefreshState.RefreshFinish);
    }

    protected void setStateLoding() {
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.mLastLoadingTime = System.currentTimeMillis();
                SmartRefreshLayout.this.notifyStateChanged(RefreshState.Loading);
                if(SmartRefreshLayout.this.mRefreshFooter != null) {
                    SmartRefreshLayout.this.mRefreshFooter.onStartAnimator(SmartRefreshLayout.this, SmartRefreshLayout.this.mFooterHeight, SmartRefreshLayout.this.mFooterExtendHeight);
                }

                if(SmartRefreshLayout.this.mLoadmoreListener != null) {
                    SmartRefreshLayout.this.mLoadmoreListener.onLoadmore(SmartRefreshLayout.this);
                }

                if(SmartRefreshLayout.this.mOnMultiPurposeListener != null) {
                    SmartRefreshLayout.this.mOnMultiPurposeListener.onLoadmore(SmartRefreshLayout.this);
                    SmartRefreshLayout.this.mOnMultiPurposeListener.onFooterStartAnimator(SmartRefreshLayout.this.mRefreshFooter, SmartRefreshLayout.this.mFooterHeight, SmartRefreshLayout.this.mFooterExtendHeight);
                }

            }
        };
        this.notifyStateChanged(RefreshState.LoadReleased);
        ValueAnimator animator = this.animSpinner(-this.mFooterHeight);
        if(this.mRefreshFooter != null) {
            this.mRefreshFooter.onLoadmoreReleased(this, this.mFooterHeight, this.mFooterExtendHeight);
        }

        if(animator != null && animator == this.reboundAnimator) {
            animator.addListener(listener);
        } else {
            listener.onAnimationEnd((Animator)null);
        }

    }

    protected void setStateRefresing() {
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SmartRefreshLayout.this.mLastRefreshingTime = System.currentTimeMillis();
                SmartRefreshLayout.this.notifyStateChanged(RefreshState.Refreshing);
                if(SmartRefreshLayout.this.mRefreshListener != null) {
                    SmartRefreshLayout.this.mRefreshListener.onRefresh(SmartRefreshLayout.this);
                }

                if(SmartRefreshLayout.this.mRefreshHeader != null) {
                    SmartRefreshLayout.this.mRefreshHeader.onStartAnimator(SmartRefreshLayout.this, SmartRefreshLayout.this.mHeaderHeight, SmartRefreshLayout.this.mHeaderExtendHeight);
                }

                if(SmartRefreshLayout.this.mOnMultiPurposeListener != null) {
                    SmartRefreshLayout.this.mOnMultiPurposeListener.onRefresh(SmartRefreshLayout.this);
                    SmartRefreshLayout.this.mOnMultiPurposeListener.onHeaderStartAnimator(SmartRefreshLayout.this.mRefreshHeader, SmartRefreshLayout.this.mHeaderHeight, SmartRefreshLayout.this.mHeaderExtendHeight);
                }

            }
        };
        this.notifyStateChanged(RefreshState.RefreshReleased);
        ValueAnimator animator = this.animSpinner(this.mHeaderHeight);
        if(this.mRefreshHeader != null) {
            this.mRefreshHeader.onRefreshReleased(this, this.mHeaderHeight, this.mHeaderExtendHeight);
        }

        if(animator != null && animator == this.reboundAnimator) {
            animator.addListener(listener);
        } else {
            listener.onAnimationEnd((Animator)null);
        }

    }

    protected void resetStatus() {
        if(this.mState != RefreshState.None && this.mSpinner == 0) {
            this.notifyStateChanged(RefreshState.None);
        }

        if(this.mSpinner != 0) {
            this.animSpinner(0);
        }

    }

    protected RefreshState getViceState() {
        return this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading?this.mState:this.mViceState;
    }

    protected void setViceState(RefreshState state) {
        if((this.mState == RefreshState.Refreshing || this.mState == RefreshState.Loading) && this.mViceState != state) {
            this.mViceState = state;
        }

    }

    protected ValueAnimator animSpinner(int endSpinner) {
        return this.animSpinner(endSpinner, 0);
    }

    protected ValueAnimator animSpinner(int endSpinner, int startDelay) {
        return this.animSpinner(endSpinner, startDelay, this.mReboundInterpolator);
    }

    protected ValueAnimator animSpinner(int endSpinner, int startDelay, Interpolator interpolator) {
        if(this.mSpinner != endSpinner) {
            if(this.reboundAnimator != null) {
                this.reboundAnimator.cancel();
            }

            this.reboundAnimator = ValueAnimator.ofInt(new int[]{this.mSpinner, endSpinner});
            this.reboundAnimator.setDuration((long)this.mReboundDuration);
            this.reboundAnimator.setInterpolator(interpolator);
            this.reboundAnimator.addUpdateListener(this.reboundUpdateListener);
            this.reboundAnimator.addListener(this.reboundAnimatorEndListener);
            this.reboundAnimator.setStartDelay((long)startDelay);
            this.reboundAnimator.start();
        }

        return this.reboundAnimator;
    }
    int duration = 0;
    protected ValueAnimator animSpinnerBounce(int bounceSpinner) {
        if(this.reboundAnimator == null) {

            this.mLastTouchX = (float)(this.getMeasuredWidth() / 2);
            if(this.mState == RefreshState.Refreshing && bounceSpinner > 0) {
                this.reboundAnimator = ValueAnimator.ofInt(new int[]{this.mSpinner, Math.min(2 * bounceSpinner, this.mHeaderHeight)});
                this.reboundAnimator.addListener(this.reboundAnimatorEndListener);
            } else if(this.mState == RefreshState.Loading && bounceSpinner < 0) {
                this.reboundAnimator = ValueAnimator.ofInt(new int[]{this.mSpinner, Math.max(2 * bounceSpinner, -this.mFooterHeight)});
                this.reboundAnimator.addListener(this.reboundAnimatorEndListener);
            } else if(this.mSpinner == 0 && this.mEnableOverScrollBounce) {
                if(bounceSpinner > 0) {
                    if(this.mState != RefreshState.Loading) {
                        this.setStatePullDownToRefresh();
                    }

                    duration = Math.max(150, bounceSpinner * 250 / this.mHeaderHeight);
                    this.reboundAnimator = ValueAnimator.ofInt(new int[]{0, Math.min(bounceSpinner, this.mHeaderHeight)});
                } else {
                    if(this.mState != RefreshState.Refreshing) {
                        this.setStatePullUpToLoad();
                    }

                    duration = Math.max(150, -bounceSpinner * 250 / this.mFooterHeight);
                    this.reboundAnimator = ValueAnimator.ofInt(new int[]{0, Math.max(bounceSpinner, -this.mFooterHeight)});
                }

                this.reboundAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        SmartRefreshLayout.this.reboundAnimator = ValueAnimator.ofInt(new int[]{SmartRefreshLayout.this.mSpinner, 0});
                        SmartRefreshLayout.this.reboundAnimator.setDuration((long)duration);
                        SmartRefreshLayout.this.reboundAnimator.setInterpolator(new DecelerateInterpolator());
                        SmartRefreshLayout.this.reboundAnimator.addUpdateListener(SmartRefreshLayout.this.reboundUpdateListener);
                        SmartRefreshLayout.this.reboundAnimator.addListener(SmartRefreshLayout.this.reboundAnimatorEndListener);
                        SmartRefreshLayout.this.reboundAnimator.start();
                    }
                });
            }

            if(this.reboundAnimator != null) {
                this.reboundAnimator.setDuration((long)duration);
                this.reboundAnimator.setInterpolator(new DecelerateInterpolator());
                this.reboundAnimator.addUpdateListener(this.reboundUpdateListener);
                this.reboundAnimator.start();
            }
        }

        return this.reboundAnimator;
    }

    protected boolean overSpinner() {
        if(this.mState == RefreshState.Loading) {
            if(this.mSpinner < -this.mFooterHeight) {
                this.mTotalUnconsumed = -this.mFooterHeight;
                this.animSpinner(-this.mFooterHeight);
            } else {
                if(this.mSpinner <= 0) {
                    return false;
                }

                this.mTotalUnconsumed = 0;
                this.animSpinner(0);
            }
        } else if(this.mState == RefreshState.Refreshing) {
            if(this.mSpinner > this.mHeaderHeight) {
                this.mTotalUnconsumed = this.mHeaderHeight;
                this.animSpinner(this.mHeaderHeight);
            } else {
                if(this.mSpinner >= 0) {
                    return false;
                }

                this.mTotalUnconsumed = 0;
                this.animSpinner(0);
            }
        } else if(this.mState != RefreshState.PullDownToRefresh && (!this.mEnablePureScrollMode || this.mState != RefreshState.ReleaseToRefresh)) {
            if(this.mState == RefreshState.PullToUpLoad || this.mEnablePureScrollMode && this.mState == RefreshState.ReleaseToLoad) {
                this.setStatePullUpCanceled();
            } else if(this.mState == RefreshState.ReleaseToRefresh) {
                this.setStateRefresing();
            } else if(this.mState == RefreshState.ReleaseToLoad) {
                this.setStateLoding();
            } else {
                if(this.mSpinner == 0) {
                    return false;
                }

                this.animSpinner(0);
            }
        } else {
            this.setStatePullDownCanceled();
        }

        return true;
    }

    protected void moveSpinnerInfinitely(float dy) {
        double M;
        double H;
        double x;
        double y;
        if(this.mState == RefreshState.Refreshing && dy >= 0.0F) {
            if(dy < (float)this.mHeaderHeight) {
                this.moveSpinner((int)dy, false);
            } else {
                M = (double)this.mHeaderExtendHeight;
                H = (double)(Math.max(this.mScreenHeightPixels * 4 / 3, this.getHeight()) - this.mHeaderHeight);
                x = (double)Math.max(0.0F, (dy - (float)this.mHeaderHeight) * this.mDragRate);
                y = Math.min(M * (1.0D - Math.pow(100.0D, -x / H)), x);
                this.moveSpinner((int)y + this.mHeaderHeight, false);
            }
        } else if(this.mState == RefreshState.Loading && dy < 0.0F) {
            if(dy > (float)(-this.mFooterHeight)) {
                this.moveSpinner((int)dy, false);
            } else {
                M = (double)this.mFooterExtendHeight;
                H = (double)(Math.max(this.mScreenHeightPixels * 4 / 3, this.getHeight()) - this.mFooterHeight);
                x = (double)(-Math.min(0.0F, (dy + (float)this.mHeaderHeight) * this.mDragRate));
                y = -Math.min(M * (1.0D - Math.pow(100.0D, -x / H)), x);
                this.moveSpinner((int)y - this.mFooterHeight, false);
            }
        } else if(dy >= 0.0F) {
            M = (double)(this.mHeaderExtendHeight + this.mHeaderHeight);
            H = (double)Math.max(this.mScreenHeightPixels / 2, this.getHeight());
            x = (double)Math.max(0.0F, dy * this.mDragRate);
            y = Math.min(M * (1.0D - Math.pow(100.0D, -x / H)), x);
            this.moveSpinner((int)y, false);
        } else {
            M = (double)(this.mFooterExtendHeight + this.mFooterHeight);
            H = (double)Math.max(this.mScreenHeightPixels / 2, this.getHeight());
            x = (double)(-Math.min(0.0F, dy * this.mDragRate));
            y = -Math.min(M * (1.0D - Math.pow(100.0D, -x / H)), x);
            this.moveSpinner((int)y, false);
        }

    }

    protected void moveSpinner(int spinner, boolean isAnimator) {
        if(this.mSpinner != spinner || this.mRefreshHeader != null && this.mRefreshHeader.isSupportHorizontalDrag() || this.mRefreshFooter != null && this.mRefreshFooter.isSupportHorizontalDrag()) {
            int oldSpinner = this.mSpinner;
            this.mSpinner = spinner;
            if(!isAnimator && this.getViceState().isDraging()) {
                if((float)this.mSpinner > (float)this.mHeaderHeight * this.mHeaderTriggerRate) {
                    this.setStateReleaseToRefresh();
                } else if((float)(-this.mSpinner) > (float)this.mFooterHeight * this.mFooterTriggerRate && !this.mLoadmoreFinished) {
                    this.setStateReleaseToLoad();
                } else if(this.mSpinner < 0 && !this.mLoadmoreFinished) {
                    this.setStatePullUpToLoad();
                } else if(this.mSpinner > 0) {
                    this.setStatePullDownToRefresh();
                }
            }

            if(this.mRefreshContent != null) {
                if(spinner > 0) {
                    if(this.mEnableHeaderTranslationContent || this.mRefreshHeader == null || this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                        this.mRefreshContent.moveSpinner(spinner);
                        if(this.mHeaderBackgroundColor != 0) {
                            this.invalidate();
                        }
                    }
                } else if(this.mEnableFooterTranslationContent || this.mRefreshFooter == null || this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    this.mRefreshContent.moveSpinner(spinner);
                    if(this.mHeaderBackgroundColor != 0) {
                        this.invalidate();
                    }
                }
            }

            int footerHeight;
            int extendHeight;
            float percent;
            int offsetX;
            int offsetMax;
            float percentX;
            if((spinner > 0 || oldSpinner > 0) && this.mRefreshHeader != null) {
                spinner = Math.max(spinner, 0);
                if((this.mEnableRefresh || this.mState == RefreshState.RefreshFinish && isAnimator) && oldSpinner != this.mSpinner && (this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale || this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Translate)) {
                    this.mIsSkipContentLayout = true;
                    this.mRefreshHeader.getView().requestLayout();
                }

                footerHeight = this.mHeaderHeight;
                extendHeight = this.mHeaderExtendHeight;
                percent = 1.0F * (float)spinner / (float)this.mHeaderHeight;
                if(isAnimator) {
                    this.mRefreshHeader.onReleasing(percent, spinner, footerHeight, extendHeight);
                    if(this.mOnMultiPurposeListener != null) {
                        this.mOnMultiPurposeListener.onHeaderReleasing(this.mRefreshHeader, percent, spinner, footerHeight, extendHeight);
                    }
                } else {
                    if(this.mRefreshHeader.isSupportHorizontalDrag()) {
                        offsetX = (int)this.mLastTouchX;
                        offsetMax = this.getWidth();
                        percentX = this.mLastTouchX / (float)offsetMax;
                        this.mRefreshHeader.onHorizontalDrag(percentX, offsetX, offsetMax);
                    }

                    this.mRefreshHeader.onPullingDown(percent, spinner, footerHeight, extendHeight);
                    if(this.mOnMultiPurposeListener != null) {
                        this.mOnMultiPurposeListener.onHeaderPulling(this.mRefreshHeader, percent, spinner, footerHeight, extendHeight);
                    }
                }
            }

            if((spinner < 0 || oldSpinner < 0) && this.mRefreshFooter != null) {
                spinner = Math.min(spinner, 0);
                if((this.mEnableLoadmore || this.mState == RefreshState.LoadFinish && isAnimator) && oldSpinner != this.mSpinner && (this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale || this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Translate)) {
                    this.mIsSkipContentLayout = true;
                    this.mRefreshFooter.getView().requestLayout();
                }

                int offset = -spinner;
                footerHeight = this.mFooterHeight;
                extendHeight = this.mFooterExtendHeight;
                percent = (float)(-spinner) * 1.0F / (float)this.mFooterHeight;
                if(isAnimator) {
                    this.mRefreshFooter.onPullReleasing(percent, offset, footerHeight, extendHeight);
                    if(this.mOnMultiPurposeListener != null) {
                        this.mOnMultiPurposeListener.onFooterReleasing(this.mRefreshFooter, percent, offset, footerHeight, extendHeight);
                    }
                } else {
                    if(this.mRefreshFooter.isSupportHorizontalDrag()) {
                        offsetX = (int)this.mLastTouchX;
                        offsetMax = this.getWidth();
                        percentX = this.mLastTouchX / (float)offsetMax;
                        this.mRefreshFooter.onHorizontalDrag(percentX, offsetX, offsetMax);
                    }

                    this.mRefreshFooter.onPullingUp(percent, offset, footerHeight, extendHeight);
                    if(this.mOnMultiPurposeListener != null) {
                        this.mOnMultiPurposeListener.onFooterPulling(this.mRefreshFooter, percent, offset, footerHeight, extendHeight);
                    }
                }
            }

        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof SmartRefreshLayout.LayoutParams;
    }

    protected SmartRefreshLayout.LayoutParams generateDefaultLayoutParams() {
        return new SmartRefreshLayout.LayoutParams(-1, -1);
    }

    protected SmartRefreshLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new SmartRefreshLayout.LayoutParams(p);
    }

    public SmartRefreshLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SmartRefreshLayout.LayoutParams(this.getContext(), attrs);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean accepted = this.isEnabled() && this.isNestedScrollingEnabled() && (nestedScrollAxes & 2) != 0;
        accepted = accepted && (this.mEnableRefresh || this.mEnableLoadmore);
        return accepted;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        this.startNestedScroll(axes & 2);
        this.mTotalUnconsumed = 0;
        this.mTouchSpinner = this.mSpinner;
        this.mNestedScrollInProgress = true;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int[] parentConsumed;
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            if(this.mEnableRefresh && dy > 0 && this.mTotalUnconsumed > 0) {
                if(dy > this.mTotalUnconsumed) {
                    consumed[1] = dy - this.mTotalUnconsumed;
                    this.mTotalUnconsumed = 0;
                } else {
                    this.mTotalUnconsumed -= dy;
                    consumed[1] = dy;
                }

                this.moveSpinnerInfinitely((float)this.mTotalUnconsumed);
            } else if(this.mEnableLoadmore && dy < 0 && this.mTotalUnconsumed < 0) {
                if(dy < this.mTotalUnconsumed) {
                    consumed[1] = dy - this.mTotalUnconsumed;
                    this.mTotalUnconsumed = 0;
                } else {
                    this.mTotalUnconsumed -= dy;
                    consumed[1] = dy;
                }

                this.moveSpinnerInfinitely((float)this.mTotalUnconsumed);
            }

            parentConsumed = this.mParentScrollConsumed;
            if(this.dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, (int[])null)) {
                consumed[0] += parentConsumed[0];
                consumed[1] += parentConsumed[1];
            }
        } else {
            parentConsumed = this.mParentScrollConsumed;
            if(this.dispatchNestedPreScroll(dx, dy, parentConsumed, (int[])null)) {
                dy -= parentConsumed[1];
            }

            if(this.mState == RefreshState.Refreshing && (dy * this.mTotalUnconsumed > 0 || this.mTouchSpinner > 0)) {
                consumed[1] = 0;
                if(Math.abs(dy) > Math.abs(this.mTotalUnconsumed)) {
                    consumed[1] += this.mTotalUnconsumed;
                    this.mTotalUnconsumed = 0;
                    dy -= this.mTotalUnconsumed;
                    if(this.mTouchSpinner <= 0) {
                        this.moveSpinnerInfinitely(0.0F);
                    }
                } else {
                    this.mTotalUnconsumed -= dy;
                    consumed[1] += dy;
                    dy = 0;
                    this.moveSpinnerInfinitely((float)(this.mTotalUnconsumed + this.mTouchSpinner));
                }

                if(dy > 0 && this.mTouchSpinner > 0) {
                    if(dy > this.mTouchSpinner) {
                        consumed[1] += this.mTouchSpinner;
                        this.mTouchSpinner = 0;
                    } else {
                        this.mTouchSpinner -= dy;
                        consumed[1] += dy;
                    }

                    this.moveSpinnerInfinitely((float)this.mTouchSpinner);
                }
            } else if(this.mState == RefreshState.Loading && (dy * this.mTotalUnconsumed > 0 || this.mTouchSpinner < 0)) {
                consumed[1] = 0;
                if(Math.abs(dy) > Math.abs(this.mTotalUnconsumed)) {
                    consumed[1] += this.mTotalUnconsumed;
                    this.mTotalUnconsumed = 0;
                    dy -= this.mTotalUnconsumed;
                    if(this.mTouchSpinner >= 0) {
                        this.moveSpinnerInfinitely(0.0F);
                    }
                } else {
                    this.mTotalUnconsumed -= dy;
                    consumed[1] += dy;
                    dy = 0;
                    this.moveSpinnerInfinitely((float)(this.mTotalUnconsumed + this.mTouchSpinner));
                }

                if(dy < 0 && this.mTouchSpinner < 0) {
                    if(dy < this.mTouchSpinner) {
                        consumed[1] += this.mTouchSpinner;
                        this.mTouchSpinner = 0;
                    } else {
                        this.mTouchSpinner -= dy;
                        consumed[1] += dy;
                    }

                    this.moveSpinnerInfinitely((float)this.mTouchSpinner);
                }
            }
        }

    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public void onStopNestedScroll(View target) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(target);
        this.mNestedScrollInProgress = false;
        this.mTotalUnconsumed = 0;
        this.overSpinner();
        this.stopNestedScroll();
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, this.mParentOffsetInWindow);
        int dy = dyUnconsumed + this.mParentOffsetInWindow[1];
        if(this.mState != RefreshState.Refreshing && this.mState != RefreshState.Loading) {
            if(!this.mEnableRefresh || dy >= 0 || this.mRefreshContent != null && !this.mRefreshContent.canRefresh()) {
                if(this.mEnableLoadmore && dy > 0 && (this.mRefreshContent == null || this.mRefreshContent.canLoadmore())) {
                    if(this.mState == RefreshState.None && !this.mLoadmoreFinished) {
                        this.setStatePullUpToLoad();
                    }

                    this.mTotalUnconsumed -= Math.abs(dy);
                    this.moveSpinnerInfinitely((float)this.mTotalUnconsumed);
                }
            } else {
                if(this.mState == RefreshState.None) {
                    this.setStatePullDownToRefresh();
                }

                this.mTotalUnconsumed += Math.abs(dy);
                this.moveSpinnerInfinitely((float)this.mTotalUnconsumed);
            }
        } else if(!this.mEnableRefresh || dy >= 0 || this.mRefreshContent != null && !this.mRefreshContent.canRefresh()) {
            if(this.mEnableLoadmore && dy > 0 && (this.mRefreshContent == null || this.mRefreshContent.canLoadmore())) {
                this.mTotalUnconsumed -= Math.abs(dy);
                this.moveSpinnerInfinitely((float)(this.mTotalUnconsumed + this.mTouchSpinner));
            }
        } else {
            this.mTotalUnconsumed += Math.abs(dy);
            this.moveSpinnerInfinitely((float)(this.mTotalUnconsumed + this.mTouchSpinner));
        }

    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if(this.mState == RefreshState.Refreshing && this.mSpinner != 0 || this.mState == RefreshState.Loading && this.mSpinner != 0) {
            this.animSpinner(0);
        }

        return this.reboundAnimator != null || this.mState == RefreshState.ReleaseToRefresh || this.mState == RefreshState.ReleaseToLoad || this.mState == RefreshState.PullDownToRefresh && this.mSpinner > 0 || this.mState == RefreshState.PullToUpLoad && this.mSpinner > 0 || this.dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return this.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        this.mManualNestedScrolling = true;
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return this.mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public SmartRefreshLayout setFooterHeight(float heightDp) {
        return this.setFooterHeightPx(DensityUtil.dp2px(heightDp));
    }

    public SmartRefreshLayout setFooterHeightPx(int heightPx) {
        if(this.mFooterHeightStatus.canReplaceWith(DimensionStatus.CodeExact)) {
            this.mFooterHeight = heightPx;
            this.mFooterExtendHeight = (int)Math.max((float)heightPx * (this.mFooterMaxDragRate - 1.0F), 0.0F);
            this.mFooterHeightStatus = DimensionStatus.CodeExactUnNotify;
            if(this.mRefreshFooter != null) {
                this.mRefreshFooter.getView().requestLayout();
            }
        }

        return this;
    }

    public SmartRefreshLayout setHeaderHeight(float heightDp) {
        return this.setHeaderHeightPx(DensityUtil.dp2px(heightDp));
    }

    public SmartRefreshLayout setHeaderHeightPx(int heightPx) {
        if(this.mHeaderHeightStatus.canReplaceWith(DimensionStatus.CodeExact)) {
            this.mHeaderHeight = heightPx;
            this.mHeaderExtendHeight = (int)Math.max((float)heightPx * (this.mHeaderMaxDragRate - 1.0F), 0.0F);
            this.mHeaderHeightStatus = DimensionStatus.CodeExactUnNotify;
            if(this.mRefreshHeader != null) {
                this.mRefreshHeader.getView().requestLayout();
            }
        }

        return this;
    }

    public SmartRefreshLayout setDragRate(float rate) {
        this.mDragRate = rate;
        return this;
    }

    public SmartRefreshLayout setHeaderMaxDragRate(float rate) {
        this.mHeaderMaxDragRate = rate;
        this.mHeaderExtendHeight = (int)Math.max((float)this.mHeaderHeight * (this.mHeaderMaxDragRate - 1.0F), 0.0F);
        if(this.mRefreshHeader != null && this.mKernel != null) {
            this.mRefreshHeader.onInitialized(this.mKernel, this.mHeaderHeight, this.mHeaderExtendHeight);
        } else {
            this.mHeaderHeightStatus = this.mHeaderHeightStatus.unNotify();
        }

        return this;
    }

    public SmartRefreshLayout setFooterMaxDragRate(float rate) {
        this.mFooterMaxDragRate = rate;
        this.mFooterExtendHeight = (int)Math.max((float)this.mFooterHeight * (this.mFooterMaxDragRate - 1.0F), 0.0F);
        if(this.mRefreshFooter != null && this.mKernel != null) {
            this.mRefreshFooter.onInitialized(this.mKernel, this.mFooterHeight, this.mFooterExtendHeight);
        } else {
            this.mFooterHeightStatus = this.mFooterHeightStatus.unNotify();
        }

        return this;
    }

    public SmartRefreshLayout setHeaderTriggerRate(float rate) {
        this.mHeaderTriggerRate = rate;
        return this;
    }

    public SmartRefreshLayout setFooterTriggerRate(float rate) {
        this.mFooterTriggerRate = rate;
        return this;
    }

    public SmartRefreshLayout setReboundInterpolator(Interpolator interpolator) {
        this.mReboundInterpolator = interpolator;
        return this;
    }

    public SmartRefreshLayout setReboundDuration(int duration) {
        this.mReboundDuration = duration;
        return this;
    }

    public SmartRefreshLayout setEnableLoadmore(boolean enable) {
        this.mManualLoadmore = true;
        this.mEnableLoadmore = enable;
        return this;
    }

    public SmartRefreshLayout setEnableRefresh(boolean enable) {
        this.mEnableRefresh = enable;
        return this;
    }

    public SmartRefreshLayout setEnableHeaderTranslationContent(boolean enable) {
        this.mEnableHeaderTranslationContent = enable;
        return this;
    }

    public SmartRefreshLayout setEnableFooterTranslationContent(boolean enable) {
        this.mEnableFooterTranslationContent = enable;
        return this;
    }

    public SmartRefreshLayout setDisableContentWhenRefresh(boolean disable) {
        this.mDisableContentWhenRefresh = disable;
        return this;
    }

    public SmartRefreshLayout setDisableContentWhenLoading(boolean disable) {
        this.mDisableContentWhenLoading = disable;
        return this;
    }

    public SmartRefreshLayout setEnableAutoLoadmore(boolean enable) {
        this.mEnableAutoLoadmore = enable;
        return this;
    }

    public SmartRefreshLayout setEnableOverScrollBounce(boolean enable) {
        this.mEnableOverScrollBounce = enable;
        return this;
    }

    public SmartRefreshLayout setEnablePureScrollMode(boolean enable) {
        this.mEnablePureScrollMode = enable;
        if(this.mRefreshContent != null) {
            this.mRefreshContent.setEnableLoadmoreWhenContentNotFull(enable || this.mEnableLoadmoreWhenContentNotFull);
        }

        return this;
    }

    public SmartRefreshLayout setEnableScrollContentWhenLoaded(boolean enable) {
        this.mEnableScrollContentWhenLoaded = enable;
        return this;
    }

    public SmartRefreshLayout setEnableLoadmoreWhenContentNotFull(boolean enable) {
        this.mEnableLoadmoreWhenContentNotFull = enable;
        if(this.mRefreshContent != null) {
            this.mRefreshContent.setEnableLoadmoreWhenContentNotFull(enable || this.mEnablePureScrollMode);
        }

        return this;
    }

    public RefreshLayout setEnableNestedScroll(boolean enabled) {
        this.setNestedScrollingEnabled(enabled);
        return this;
    }

    public SmartRefreshLayout setRefreshHeader(RefreshHeader header) {
        return this.setRefreshHeader(header, -1, -2);
    }

    public SmartRefreshLayout setRefreshHeader(RefreshHeader header, int width, int height) {
        if(header != null) {
            if(this.mRefreshHeader != null) {
                this.removeView(this.mRefreshHeader.getView());
            }

            this.mRefreshHeader = header;
            this.mHeaderHeightStatus = this.mHeaderHeightStatus.unNotify();
            if(header.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                this.addView(this.mRefreshHeader.getView(), 0, new SmartRefreshLayout.LayoutParams(width, height));
            } else {
                this.addView(this.mRefreshHeader.getView(), width, height);
            }
        }

        return this;
    }

    public SmartRefreshLayout setRefreshFooter(RefreshFooter footer) {
        return this.setRefreshFooter(footer, -1, -2);
    }

    public SmartRefreshLayout setRefreshFooter(RefreshFooter footer, int width, int height) {
        if(footer != null) {
            if(this.mRefreshFooter != null) {
                this.removeView(this.mRefreshFooter.getView());
            }

            this.mRefreshFooter = footer;
            this.mFooterHeightStatus = this.mFooterHeightStatus.unNotify();
            this.mEnableLoadmore = !this.mManualLoadmore || this.mEnableLoadmore;
            if(this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                this.addView(this.mRefreshFooter.getView(), 0, new SmartRefreshLayout.LayoutParams(width, height));
            } else {
                this.addView(this.mRefreshFooter.getView(), width, height);
            }
        }

        return this;
    }

    public RefreshLayout setRefreshContent(View content) {
        return this.setRefreshContent(content, -1, -1);
    }

    public RefreshLayout setRefreshContent(View content, int width, int height) {
        if(content != null) {
            if(this.mRefreshContent != null) {
                this.removeView(this.mRefreshContent.getView());
            }

            this.addView(content, 0, new SmartRefreshLayout.LayoutParams(width, height));
            if(this.mRefreshHeader != null && this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                this.bringChildToFront(content);
                if(this.mRefreshFooter != null && this.mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                    this.bringChildToFront(this.mRefreshFooter.getView());
                }
            } else if(this.mRefreshFooter != null && this.mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                this.bringChildToFront(content);
                if(this.mRefreshHeader != null && this.mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    this.bringChildToFront(this.mRefreshHeader.getView());
                }
            }

            this.mRefreshContent = new RefreshContentWrapper(content);
            if(this.mKernel != null) {
                View fixedHeaderView = this.mFixedHeaderViewId > 0?this.findViewById(this.mFixedHeaderViewId):null;
                View fixedFooterView = this.mFixedFooterViewId > 0?this.findViewById(this.mFixedFooterViewId):null;
                this.mRefreshContent.setScrollBoundaryDecider(this.mScrollBoundaryDecider);
                this.mRefreshContent.setEnableLoadmoreWhenContentNotFull(this.mEnableLoadmoreWhenContentNotFull || this.mEnablePureScrollMode);
                this.mRefreshContent.setupComponent(this.mKernel, fixedHeaderView, fixedFooterView);
            }
        }

        return this;
    }

    @Nullable
    public RefreshFooter getRefreshFooter() {
        return this.mRefreshFooter;
    }

    @Nullable
    public RefreshHeader getRefreshHeader() {
        return this.mRefreshHeader;
    }

    public RefreshState getState() {
        return this.mState;
    }

    public SmartRefreshLayout getLayout() {
        return this;
    }

    public SmartRefreshLayout setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
        return this;
    }

    public SmartRefreshLayout setOnLoadmoreListener(OnLoadmoreListener listener) {
        this.mLoadmoreListener = listener;
        this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore && listener != null;
        return this;
    }

    public SmartRefreshLayout setOnRefreshLoadmoreListener(OnRefreshLoadmoreListener listener) {
        this.mRefreshListener = listener;
        this.mLoadmoreListener = listener;
        this.mEnableLoadmore = this.mEnableLoadmore || !this.mManualLoadmore && listener != null;
        return this;
    }

    public SmartRefreshLayout setOnMultiPurposeListener(OnMultiPurposeListener listener) {
        this.mOnMultiPurposeListener = listener;
        return this;
    }

    public SmartRefreshLayout setPrimaryColorsId(@ColorRes int... primaryColorId) {
        int[] colors = new int[primaryColorId.length];

        for(int i = 0; i < primaryColorId.length; ++i) {
            colors[i] = ContextCompat.getColor(this.getContext(), primaryColorId[i]);
        }

        this.setPrimaryColors(colors);
        return this;
    }

    public SmartRefreshLayout setPrimaryColors(int... colors) {
        if(this.mRefreshHeader != null) {
            this.mRefreshHeader.setPrimaryColors(colors);
        }

        if(this.mRefreshFooter != null) {
            this.mRefreshFooter.setPrimaryColors(colors);
        }

        this.mPrimaryColors = colors;
        return this;
    }

    public RefreshLayout setScrollBoundaryDecider(ScrollBoundaryDecider boundary) {
        this.mScrollBoundaryDecider = boundary;
        if(this.mRefreshContent != null) {
            this.mRefreshContent.setScrollBoundaryDecider(boundary);
        }

        return this;
    }

    public SmartRefreshLayout setLoadmoreFinished(boolean finished) {
        this.mLoadmoreFinished = finished;
        if(this.mRefreshFooter != null) {
            this.mRefreshFooter.setLoadmoreFinished(finished);
        }

        return this;
    }

    public SmartRefreshLayout finishRefresh() {
        long passTime = System.currentTimeMillis() - this.mLastRefreshingTime;
        return this.finishRefresh(Math.max(0, 1000 - (int)passTime));
    }

    public SmartRefreshLayout finishLoadmore() {
        long passTime = System.currentTimeMillis() - this.mLastLoadingTime;
        return this.finishLoadmore(Math.max(0, 1000 - (int)passTime));
    }

    public SmartRefreshLayout finishRefresh(int delayed) {
        return this.finishRefresh(delayed, true);
    }

    public SmartRefreshLayout finishRefresh(boolean success) {
        long passTime = System.currentTimeMillis() - this.mLastRefreshingTime;
        return this.finishRefresh(Math.max(0, 1000 - (int)passTime), success);
    }

    public SmartRefreshLayout finishRefresh(int delayed, final boolean success) {
        this.postDelayed(new Runnable() {
            public void run() {
                if(SmartRefreshLayout.this.mState == RefreshState.Refreshing) {
                    if(SmartRefreshLayout.this.mRefreshHeader != null) {
                        int startDelay = SmartRefreshLayout.this.mRefreshHeader.onFinish(SmartRefreshLayout.this, success);
                        SmartRefreshLayout.this.notifyStateChanged(RefreshState.RefreshFinish);
                        if(SmartRefreshLayout.this.mOnMultiPurposeListener != null) {
                            SmartRefreshLayout.this.mOnMultiPurposeListener.onHeaderFinish(SmartRefreshLayout.this.mRefreshHeader, success);
                        }

                        if(startDelay < 2147483647) {
                            if(SmartRefreshLayout.this.mSpinner == 0) {
                                SmartRefreshLayout.this.resetStatus();
                            } else {
                                SmartRefreshLayout.this.animSpinner(0, startDelay);
                            }
                        }
                    } else {
                        SmartRefreshLayout.this.resetStatus();
                    }
                }

            }
        }, (long)delayed);
        return this;
    }

    public SmartRefreshLayout finishLoadmore(int delayed) {
        return this.finishLoadmore(delayed, true);
    }

    public SmartRefreshLayout finishLoadmore(boolean success) {
        long passTime = System.currentTimeMillis() - this.mLastLoadingTime;
        return this.finishLoadmore(Math.max(0, 1000 - (int)passTime), success);
    }

    public SmartRefreshLayout finishLoadmore(int delayed, final boolean success) {
        this.postDelayed(new Runnable() {
            public void run() {
                if(SmartRefreshLayout.this.mState == RefreshState.Loading) {
                    if(SmartRefreshLayout.this.mRefreshFooter != null && SmartRefreshLayout.this.mKernel != null && SmartRefreshLayout.this.mRefreshContent != null) {
                        int startDelay = SmartRefreshLayout.this.mRefreshFooter.onFinish(SmartRefreshLayout.this, success);
                        if(startDelay == 2147483647) {
                            return;
                        }

                        SmartRefreshLayout.this.notifyStateChanged(RefreshState.LoadFinish);
                        AnimatorUpdateListener updateListener = SmartRefreshLayout.this.mRefreshContent.onLoadingFinish(SmartRefreshLayout.this.mKernel, SmartRefreshLayout.this.mFooterHeight, startDelay, SmartRefreshLayout.this.mReboundDuration);
                        if(SmartRefreshLayout.this.mOnMultiPurposeListener != null) {
                            SmartRefreshLayout.this.mOnMultiPurposeListener.onFooterFinish(SmartRefreshLayout.this.mRefreshFooter, success);
                        }

                        if(SmartRefreshLayout.this.mSpinner == 0) {
                            SmartRefreshLayout.this.resetStatus();
                        } else {
                            ValueAnimator valueAnimator = SmartRefreshLayout.this.animSpinner(0, startDelay);
                            if(updateListener != null && valueAnimator != null) {
                                valueAnimator.addUpdateListener(updateListener);
                            }
                        }
                    } else {
                        SmartRefreshLayout.this.resetStatus();
                    }
                }

            }
        }, (long)delayed);
        return this;
    }

    public boolean isRefreshing() {
        return this.mState == RefreshState.Refreshing;
    }

    public boolean isLoading() {
        return this.mState == RefreshState.Loading;
    }

    public boolean autoRefresh() {
        return this.autoRefresh(400);
    }

    public boolean autoRefresh(int delayed) {
        return this.autoRefresh(delayed, 1.0F * (float)(this.mHeaderHeight + this.mHeaderExtendHeight / 2) / (float)this.mHeaderHeight);
    }

    public boolean autoRefresh(int delayed, final float dragrate) {
        if(this.mState == RefreshState.None && this.mEnableRefresh) {
            if(this.reboundAnimator != null) {
                this.reboundAnimator.cancel();
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    SmartRefreshLayout.this.reboundAnimator = ValueAnimator.ofInt(new int[]{SmartRefreshLayout.this.mSpinner, (int)((float) SmartRefreshLayout.this.mHeaderHeight * dragrate)});
                    SmartRefreshLayout.this.reboundAnimator.setDuration((long) SmartRefreshLayout.this.mReboundDuration);
                    SmartRefreshLayout.this.reboundAnimator.setInterpolator(new DecelerateInterpolator());
                    SmartRefreshLayout.this.reboundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), false);
                        }
                    });
                    SmartRefreshLayout.this.reboundAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            SmartRefreshLayout.this.mLastTouchX = (float)(SmartRefreshLayout.this.getMeasuredWidth() / 2);
                            SmartRefreshLayout.this.setStatePullDownToRefresh();
                        }

                        public void onAnimationEnd(Animator animation) {
                            SmartRefreshLayout.this.reboundAnimator = null;
                            if(SmartRefreshLayout.this.mState != RefreshState.ReleaseToRefresh) {
                                SmartRefreshLayout.this.setStateReleaseToRefresh();
                            }

                            SmartRefreshLayout.this.overSpinner();
                        }
                    });
                    SmartRefreshLayout.this.reboundAnimator.start();
                }
            };
            if(delayed > 0) {
                this.reboundAnimator = new ValueAnimator();
                this.postDelayed(runnable, (long)delayed);
            } else {
                runnable.run();
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean autoLoadmore() {
        return this.autoLoadmore(0);
    }

    public boolean autoLoadmore(int delayed) {
        return this.autoLoadmore(delayed, 1.0F * (float)(this.mFooterHeight + this.mFooterExtendHeight / 2) / (float)this.mFooterHeight);
    }

    public boolean autoLoadmore(int delayed, final float dragrate) {
        if(this.mState == RefreshState.None && this.mEnableLoadmore && !this.mLoadmoreFinished) {
            if(this.reboundAnimator != null) {
                this.reboundAnimator.cancel();
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    SmartRefreshLayout.this.reboundAnimator = ValueAnimator.ofInt(new int[]{SmartRefreshLayout.this.mSpinner, -((int)((float) SmartRefreshLayout.this.mFooterHeight * dragrate))});
                    SmartRefreshLayout.this.reboundAnimator.setDuration((long) SmartRefreshLayout.this.mReboundDuration);
                    SmartRefreshLayout.this.reboundAnimator.setInterpolator(new DecelerateInterpolator());
                    SmartRefreshLayout.this.reboundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            SmartRefreshLayout.this.moveSpinner(((Integer)animation.getAnimatedValue()).intValue(), false);
                        }
                    });
                    SmartRefreshLayout.this.reboundAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            SmartRefreshLayout.this.mLastTouchX = (float)(SmartRefreshLayout.this.getMeasuredWidth() / 2);
                            SmartRefreshLayout.this.setStatePullUpToLoad();
                        }

                        public void onAnimationEnd(Animator animation) {
                            SmartRefreshLayout.this.reboundAnimator = null;
                            if(SmartRefreshLayout.this.mState != RefreshState.ReleaseToLoad) {
                                SmartRefreshLayout.this.setStateReleaseToLoad();
                            }

                            SmartRefreshLayout.this.overSpinner();
                        }
                    });
                    SmartRefreshLayout.this.reboundAnimator.start();
                }
            };
            if(delayed > 0) {
                this.reboundAnimator = new ValueAnimator();
                this.postDelayed(runnable, (long)delayed);
            } else {
                runnable.run();
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean isEnableLoadmore() {
        return this.mEnableLoadmore;
    }

    public boolean isLoadmoreFinished() {
        return this.mLoadmoreFinished;
    }

    public boolean isEnableAutoLoadmore() {
        return this.mEnableAutoLoadmore;
    }

    public boolean isEnableRefresh() {
        return this.mEnableRefresh;
    }

    public boolean isEnableOverScrollBounce() {
        return this.mEnableOverScrollBounce;
    }

    public boolean isEnablePureScrollMode() {
        return this.mEnablePureScrollMode;
    }

    public boolean isEnableScrollContentWhenLoaded() {
        return this.mEnableScrollContentWhenLoaded;
    }

    public static void setDefaultRefreshHeaderCreater(@NonNull DefaultRefreshHeaderCreater creater) {
        sHeaderCreater = creater;
    }

    public static void setDefaultRefreshFooterCreater(@NonNull DefaultRefreshFooterCreater creater) {
        sFooterCreater = creater;
        sManualFooterCreater = true;
    }

    public boolean post(Runnable action) {
        if(this.handler == null) {
            this.mDelayedRunables = (List)(this.mDelayedRunables == null?new ArrayList():this.mDelayedRunables);
            this.mDelayedRunables.add(new DelayedRunable(action));
            return false;
        } else {
            return this.handler.post(new DelayedRunable(action));
        }
    }

    public boolean postDelayed(Runnable action, long delayMillis) {
        if(this.handler == null) {
            this.mDelayedRunables = (List)(this.mDelayedRunables == null?new ArrayList():this.mDelayedRunables);
            this.mDelayedRunables.add(new DelayedRunable(action, delayMillis));
            return false;
        } else {
            return this.handler.postDelayed(new DelayedRunable(action), delayMillis);
        }
    }

    protected class RefreshKernelImpl implements RefreshKernel {
        protected RefreshKernelImpl() {
        }

        @NonNull
        public RefreshLayout getRefreshLayout() {
            return SmartRefreshLayout.this;
        }

        @NonNull
        public RefreshContent getRefreshContent() {
            return SmartRefreshLayout.this.mRefreshContent;
        }

        public RefreshKernel setStatePullUpToLoad() {
            SmartRefreshLayout.this.setStatePullUpToLoad();
            return this;
        }

        public RefreshKernel setStateReleaseToLoad() {
            SmartRefreshLayout.this.setStateReleaseToLoad();
            return this;
        }

        public RefreshKernel setStateReleaseToRefresh() {
            SmartRefreshLayout.this.setStateReleaseToRefresh();
            return this;
        }

        public RefreshKernel setStatePullDownToRefresh() {
            SmartRefreshLayout.this.setStatePullDownToRefresh();
            return this;
        }

        public RefreshKernel setStatePullDownCanceled() {
            SmartRefreshLayout.this.setStatePullDownCanceled();
            return this;
        }

        public RefreshKernel setStatePullUpCanceled() {
            SmartRefreshLayout.this.setStatePullUpCanceled();
            return this;
        }

        public RefreshKernel setStateLoding() {
            SmartRefreshLayout.this.setStateLoding();
            return this;
        }

        public RefreshKernel setStateRefresing() {
            SmartRefreshLayout.this.setStateRefresing();
            return this;
        }

        public RefreshKernel setStateLodingFinish() {
            SmartRefreshLayout.this.setStateLodingFinish();
            return this;
        }

        public RefreshKernel setStateRefresingFinish() {
            SmartRefreshLayout.this.setStateRefresingFinish();
            return this;
        }

        public RefreshKernel resetStatus() {
            SmartRefreshLayout.this.resetStatus();
            return this;
        }

        public RefreshKernel moveSpinner(int spinner, boolean isAnimator) {
            SmartRefreshLayout.this.moveSpinner(spinner, isAnimator);
            return this;
        }

        public RefreshKernel animSpinner(int endSpinner) {
            SmartRefreshLayout.this.animSpinner(endSpinner);
            return this;
        }

        public int getSpinner() {
            return SmartRefreshLayout.this.mSpinner;
        }

        public RefreshKernel requestDrawBackgoundForHeader(int backgroundColor) {
            if(SmartRefreshLayout.this.mPaint == null && backgroundColor != 0) {
                SmartRefreshLayout.this.mPaint = new Paint();
            }

            SmartRefreshLayout.this.mHeaderBackgroundColor = backgroundColor;
            return this;
        }

        public RefreshKernel requestDrawBackgoundForFooter(int backgroundColor) {
            if(SmartRefreshLayout.this.mPaint == null && backgroundColor != 0) {
                SmartRefreshLayout.this.mPaint = new Paint();
            }

            SmartRefreshLayout.this.mFooterBackgroundColor = backgroundColor;
            return this;
        }

        public RefreshKernel requestHeaderNeedTouchEventWhenRefreshing(boolean request) {
            SmartRefreshLayout.this.mHeaderNeedTouchEventWhenRefreshing = request;
            return this;
        }

        public RefreshKernel requestFooterNeedTouchEventWhenLoading(boolean request) {
            SmartRefreshLayout.this.mFooterNeedTouchEventWhenRefreshing = request;
            return this;
        }

        public RefreshKernel requestRemeasureHeightForHeader() {
            if(SmartRefreshLayout.this.mHeaderHeightStatus.notifyed) {
                SmartRefreshLayout.this.mHeaderHeightStatus = SmartRefreshLayout.this.mHeaderHeightStatus.unNotify();
            }

            return this;
        }

        public RefreshKernel requestRemeasureHeightForFooter() {
            if(SmartRefreshLayout.this.mFooterHeightStatus.notifyed) {
                SmartRefreshLayout.this.mFooterHeightStatus = SmartRefreshLayout.this.mFooterHeightStatus.unNotify();
            }

            return this;
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int backgroundColor = 0;
        public SpinnerStyle spinnerStyle = null;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRefreshLayout_Layout);
            this.backgroundColor = ta.getColor(R.styleable.SmartRefreshLayout_Layout_layout_srlBackgroundColor, this.backgroundColor);
            if(ta.hasValue(R.styleable.SmartRefreshLayout_Layout_layout_srlSpinnerStyle)) {
                this.spinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.SmartRefreshLayout_Layout_layout_srlSpinnerStyle, SpinnerStyle.Translate.ordinal())];
            }

            ta.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}

