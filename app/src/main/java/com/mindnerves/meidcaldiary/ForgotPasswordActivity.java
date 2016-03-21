package com.mindnerves.meidcaldiary;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mindnerves.meidcaldiary.Fragments.ForgetPassword;
import com.mindnerves.meidcaldiary.Fragments.Login;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Region;
import android.os.BatteryManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.Login;

import java.util.Calendar;

/**
 * Created by Narendra on 17-02-2016.
 */
public class ForgotPasswordActivity extends FragmentActivity {
    public ForgetPassword forgetPassword;

    @Override
    public void onBackPressed() {

        // initialize variables
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

// check to see if stack is empty
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            ft.commit();
        } /*else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.confirm_logout);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Do Nothing");
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetPassword = new ForgetPassword();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.lower_content, forgetPassword);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        /* Fragment frag = new ForgetPassword();
                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        ft.replace(R.id.lower_content, frag, "Forget_Password");
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
