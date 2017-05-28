package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.util.BackStress;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.HomeActivity;
import com.medicohealthcare.view.settings.ManagePersonSettings;

import java.util.ArrayList;

/**
 * Created by MNT on 11-Mar-15.
 */
public class MenuAdapter extends ParentAdapter{
    private Activity activity;
    private ArrayList<String> menus;
    private LayoutInflater inflater;
    private int type;
    private String imageUrl;

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
        ImageView person_image = (ImageView)convertView.findViewById(R.id.person_image);
        TextView showTv = (TextView)convertView.findViewById(R.id.text_show);
        RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.setting_menu);
        showTv.setText("" + menus.get(pos));
        Bundle bundle = new Bundle();
        setSettingParameters(bundle);
        final int loggedInId = bundle.getInt(LOGGED_IN_ID);
        final int profileId = bundle.getInt(PROFILE_ID);
        final int loggedInProfileRole = bundle.getInt(LOGGED_IN_USER_ROLE);
        final int profileRole = bundle.getInt(PROFILE_ROLE);
        final boolean isDependent = bundle.getBoolean(IS_DEPENDENT);
        final boolean isDelegate = bundle.getBoolean(IS_DEPENDENT)==false && loggedInId !=profileId?true:false ;
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSetting(position, profileRole,isDependent,isDelegate);
            }
        });


        int settingViewId = getViewId (position, profileRole,isDependent,isDelegate);
        switch (settingViewId)
        {
            case MANAGE_PROFILE_VIEW:
            {
                if(loggedInProfileRole == PATIENT)
                    person_image.setImageResource(R.drawable.patient_default);
                else if(loggedInProfileRole == DOCTOR)
                    person_image.setImageResource(R.drawable.doctor_default);
                else
                    person_image.setImageResource(R.drawable.assistant_default);

                if (imageUrl != null)
                    new ImageLoadTask( imageUrl, person_image).execute();

                break;
            }
            case PATIENT_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.patient_default);
                break;
            }
            case CLINIC_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.clinic_default);
                break;
            }
            case ASSISTANT_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.assistant_default);
                break;
            }
            case DEPENDENT_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.patient_default);
                break;
            }
            case DELEGATE_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.patient_default);
                break;
            }
            case DOCTOR_SETTING_VIEW:
            {
                person_image.setImageResource(R.drawable.doctor_default);
                break;
            }
            case LOGOUT_CONFIRMATION:
            {
                person_image.setImageResource(R.drawable.settings_logout);
                break;
            }
        }
        return convertView;
    }

    public void showSetting(int position,int role, boolean isDependent, boolean isDelegate)
    {
        int settingViewId = getViewId (position, role, isDependent, isDelegate);
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
            bundle.putBoolean(PARAM.IS_DEPENDENT, ((HomeActivity)activity).isDependent);
        }
        else
        {
            bundle.putInt(PARAM.LOGGED_IN_ID, ((HomeActivity)activity).profileId);
            bundle.putInt(PARAM.LOGGED_IN_USER_ROLE,((HomeActivity)activity).profileRole);
            bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, ((HomeActivity)activity).profileStatus);
            bundle.putBoolean(PARAM.IS_DEPENDENT, false);
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

    private int getViewId(int position, int role, boolean isDependent, boolean isDelegation)
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
            if(isDependent)
            {
                switch (position)
                {
                    case 0:
                        return DOCTOR_SETTING_VIEW;
                    case 1:
                        return DELEGATE_SETTING_VIEW;
                    case 2:
                        return LOGOUT_CONFIRMATION;
                }

            }
            else if(isDelegation)
            {
                switch (position)
                {
                    case 0:
                        return DOCTOR_SETTING_VIEW;
                    case 1:
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
                        return DELEGATE_SETTING_VIEW;
                    case 4:
                        return LOGOUT_CONFIRMATION;
                }
            }

        }
        return 0;
    }
}
