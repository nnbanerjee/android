package com.medicohealthcare.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Narendra on 15-02-2016.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap>
{
    private static Map<String, Bitmap> drawableMap;
    private String url;
    private ImageView imageView;

    public ImageLoadTask()
    {
        if (drawableMap == null)
            drawableMap = new HashMap<String, Bitmap>();
    }
    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
        if (drawableMap == null)
            drawableMap = new HashMap<String, Bitmap>();
    }

    public void execute()
    {
        if(this.drawableMap.get(url) != null)
        {
            imageView.setImageBitmap(this.drawableMap.get(url));
        }
        else
        {
            super.execute();
        }
    }

    public static synchronized void addImage(String url, Bitmap bitmap)
    {
        drawableMap.put(url, bitmap);
    }
    public static synchronized void removeImage(String url)
    {
        drawableMap.remove(url);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);//"https://s3-ap-southeast-1.amazonaws.com/medico-documents/101/10107+Nov,+2016");
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
        addImage(url, result);
    }

}