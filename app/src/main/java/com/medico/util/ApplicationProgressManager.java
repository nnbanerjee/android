package com.medico.util;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Narendra on 20-04-2017.
 */

public class ApplicationProgressManager
{
    int index = 0,x=10,y=10;
    long expandDuration = 10000;
    Animation animation;

    private static ApplicationProgressManager progressManager;

    private ApplicationProgressManager(Context context)
    {
        animation = createItemOutAnimation(context,index,expandDuration,x,y);
    }

    public static ApplicationProgressManager getInstance(Context context)
    {
        if(progressManager == null)
        {
            progressManager = new ApplicationProgressManager(context);
        }
        return progressManager;
    }

    public Animation getAnimation()
    {
        return animation;
    }


    private Animation createItemOutAnimation(Context context, int index, long expandDuration, int x, int y)
    {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        long alphaDuration = 60;
        if(expandDuration < 60){
            alphaDuration = expandDuration / 4;
        }
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartOffset(0);


        TranslateAnimation translate = new TranslateAnimation(0, x, 0, y);

        translate.setStartOffset(0);
        translate.setDuration(expandDuration);
        translate.setInterpolator(new LinearInterpolator());

        RotateAnimation rotate = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setInterpolator(new LinearInterpolator());
        long duration = 100;
        if(expandDuration <= 150){
            duration = expandDuration / 3;
        }

        rotate.setDuration(expandDuration-duration);
        rotate.setStartOffset(duration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(false);
        animationSet.setFillBefore(true);
        animationSet.setFillEnabled(true);

//        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotate);
//        animationSet.addAnimation(translate);

        animationSet.setStartOffset(30*index);

        return animationSet;
    }

}
