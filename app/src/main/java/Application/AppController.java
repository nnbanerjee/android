package Application;

/**
 * Created by MNT on 17-Feb-15.
 */

import android.app.Application;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayDeque;
import java.util.ArrayList;

import Model.ClinicAppointment;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    public static MyApi api;


    private String baseUrl = "";


    public static synchronized AppController getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
              .setClient(new OkClient()).build();

        api = restAdapter.create(MyApi.class);
    }
}
