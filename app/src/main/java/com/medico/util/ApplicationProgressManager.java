package com.medico.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.medico.application.R;

/**
 * Created by Narendra on 20-04-2017.
 */

public class ApplicationProgressManager
{
//    int index = 0,x=300,y=300;
//    long expandDuration = 10000;
//    Animation animation;
    ProgressDialog progress;
    Context context;

    private static ApplicationProgressManager progressManager;

    private ApplicationProgressManager(Context context)
    {
       progress = new ProgressDialog(context);
        this.context = context;
    }

    public static ApplicationProgressManager getInstance(Context context)
    {
        if(progressManager == null || progressManager.context == null || progressManager.context != context)
        {
            progressManager = new ApplicationProgressManager(context);
        }
        return progressManager;
    }

//    public Animation getAnimation()
//    {
//        return animation;
//    }


    private Animation createItemOutAnimation(Context context, int index, long expandDuration, int x, int y)
    {
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
//        long alphaDuration = 60;
//        if(expandDuration < 60){
//            alphaDuration = expandDuration / 4;
//        }
//        alphaAnimation.setDuration(alphaDuration);
//        alphaAnimation.setStartOffset(0);
//
//
//        TranslateAnimation translate = new TranslateAnimation(0, x, 0, y);
//
//        translate.setStartOffset(0);
//        translate.setDuration(expandDuration);
//        translate.setInterpolator(new LinearInterpolator());
//
//        RotateAnimation rotate = new RotateAnimation(0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//
//        rotate.setInterpolator(new LinearInterpolator());
//        long duration = 100;
//        if(expandDuration <= 150){
//            duration = expandDuration / 3;
//        }
//
//        rotate.setDuration(expandDuration-duration);
//        rotate.setStartOffset(duration);
//
//        AnimationSet animationSet = new AnimationSet(true);
//        animationSet.setFillAfter(false);
//        animationSet.setFillBefore(true);
//        animationSet.setFillEnabled(true);
//
//        animationSet.addAnimation(alphaAnimation);
//        animationSet.addAnimation(rotate);
//        animationSet.addAnimation(translate);
//
//        animationSet.setStartOffset(30*index);
        Animation animRotate= AnimationUtils.loadAnimation(context, R.anim.image_rotate);
        Animation fade_in = AnimationUtils.loadAnimation(context, R.anim.animation_fade_in);
        Animation fade_out = AnimationUtils.loadAnimation(context, R.anim.animation_fade_out);

        AnimationSet s = new AnimationSet(false);
        s.addAnimation(fade_in);

        animRotate.setDuration((long) 10000);
        animRotate.setStartOffset(fade_in.getDuration());
        s.addAnimation(animRotate);

        fade_out.setStartOffset(fade_in.getDuration() + animRotate.getDuration());
        s.addAnimation(fade_out);

        s.setFillAfter(true);

        return s;
    }

    public ProgressDialog getProgressDialog()
    {
        return progress;
    }

}
