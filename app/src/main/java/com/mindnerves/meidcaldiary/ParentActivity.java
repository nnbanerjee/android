package com.mindnerves.meidcaldiary;

import android.app.Activity;
import android.content.SharedPreferences;

import Utils.PARAM;

/**
 * Created by Narendra on 17-01-2017.
 */

public abstract class ParentActivity extends Activity implements PARAM
{

    protected int profileRole = PATIENT;
    protected int profileId = 0;
    protected int profileStatus = UNREGISTERED;

    protected SharedPreferences session = null;
}
