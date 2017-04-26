package com.medico.view.home;

import android.app.Activity;
import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.Country;
import com.medico.model.ProfileId;
import com.medico.util.PARAM;
import com.medico.util.ServerConnectionAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Narendra on 27-01-2017.
 */
public class ParentFragment extends Fragment implements PARAM
{

    public MyApi api;
    public static List<Country> countriesList = null;
    public Fragment fragment;
    @Override
    public void onStart()
    {
        super.onStart();
        fragment = this;
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(getActivity());
                return false;
            }
        });
        api = api = ServerConnectionAdapter.getServerAdapter(getActivity()).getServerAPI();
        if(countriesList == null)
            loadSupportedCountryList();

    }
    public void onResume()
    {
        super.onResume();
    }

    private void loadSupportedCountryList()
    {
        api.getSupportedCountryList(new ProfileId(0), new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries, Response response) {
                if (countries != null && countries.size() > 0) {
                    countriesList =  countries;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<Country> getSupportedCountriesList()
    {
        return countriesList;
    }

    public boolean isChanged()
    {
        return false;
    }
    public boolean save()
    {
        return false;
    }
    public void update()   {    }
    public boolean canBeSaved()
    {
        return false;
    }
    public void setEditable(boolean editable)
    {
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void showBusy()
    {
        Activity activity = getActivity();
        if(activity instanceof ParentActivity)
            ((ParentActivity)getActivity()).showBusy();

    }

    public void hideBusy()
    {
        Activity activity = getActivity();
        if(activity instanceof ParentActivity)
        ((ParentActivity)getActivity()).hideBusy();

    }

}
