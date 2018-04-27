package com.clock.zc.punchtheclock.util;

        import android.content.Context;
        import android.text.TextUtils;
        import android.view.View;

        import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
        import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by Zc on 2017/10/11.
 * Effectstype.Fadein
 * Effectstype.Slideright 从右平移进入
 * Effectstype.Slideleft 从左平移进入
 * Effectstype.Slidetop从上平移进入
 * Effectstype.SlideBottom从下平移进入
 * Effectstype.Newspager 旋转进入
 * Effectstype.Fall从两边收缩显示
 * Effectstype.Sidefill上斜着进入
 * Effectstype.Fliph右边翻转进入
 * Effectstype.Flipv上边翻转进入
 * Effectstype.RotateBottom下边翻转
 * Effectstype.RotateLeft左边翻转
 * Effectstype.Slit左翻到右
 * Effectstype.Shake左右抖动
 */

public class EffectsDialogUtil {
    private NiftyDialogBuilder dialogBuilder;

    public void createSingleDialog(Context context, String title, Effectstype effectstype, String content, String ok, View.OnClickListener listener) {
        if (TextUtils.isEmpty(ok)) ok = "确定";
        if (TextUtils.isEmpty(title)) title = "提示";
        dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(content)                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#00aeFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effectstype)                                         //def Effectstype.Slidetop
                .withButton1Text(ok)                                      //def gone
                .setButton1Click(listener)
                .isCancelableOnTouchOutside(false)
                .show();
    }

    public void createSingleDialog(Context context, String title, Effectstype effectstype, String content, View.OnClickListener listener) {
        createSingleDialog(context, title, effectstype, content, null, listener);
    }

    public void createSingleDialog(Context context, Effectstype effectstype, String content, String ok, View.OnClickListener listener) {
        createSingleDialog(context, null, effectstype, content, ok, listener);
    }

    public void createSingleDialog(Context context, Effectstype effectstype, String content, View.OnClickListener listener) {
        createSingleDialog(context, null, effectstype, content, listener);
    }


    public void createDoubleDialog(Context context, String title, Effectstype effectstype, String content, String ok, String cancle, View.OnClickListener slistener, View.OnClickListener clistener) {
        if (TextUtils.isEmpty(ok)) ok = "确定";
        if (TextUtils.isEmpty(title)) title = "提示";
        if (TextUtils.isEmpty(cancle)) cancle = "取消";
        dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(content)                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#00aeFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effectstype)                                         //def Effectstype.Slidetop
                .withButton1Text(ok)                                      //def gone
                .withButton2Text(cancle)                                  //def gone
//                .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(slistener)
                .isCancelableOnTouchOutside(false)
                .setButton2Click(clistener)
                .show();
    }

    public void createDoubleDialog(Context context, Effectstype effectstype, String content, String ok, String cancle, View.OnClickListener slistener, View.OnClickListener clistener) {
        createDoubleDialog(context, null, effectstype, content, ok, cancle, slistener, clistener);
    }

    public void createDoubleDialog(Context context, String title, Effectstype effectstype, String content, String cancle, View.OnClickListener slistener, View.OnClickListener clistener) {
        createDoubleDialog(context, title, effectstype, content, null, cancle, slistener, clistener);
    }

    public void createDoubleDialog(Context context, String title, Effectstype effectstype, String content, View.OnClickListener slistener, View.OnClickListener clistener) {
        createDoubleDialog(context, title, effectstype, content, null, slistener, clistener);
    }

    public void cancleDialog(){
        try {
            if(dialogBuilder!=null && dialogBuilder.isShowing()){
                dialogBuilder.dismiss();
                dialogBuilder = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}