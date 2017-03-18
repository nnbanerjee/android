package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

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
    public View getView(int position, View cv, ViewGroup parent) {

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
        System.out.println("menus.get(pos) = "+menus.get(pos));
        if(menus.get(pos).equals("Manage Profile"))
        {

//            if(imageUrl != null)
//            {
//                new ImageLoadTask(this.activity.getResources().getString(R.string.image_base_url) + imageUrl, imageShow).execute();
//
//            }
//            else
//            {
//                Bundle bundle = activity.getIntent().getExtras();
//                if (bundle.getInt(LOGGED_IN_USER_ROLE) == DOCTOR) {
//                    new ImageLoadTask(this.activity.getResources().getString(R.string.image_base_url) + imageUrl, imageShow).execute();
//               } else if (bundle.getInt(LOGGED_IN_USER_ROLE) == PATIENT) {
//                    new ImageLoadTask(this.activity.getResources().getString(R.string.image_base_url) + imageUrl, imageShow).execute();
//                }
//            }

        }
        else if(menus.get(pos).equals("Manage Doctor"))
        {
            imageShow.setBackgroundResource(R.drawable.doctor_image);
         }
        else if(menus.get(pos).equals("Manage Patient"))
        {
            imageShow.setBackgroundResource(R.drawable.patient);
        }
        else if(menus.get(pos).equals("Manage Clinic"))
        {
            imageShow.setBackgroundResource(R.drawable.clinic);
         }
        else if(menus.get(pos).equals("Manage Assistant"))
        {
            imageShow.setBackgroundResource(R.drawable.nurse);
        }
        else if(menus.get(pos).equals("Manage Dependency"))
        {
            imageShow.setBackgroundResource(R.drawable.depend);
         }
        else if(menus.get(pos).equals("Manage Reminder"))
        {
            imageShow.setBackgroundResource(R.drawable.medicine_reminder);
        }
        else if(menus.get(pos).equals("Manage Delegation"))
        {
            imageShow.setBackgroundResource(R.drawable.delegate);
        }
        else if(menus.get(pos).equals("Manage Template"))
        {
            imageShow.setBackgroundResource(R.drawable.templates);
        }
        else if (menus.get(pos).equals("Messages And Notification"))
        {
            imageShow.setBackgroundResource(R.drawable.msg);
         }
        else if (menus.get(pos).equals("Doctor Consultations")) {
            imageShow.setBackgroundResource(R.drawable.msg);
        }
        else if (menus.get(pos).equals("Logout")) {
            imageShow.setBackgroundResource(R.drawable.logout_menu);
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
}
