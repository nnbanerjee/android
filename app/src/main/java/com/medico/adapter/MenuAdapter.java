package com.medico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.util.BackStress;
import com.medico.util.PARAM;
import com.medico.view.home.HomeActivity;
import com.medico.view.settings.ManagePersonSettings;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MNT on 11-Mar-15.
 */
public class MenuAdapter extends ParentAdapter{
    private Activity activity;
    private ArrayList<String> menus;
    private LayoutInflater inflater;
    private ImageView imageShow;

    private int type;
    private String imageUrl;
    SharedPreferences session;
    String id;

    public MenuAdapter(Activity activity,ArrayList<String> menus,int type,String imageUrl)
    {
        this.activity = activity;
        this.menus = menus;
        this.type = type;
        this.imageUrl = imageUrl;
    }


    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.main_menu, null);

        int pos = position;
        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id = session.getString("sessionID",null);
        imageShow = (ImageView)convertView.findViewById(R.id.image_show);
        TextView showTv = (TextView)convertView.findViewById(R.id.text_show);
        showTv.setText("" + menus.get(pos));
        Bundle bundle = new Bundle();
        setSettingParameters(bundle);
        final int loggedInProfileRole = bundle.getInt(LOGGED_IN_USER_ROLE);
        showTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSetting(position, loggedInProfileRole);
            }
        });

        int settingViewId = getViewId (position, loggedInProfileRole);
        switch (settingViewId)
        {
            case MANAGE_PROFILE_VIEW:
            {
                if (imageUrl != null)
                    new ImageLoadTask(this.activity.getResources().getString(R.string.image_base_url) + imageUrl, imageShow).execute();
                break;
            }
            case PATIENT_SETTING_VIEW:
            {
                imageShow.setImageResource(R.drawable.patient);
                break;
            }
            case CLINIC_SETTING_VIEW:
            {
                imageShow.setImageResource(R.drawable.clinic_default);
                break;
            }
            case ASSISTANT_SETTING_VIEW:
            {
                imageShow.setImageResource(R.drawable.assistant_default);
                break;
            }
            case DEPENDENT_SETTING_VIEW:
            {
                imageShow.setImageResource(R.drawable.settings_dependent);
                break;
            }
            case LOGOUT_CONFIRMATION:
            {
                imageShow.setImageResource(R.drawable.menu);
                break;
            }
        }
        return convertView;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
    protected void showSetting(int position,int role)
    {
        int settingViewId = getViewId (position, role);
        if(settingViewId == LOGOUT_CONFIRMATION)
        {
            logout();
        }
        else
        {
            Bundle bundle = new Bundle();
            setSettingParameters(bundle);
            bundle.putInt(PARAM.SETTING_VIEW_ID, settingViewId);
            Intent intObj = new Intent(activity, ManagePersonSettings.class);
            intObj.putExtras(bundle);
            activity.startActivity(intObj);
            //        activity.onPause();
        }
        ((HomeActivity)activity).dLayout.closeDrawer(((HomeActivity)activity).dList);
    }
    protected void setSettingParameters(Bundle bundle)
    {
        if(((HomeActivity)activity).parent != null)
        {
            bundle.putInt(PARAM.LOGGED_IN_ID, ((HomeActivity)activity).parent.getPerson().getId());
            bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, ((HomeActivity)activity).parent.getPerson().getRole());
            bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, ((HomeActivity)activity).parent.getPerson().getStatus());
        }
        else
        {
            bundle.putInt(PARAM.LOGGED_IN_ID, ((HomeActivity)activity).profileId);
            bundle.putInt(PARAM.LOGGED_IN_USER_ROLE,((HomeActivity)activity).profileRole);
            bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, ((HomeActivity)activity).profileStatus);
        }
        bundle.putInt(PARAM.PROFILE_ID, ((HomeActivity)activity).profileId);
        bundle.putInt(PARAM.PROFILE_ROLE, ((HomeActivity)activity).profileRole);
        bundle.putInt(PARAM.PROFILE_STATUS,((HomeActivity)activity).profileStatus);

    }

    protected void logout()
    {
//        dList.setSelection(position);
        ((HomeActivity)activity).dLayout.closeDrawer(((HomeActivity)activity).dList);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(R.string.confirm_logout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                BackStress.staticflag = 0;
                SharedPreferences sharedPref = ((HomeActivity)activity).getSharedPreferences(MyPREFERENCES, ((HomeActivity)activity).MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("USER_STATUS",false).commit();
                ((HomeActivity)activity).finish();

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

    private int getViewId(int position, int role)
    {
        if(role == DOCTOR)
        {
            switch (position)
            {
                case 0:
                    return MANAGE_PROFILE_VIEW;
                case 1:
                    return PATIENT_SETTING_VIEW;
                case 2:
                    return CLINIC_SETTING_VIEW;
                case 3:
                    return ASSISTANT_SETTING_VIEW;
                case 4:
                    return DEPENDENT_SETTING_VIEW;
                case 5:
                    return LOGOUT_CONFIRMATION;
            }
        }
        else
        {
            switch (position)
            {
                case 0:
                    return MANAGE_PROFILE_VIEW;
                case 1:
                    return DOCTOR_SETTING_VIEW;
                case 2:
                    return DEPENDENT_SETTING_VIEW;
                case 3:
                    return LOGOUT_CONFIRMATION;
            }

        }
        return 0;
    }
}
