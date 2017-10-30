package com.clock.zc.punchtheclock.view.smart.impl;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.clock.zc.punchtheclock.view.smart.PagerAdapterWrapper;
import com.clock.zc.punchtheclock.view.smart.api.RefreshContent;
import com.clock.zc.punchtheclock.view.smart.api.RefreshKernel;
import com.clock.zc.punchtheclock.view.smart.api.RefreshLayout;
import com.clock.zc.punchtheclock.view.smart.api.ScrollBoundaryDecider;
import com.clock.zc.punchtheclock.view.smart.util.ScrollBoundaryUtil;

import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.View.MeasureSpec.UNSPECIFIED;

public class RefreshContentWrapper implements RefreshContent {
    protected int mHeaderHeight = 2147483647;
    protected int mFooterHeight;
    protected View mContentView;
    protected View mRealContentView;
    protected View mScrollableView;
    protected View mFixedHeader;
    protected View mFixedFooter;
    protected boolean mEnableRefresh;
    protected boolean mEnableLoadmore;
    protected MotionEvent mMotionEvent;
    protected ScrollBoundaryDeciderAdapter mBoundaryAdapter;

    public RefreshContentWrapper(View view) {
        this.mFooterHeight = this.mHeaderHeight - 1;
        this.mEnableRefresh = true;
        this.mEnableLoadmore = true;
        this.mBoundaryAdapter = new ScrollBoundaryDeciderAdapter();
        this.mContentView = this.mRealContentView = view;
    }

    public RefreshContentWrapper(Context context) {
        this.mFooterHeight = this.mHeaderHeight - 1;
        this.mEnableRefresh = true;
        this.mEnableLoadmore = true;
        this.mBoundaryAdapter = new ScrollBoundaryDeciderAdapter();
        this.mContentView = this.mRealContentView = new View(context);
    }

    protected void findScrollableView(View content, RefreshKernel kernel) {
        for(this.mScrollableView = null; this.mScrollableView == null || this.mScrollableView instanceof NestedScrollingParent && !(this.mScrollableView instanceof NestedScrollingChild); this.mScrollableView = content) {
            content = this.findScrollableViewInternal(content, this.mScrollableView == null);
            if(content == this.mScrollableView) {
                break;
            }

            try {
                if(content instanceof CoordinatorLayout) {
                    kernel.getRefreshLayout().setEnableNestedScroll(false);
                    this.wrapperCoordinatorLayout((CoordinatorLayout)content, kernel.getRefreshLayout());
                }
            } catch (Throwable var5) {
                ;
            }

            try {
                if(content instanceof ViewPager) {
                    this.wrapperViewPager((ViewPager)content);
                }
            } catch (Throwable var4) {
                ;
            }
        }

    }

