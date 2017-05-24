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
        api =  ServerConnectionAdapter.getServerAdapter(getActivity()).getServerAPI();
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
    public Country[] getSupportedCountries()
    {
        Country[] countries = {};
        if(countriesList != null && !countriesList.isEmpty())
        {
            countries = new Country[countriesList.size()];
            countriesList.toArray(countries);
            return countries;
        }
        return countries;
    }

    public Country getDefaultCountry()
    {
        String countriIso = HomeActivity.parent_activity.personProfile.getPerson().getIsoCountry();
        for(Country country : countriesList)
        {
            if(country.getIso2().equalsIgnoreCase(countriIso))
                return country;
        }
        return countriesList.get(0);
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

    public int getCountryIndex(String isdCode)
    {
        if(countriesList != null && !countriesList.isEmpty() && isdCode != null && !isdCode.isEmpty())
        {
            for (int i = 0; i < countriesList.size(); i++)
            {
                if (countriesList.get(i).getIsdCode().equals(isdCode))
                    return i;
            }
        }
        return 0;
    }

}
