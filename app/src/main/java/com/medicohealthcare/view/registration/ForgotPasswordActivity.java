package com.medicohealthcare.view.registration;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.medicohealthcare.view.home.ParentActivity;


/**
 * Created by Narendra on 17-02-2016.
 */
public class ForgotPasswordActivity extends ParentActivity
{
    public ForgetPassword forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.medicohealthcare.application.R.layout.activity_forget_password);

        forgetPassword = new ForgetPassword();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(com.medicohealthcare.application.R.id.lower_content, forgetPassword);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.medicohealthcare.application.R.menu.menu_main, menu);
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
