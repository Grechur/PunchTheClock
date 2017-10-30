package com.clock.zc.punchtheclock.view.wave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class AnimationImageView extends ImageView {
    private AnimationListener mListener;

    public AnimationImageView(Context context) {
        super(context);
    }

    public void setAnimationListener(AnimationListener listener) {
        this.mListener = listener;
    }

    public void onAnimationStart() {
        super.onAnimationStart();
        if(this.mListener != null) {
            this.mListener.onAnimationStart(this.getAnimation());
        }

    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if(this.mListener != null) {
            this.mListener.onAnimationEnd(this.getAnimation());
        }

    }
}