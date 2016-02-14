package com.mindnerves.meidcaldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DisplayAlarm extends Activity {
	Button dismiss;
	Ringtone ringtone;
    TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_alarm);
        String title = this.getIntent().getExtras().getString("title");
        getActionBar().hide();
        tv = (TextView)findViewById(R.id.tonetype);
        tv.setText(title);

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
		ringtone.play();
		dismiss = (Button) findViewById(R.id.dismiss);
		dismiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ringtone.stop();
				finish();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        /*.setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	                WelcomeActivity.super.onBackPressed();
	            }

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
	        }).create().show()*/;
	}
}
