package com.medicohealthcare.application;

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

import com.medicohealthcare.util.PermissionManager;
import com.medicohealthcare.util.ServerConnectionAdapter;
import com.medicohealthcare.view.registration.Login;

//import com.mindnerves.meidcaldiary.Fragments.Login;


public class MainActivity extends Activity
{
    public MyApi api;
    public Login login;

    @Override
    public void onBackPressed() {

        // initialize variables
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Log.i("MainActivity","mainActviity->onbackpressed");

        Fragment f =  getFragmentManager().findFragmentById(com.medicohealthcare.application.R.id.lower_content);

            // check to see if stack is empty
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            ft.commit();
        } else   if (f instanceof Login){
            finish();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(com.medicohealthcare.application.R.string.confirm_logout);
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
        setContentView(com.medicohealthcare.application.R.layout.activity_main);
        api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
        login = new Login();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.lower_content, new Login());
        ft.addToBackStack(Login.class.getName());
        ft.commit();
        PermissionManager.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.medicohealthcare.application.R.menu.menu, menu);
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
