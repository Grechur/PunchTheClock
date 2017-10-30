package com.clock.zc.punchtheclock.ui;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.base.BaseActivity;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
//import com.sunfusheng.glideimageview.GlideImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonCenterActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rl_center)
    RelativeLayout rl_center;
//    @BindView(R.id.iv_user_img)
//    GlideImageView iv_user_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setEnterTransition(initContentEnterTransition());
            getWindow().setSharedElementEnterTransition(initSharedElementEnterTransition());
        }
        title.setText("个人中心");
        String url = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=221689415,26330428&fm=27&gp=0.jpg";
//        iv_user_img.loadImage(url,R.mipmap.ic_launcher);
    }

    @OnClick({R.id.back,R.id.title,R.id.rl_center})
    void toggleEvent(View v){
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.title:
                effectsDialogUtil.createSingleDialog(context, Effectstype.Slit, "hahaha", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        effectsDialogUtil.cancleDialog();
                    }
                });
                break;
            case R.id.rl_center:
                effectsDialogUtil.createSingleDialog(context, Effectstype.Shake, "hahaha", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        effectsDialogUtil.cancleDialog();
                    }
                });
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Transition initContentEnterTransition() {
        Transition transition= TransitionInflater.from(this).inflateTransition(R.transition.slide_and_fade);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return transition;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Transition initSharedElementEnterTransition() {
        final Transition sharedTransition=TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        sharedTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(rl_center, rl_center.getWidth() / 2, rl_center.getHeight() / 2
                        , rl_center.getWidth()/2, Math.max(rl_center.getWidth(), rl_center.getHeight()));
                rl_center.setBackgroundColor(Color.BLACK);
                circularReveal.setDuration(600);
                circularReveal.start();
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                sharedTransition.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return sharedTransition;
    }

}
