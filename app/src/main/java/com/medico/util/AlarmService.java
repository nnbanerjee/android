package com.medico.util;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmService extends BroadcastReceiver {

	@Override
	 public void onReceive(Context context, Intent intent) {
		WakeLocker.acquire(context);
        String title = intent.getExtras().getString("title");
        System.out.println("in Alarm Service //////////////////////////////");
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        Intent nextIntent;
        nextIntent = new Intent(context,DisplayAlarm.class);
        nextIntent.putExtra("title",title);
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(nextIntent);
	 }
	
}
