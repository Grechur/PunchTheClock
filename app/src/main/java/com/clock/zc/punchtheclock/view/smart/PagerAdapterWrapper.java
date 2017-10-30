package com.clock.zc.punchtheclock.view.smart;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class PagerAdapterWrapper extends PagerAdapter {
    protected PagerAdapter wrapped = null;

    public PagerAdapterWrapper(PagerAdapter wrapped) {
        this.wrapped = wrapped;
    }

    public void attachViewPager(ViewPager viewPager) {
        try {
            Field[] fields = ViewPager.class.getDeclaredFields();
            if(fields != null && fields.length > 0) {
                Field[] var3 = fields;
                int var4 = fields.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Field field = var3[var5];
                    if(PagerAdapter.class.equals(field.getType())) {
                        field.setAccessible(true);
                        field.set(viewPager, this);
                        break;
                    }
                }
            }
        } catch (IllegalAccessException var7) {
            var7.printStackTrace();
        }

    }


    public int getCount() {
        return this.wrapped.getCount();
    }

    public void startUpdate(ViewGroup container) {
        this.wrapped.startUpdate(container);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        return this.wrapped.instantiateItem(container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        this.wrapped.destroyItem(container, position, object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.wrapped.setPrimaryItem(container, position, object);
    }

    public void finishUpdate(ViewGroup container) {
        this.wrapped.finishUpdate(container);
    }

    /** @deprecated */
    @Deprecated
    public void startUpdate(View container) {
        this.wrapped.startUpdate(container);
    }

    /** @deprecated */
    @Deprecated
    public Object instantiateItem(View container, int position) {
        return this.wrapped.instantiateItem(container, position);
    }

    /** @deprecated */
    @Deprecated
    public void destroyItem(View container, int position, Object object) {
        this.wrapped.destroyItem(container, position, object);
    }

    /** @deprecated */
    @Deprecated
    public void setPrimaryItem(View container, int position, Object object) {
        this.wrapped.setPrimaryItem(container, position, object);
    }

    /** @deprecated */
    @Deprecated
    public void finishUpdate(View container) {
        this.wrapped.finishUpdate(container);
    }

    public boolean isViewFromObject(View view, Object object) {
        return this.wrapped.isViewFromObject(view, object);
    }

    public Parcelable saveState() {
        return this.wrapped.saveState();
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
        this.wrapped.restoreState(state, loader);
    }

    public int getItemPosition(Object object) {
        return this.wrapped.getItemPosition(object);
    }

    public void notifyDataSetChanged() {
        this.wrapped.notifyDataSetChanged();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.wrapped.registerDataSetObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.wrapped.unregisterDataSetObserver(observer);
    }

    public CharSequence getPageTitle(int position) {
        return this.wrapped.getPageTitle(position);
    }

    public float getPageWidth(int position) {
        return this.wrapped.getPageWidth(position);
    }
}