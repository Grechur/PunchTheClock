package com.clock.zc.punchtheclock.view.smart.util;

import android.graphics.PointF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class ScrollBoundaryUtil {
    public ScrollBoundaryUtil() {
    }

    public static boolean canRefresh(View targetView, MotionEvent event) {
        if(canScrollUp(targetView) && targetView.getVisibility() == 0) {
            return false;
        } else {
            if(targetView instanceof ViewGroup && event != null) {
                ViewGroup viewGroup = (ViewGroup)targetView;
                int childCount = viewGroup.getChildCount();
                PointF point = new PointF();

                for(int i = childCount; i > 0; --i) {
                    View child = viewGroup.getChildAt(i - 1);
                    if(isTransformedTouchPointInView(viewGroup, child, event.getX(), event.getY(), point)) {
                        event = MotionEvent.obtain(event);
                        event.offsetLocation(point.x, point.y);
                        return canRefresh(child, event);
                    }
                }
            }

            return true;
        }
    }

    public static boolean canLoadmore(View targetView, MotionEvent event) {
        if(!canScrollDown(targetView) && canScrollUp(targetView) && targetView.getVisibility() == 0) {
            return true;
        } else {
            if(targetView instanceof ViewGroup && event != null) {
                ViewGroup viewGroup = (ViewGroup)targetView;
                int childCount = viewGroup.getChildCount();
                PointF point = new PointF();

                for(int i = 0; i < childCount; ++i) {
                    View child = viewGroup.getChildAt(i);
                    if(isTransformedTouchPointInView(viewGroup, child, event.getX(), event.getY(), point)) {
                        event = MotionEvent.obtain(event);
                        event.offsetLocation(point.x, point.y);
                        return canLoadmore(child, event);
                    }
                }
            }

            return false;
        }
    }

    public static boolean canScrollDown(View targetView, MotionEvent event) {
        if(canScrollDown(targetView) && targetView.getVisibility() == 0) {
            return true;
        } else {
            if(targetView instanceof ViewGroup && event != null) {
                ViewGroup viewGroup = (ViewGroup)targetView;
                int childCount = viewGroup.getChildCount();
                PointF point = new PointF();

                for(int i = 0; i < childCount; ++i) {
                    View child = viewGroup.getChildAt(i);
                    if(isTransformedTouchPointInView(viewGroup, child, event.getX(), event.getY(), point)) {
                        event = MotionEvent.obtain(event);
                        event.offsetLocation(point.x, point.y);
                        return canScrollDown(child, event);
                    }
                }
            }

            return false;
        }
    }

    public static boolean canScrollUp(View targetView) {
        if(VERSION.SDK_INT < 14) {
            if(!(targetView instanceof AbsListView)) {
                return targetView.getScrollY() > 0;
            } else {
                AbsListView absListView = (AbsListView)targetView;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            }
        } else {
            return targetView.canScrollVertically(-1);
        }
    }

    public static boolean canScrollDown(View targetView) {
        if(VERSION.SDK_INT < 14) {
            if(!(targetView instanceof AbsListView)) {
                return targetView.getScrollY() < 0;
            } else {
                AbsListView absListView = (AbsListView)targetView;
                return absListView.getChildCount() > 0 && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1 || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            }
        } else {
            return targetView.canScrollVertically(1);
        }
    }

    public static boolean pointInView(View view, float localX, float localY, float slop) {
        float left = -slop;
        float top = -slop;
        float width = (float)view.getWidth();
        float height = (float)view.getHeight();
        return localX >= left && localY >= top && localX < width + slop && localY < height + slop;
    }

    public static boolean isTransformedTouchPointInView(ViewGroup group, View child, float x, float y, PointF outLocalPoint) {
        if(child.getVisibility() != 0) {
            return false;
        } else {
            float[] point = new float[]{x, y};
            transformPointToViewLocal(group, child, point);
            boolean isInView = pointInView(child, point[0], point[1], 0.0F);
            if(isInView && outLocalPoint != null) {
                outLocalPoint.set(point[0] - x, point[1] - y);
            }

            return isInView;
        }
    }

    public static void transformPointToViewLocal(ViewGroup group, View child, float[] point) {
        point[0] += (float)(group.getScrollX() - child.getLeft());
        point[1] += (float)(group.getScrollY() - child.getTop());
    }
}

