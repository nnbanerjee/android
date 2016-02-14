package com.mindnerves.meidcaldiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MNT on 18-Mar-15.
 */
public class MyReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);


    }
}