    protected void wrapperCoordinatorLayout(CoordinatorLayout layout, final RefreshLayout refreshLayout) {
        for(int i = layout.getChildCount() - 1; i >= 0; --i) {
            View view = layout.getChildAt(i);
            if(view instanceof AppBarLayout) {
                ((AppBarLayout)view).addOnOffsetChangedListener(new OnOffsetChangedListener() {
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        RefreshContentWrapper.this.mEnableRefresh = verticalOffset >= 0;
                        RefreshContentWrapper.this.mEnableLoadmore = refreshLayout.isEnableLoadmore() && appBarLayout.getTotalScrollRange() + verticalOffset <= 0;
                    }
                });
            }
        }

    }

    protected void wrapperViewPager(ViewPager viewPager) {
        this.wrapperViewPager(viewPager, (RefreshContentWrapper.PagerPrimaryAdapter)null);
    }

    protected void wrapperViewPager(final ViewPager viewPager, final RefreshContentWrapper.PagerPrimaryAdapter primaryAdapter) {
        viewPager.post(new Runnable() {
            int count = 0;
            RefreshContentWrapper.PagerPrimaryAdapter mAdapter = primaryAdapter;

            public void run() {
                ++this.count;
                PagerAdapter adapter = viewPager.getAdapter();
                if(adapter != null) {
                    if(adapter instanceof RefreshContentWrapper.PagerPrimaryAdapter) {
                        if(adapter == primaryAdapter && this.count < 10) {
                            viewPager.postDelayed(this, 500L);
                        }
                    } else {
                        if(this.mAdapter == null) {
                            this.mAdapter = RefreshContentWrapper.this.new PagerPrimaryAdapter(adapter);
                        } else {
                            this.mAdapter.wrapper(adapter);
                        }

                        this.mAdapter.attachViewPager(viewPager);
                    }
                } else if(this.count < 10) {
                    viewPager.postDelayed(this, 500L);
                }

            }
        });
    }

    protected View findScrollableViewInternal(View content, boolean selfable) {
        View scrollableView = null;
        LinkedBlockingQueue views = new LinkedBlockingQueue(Collections.singletonList(content));

        while(!views.isEmpty() && scrollableView == null) {
            View view = (View)views.poll();
            if(view != null) {
                if((selfable || view != content) && (view instanceof AbsListView || view instanceof ScrollView || view instanceof ScrollingView || view instanceof NestedScrollingChild || view instanceof NestedScrollingParent || view instanceof WebView || view instanceof ViewPager)) {
                    scrollableView = view;
                } else if(view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup)view;

                    for(int j = 0; j < group.getChildCount(); ++j) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        }

        return scrollableView == null?content:scrollableView;
    }

    @NonNull
    public View getView() {
        return this.mContentView;
    }

    public void moveSpinner(int spinner) {
        this.mRealContentView.setTranslationY((float)spinner);
        if(this.mFixedHeader != null) {
            this.mFixedHeader.setTranslationY((float)Math.max(0, spinner));
        }

        if(this.mFixedFooter != null) {
            this.mFixedFooter.setTranslationY((float)Math.min(0, spinner));
        }

    }

    public boolean canRefresh() {
        return this.mEnableRefresh && this.mBoundaryAdapter.canRefresh(this.mContentView);
    }

    public boolean canLoadmore() {
        return this.mEnableLoadmore && this.mBoundaryAdapter.canLoadmore(this.mContentView);
    }

    public void measure(int widthSpec, int heightSpec) {
        this.mContentView.measure(widthSpec, heightSpec);
    }

    public LayoutParams getLayoutParams() {
        return this.mContentView.getLayoutParams();
    }

    public int getMeasuredWidth() {
        return this.mContentView.getMeasuredWidth();
    }

    public int getMeasuredHeight() {
        return this.mContentView.getMeasuredHeight();
    }

    public void layout(int left, int top, int right, int bottom, boolean skip) {
        if(skip && this.mContentView.getLeft() == left && this.mContentView.getTop() == top && this.mContentView.getRight() == right && this.mContentView.getBottom() == bottom) {
            System.out.println("skip!");
        } else {
            this.mContentView.layout(left, top, right, bottom);
        }

    }

    public View getScrollableView() {
        return this.mScrollableView;
    }

    public void onActionDown(MotionEvent e) {
        this.mMotionEvent = MotionEvent.obtain(e);
        this.mMotionEvent.offsetLocation((float)(-this.mContentView.getLeft()), (float)(-this.mContentView.getTop()));
        this.mBoundaryAdapter.setActionEvent(this.mMotionEvent);
    }

    public void onActionUpOrCancel() {
        this.mMotionEvent = null;
    }

    public void setupComponent(RefreshKernel kernel, View fixedHeader, View fixedFooter) {
        this.findScrollableView(this.mContentView, kernel);
        if(fixedHeader != null || fixedFooter != null) {
            this.mFixedHeader = fixedHeader;
            this.mFixedFooter = fixedFooter;
            FrameLayout frameLayout = new FrameLayout(this.mContentView.getContext());
            kernel.getRefreshLayout().getLayout().removeView(this.mContentView);
            LayoutParams layoutParams = this.mContentView.getLayoutParams();
            frameLayout.addView(this.mContentView, -1, -1);
            kernel.getRefreshLayout().getLayout().addView(frameLayout, layoutParams);
            this.mContentView = frameLayout;
            LayoutParams lp;
            ViewGroup parent;
            int index;
            if(fixedHeader != null) {
                fixedHeader.setClickable(true);
                lp = fixedHeader.getLayoutParams();
                parent = (ViewGroup)fixedHeader.getParent();
                index = parent.indexOfChild(fixedHeader);
                parent.removeView(fixedHeader);
                lp.height = measureViewHeight(fixedHeader);
                parent.addView(new Space(this.mContentView.getContext()), index, lp);
                frameLayout.addView(fixedHeader);
            }

            if(fixedFooter != null) {
                fixedFooter.setClickable(true);
                lp = fixedFooter.getLayoutParams();
                parent = (ViewGroup)fixedFooter.getParent();
                index = parent.indexOfChild(fixedFooter);
                parent.removeView(fixedFooter);
                FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(lp);
                lp.height = measureViewHeight(fixedFooter);
                parent.addView(new Space(this.mContentView.getContext()), index, lp);
                flp.gravity = 80;
                frameLayout.addView(fixedFooter, flp);
            }
        }

    }

    public void onInitialHeaderAndFooter(int headerHeight, int footerHeight) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
    }

    public void setScrollBoundaryDecider(ScrollBoundaryDecider boundary) {
        if(boundary instanceof ScrollBoundaryDeciderAdapter) {
            this.mBoundaryAdapter = (ScrollBoundaryDeciderAdapter)boundary;
        } else {
            this.mBoundaryAdapter.setScrollBoundaryDecider(boundary);
        }

    }

    public void setEnableLoadmoreWhenContentNotFull(boolean enable) {
        this.mBoundaryAdapter.setEnableLoadmoreWhenContentNotFull(enable);
    }

    public AnimatorUpdateListener onLoadingFinish(final RefreshKernel kernel, final int footerHeight, int startDelay, final int duration) {
        if(this.mScrollableView != null && kernel.getRefreshLayout().isEnableScrollContentWhenLoaded()) {
            if(!ScrollBoundaryUtil.canScrollDown(this.mScrollableView)) {
                return null;
            } else if(this.mScrollableView instanceof AbsListView && !(this.mScrollableView instanceof ListView) && VERSION.SDK_INT < 19) {
                if(startDelay > 0) {
                    kernel.getRefreshLayout().getLayout().postDelayed(new Runnable() {
                        public void run() {
                            ((AbsListView)RefreshContentWrapper.this.mScrollableView).smoothScrollBy(footerHeight, duration);
                        }
                    }, (long)startDelay);
                } else {
                    ((AbsListView)this.mScrollableView).smoothScrollBy(footerHeight, duration);
                }

                return null;
            } else {
                return new AnimatorUpdateListener() {
                    int lastValue = kernel.getSpinner();

                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = ((Integer)animation.getAnimatedValue()).intValue();

                        try {
                            if(RefreshContentWrapper.this.mScrollableView instanceof ListView) {
                                if(VERSION.SDK_INT >= 19) {
                                    ((ListView)RefreshContentWrapper.this.mScrollableView).scrollListBy(value - this.lastValue);
                                } else {
                                    ListView listView = (ListView)RefreshContentWrapper.this.mScrollableView;
                                    int firstPosition = listView.getFirstVisiblePosition();
                                    if(firstPosition == -1) {
                                        return;
                                    }

                                    View firstView = listView.getChildAt(0);
                                    if(firstView == null) {
                                        return;
                                    }

                                    int newTop = firstView.getTop() - (value - this.lastValue);
                                    listView.setSelectionFromTop(firstPosition, newTop);
                                }
                            } else {
                                RefreshContentWrapper.this.mScrollableView.scrollBy(0, value - this.lastValue);
                            }
                        } catch (Throwable var7) {
                            ;
                        }

                        this.lastValue = value;
                    }
                };
            }
        } else {
            return null;
        }
    }

    protected static int measureViewHeight(View view) {
        LayoutParams p = view.getLayoutParams();
        if(p == null) {
            p = new LayoutParams(-1, -2);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int childHeightSpec;
        if(p.height > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, UNSPECIFIED);
        }

        view.measure(childWidthSpec, childHeightSpec);
        return view.getMeasuredHeight();
    }

    protected class PagerPrimaryAdapter extends PagerAdapterWrapper {
        protected ViewPager mViewPager;

        PagerPrimaryAdapter(PagerAdapter wrapped) {
            super(wrapped);
        }

        void wrapper(PagerAdapter adapter) {
            this.wrapped = adapter;
        }

        public void attachViewPager(ViewPager viewPager) {
            this.mViewPager = viewPager;
            super.attachViewPager(viewPager);
        }

        public void setViewPagerObserver(DataSetObserver observer) {
            if(observer == null) {
                RefreshContentWrapper.this.wrapperViewPager(this.mViewPager, this);
            }

        }

        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            View content = null;
            if(object instanceof View) {
                content = (View)object;
            } else if(object instanceof Fragment) {
                content = ((Fragment)object).getView();
            }

            if(content != null) {
                for(RefreshContentWrapper.this.mScrollableView = null; RefreshContentWrapper.this.mScrollableView == null || RefreshContentWrapper.this.mScrollableView instanceof NestedScrollingParent && !(RefreshContentWrapper.this.mScrollableView instanceof NestedScrollingChild); RefreshContentWrapper.this.mScrollableView = content) {
                    content = RefreshContentWrapper.this.findScrollableViewInternal(content, RefreshContentWrapper.this.mScrollableView == null);
                    if(content == RefreshContentWrapper.this.mScrollableView) {
                        break;
                    }
                }
            }

        }
    }
}
