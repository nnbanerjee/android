package com.medicohealthcare.util;

import android.content.Context;
import android.widget.Toast;

import com.medicohealthcare.view.home.HomeActivity;

/**
 * Created by Narendra on 27-07-2017.
 */

public class FloatingMessage extends Toast
{
    public FloatingMessage(Context context)
    {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration)
    {
        if(context == null)
            context = HomeActivity.getParentAtivity();
        return Toast.makeText(context, text, duration);
    }
}
