package com.medico.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.medico.view.registration.Login;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

//import com.mindnerves.meidcaldiary.Fragments.Login;


public class MainActivity extends Activity
{
    public static MyApi api;
    public Login login;

    @Override
    public void onBackPressed() {

        // initialize variables
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Log.i("MainActivity","mainActviity->onbackpressed");

        Fragment f =  getFragmentManager().findFragmentById(com.medico.application.R.id.lower_content);

            // check to see if stack is empty
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            ft.commit();
        } else   if (f instanceof Login){
            finish();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(com.medico.application.R.string.confirm_logout);
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.medico.application.R.layout.activity_main);
        getMyAPI(this);
        login = new Login();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(com.medico.application.R.id.lower_content, new Login());
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.medico.application.R.menu.menu, menu);
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
    public static MyApi getMyAPI(Activity activity)
    {
        if(api == null)
        {
            CookieManager cookieManager = new CookieManager(null, null);
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setCookieHandler(cookieManager);
            Client client = new OkClient(okHttpClient);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(activity.getResources().getString(R.string.base_url))
                    .setClient(client).build();

            api = restAdapter.create(MyApi.class);
        }
        return api;
    }
}
