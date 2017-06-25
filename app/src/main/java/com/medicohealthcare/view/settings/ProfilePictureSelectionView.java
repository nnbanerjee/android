package com.medicohealthcare.view.settings;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.FileUpload1;
import com.medicohealthcare.model.ResponseAddDocuments;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

//import Model.PersonID;
//import Model.ResponseAddDocuments;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class ProfilePictureSelectionView extends ParentFragment
{

    Button browse;
    RadioButton browseDocument,browseImage, image_capture;
    ImageView preview_image,preview_image_selection,preview_image_final;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1889;
    public FileUpload1 fileupload = new FileUpload1();
    TypedFile typedFile;

    Bitmap bitmapimage = null;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_picture_upload, container, false);
        setHasOptionsMenu(true);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Capture");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        fileupload.date = new Date().getTime();
        browse = (Button)view.findViewById(R.id.image_previw) ;
        browseImage = (RadioButton)view.findViewById(R.id.image_browse);
        image_capture = (RadioButton)view.findViewById(R.id.image_capture);
        preview_image = (ImageView)view.findViewById(R.id.preview_image);
        preview_image_selection = (ImageView)view.findViewById(R.id.preview_image_selection);
        preview_image_final = (ImageView) view.findViewById(R.id.preview_image_final);
        browse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                    if(browseImage.isChecked())
                    {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, SELECT_PICTURE);
                    }

                    else if(image_capture.isChecked())
                    {
                        browse.setText("Capture");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
            }
        });
        browseImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                browse.setText("Browse");
                preview_image.setImageBitmap(null);
                preview_image_selection.setImageBitmap(null);
            }
        });
        image_capture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                browse.setText("Capture");
                preview_image.setImageBitmap(null);
                preview_image_selection.setImageBitmap(null);
            }
        });
        ImageManipulation listener = new ImageManipulation();
        preview_image.setOnTouchListener(listener);
        preview_image_selection.setOnClickListener(listener);

        return view;
    }







    public void saveFile(FileUpload1 fileupload)
    {
        showBusy();
        api.updateProfilePicture(fileupload.personId.toString(), fileupload.fileName, fileupload.file, new Callback<ResponseAddDocuments>()
        {
            @Override
            public void success(ResponseAddDocuments responseAddDocuments, Response response) {
                {

                    if (responseAddDocuments != null && responseAddDocuments.status.equals("1"))
                    {
                        // System.out.println("Url::::::::" + responseAddDocuments.getUrl());
                        Toast.makeText(getActivity(), "File Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        hideBusy();
                        Bundle bundle = getActivity().getIntent().getExtras();

                        ImageLoadTask.removeImage(bundle.getString(PROFILE_URL));

                        getActivity().onBackPressed();
                    } else
                    {
                        Toast.makeText(getActivity(), "File upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.upload, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.upload: {
                update();
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }
                return true;
            }

        }
        return false;
    }
    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    public boolean canBeSaved()
    {
        if(fileupload == null || fileupload.file == null || fileupload.personId == null || fileupload.fileName == null)
            return false;
        return true;
    }

    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        fileupload = new FileUpload1();
        fileupload.appointmentId = bundle.getInt(APPOINTMENT_ID);
        fileupload.personId = bundle.getInt(LOGGED_IN_ID);
        fileupload.patientId = bundle.getInt(PATIENT_ID);
        fileupload.file = this.typedFile;
        fileupload.fileName = bundle.getString(PROFILE_NAME);
        fileupload.type = browseImage.isChecked()? new Integer(0).byteValue():new Integer(1).byteValue();
        if (bitmapimage != null )
        {
            File file = saveInTemp(bitmapimage);
            typedFile = new TypedFile("application/octet-stream", file);
        }
    }
    @Override
    public boolean save()
    {
        saveFile(fileupload);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("in onActivityResult/////////////////////////////////");

        try
        {
            if (requestCode == SELECT_PICTURE)
            {
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
                {
                    ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                    Uri uri = data.getData();
                    String path = getPath(uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    image.setImageBitmap(bitmap);
                    Bitmap imagebitmap = Bitmap.createBitmap(bitmap,2,2,220,220,null,true);
                    imagebitmap = Bitmap.createScaledBitmap(imagebitmap,150,150, false);
                    bitmapimage = imagebitmap;
                    bitmapimage.setHasAlpha(false);

                }
            }
            else if (requestCode == CAMERA_REQUEST)
            {
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                Uri selectedImageUri = Uri.fromFile(picture);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                image.setImageBitmap(bitmap);
                Bitmap imagebitmap = Bitmap.createBitmap(bitmap,2,2,220,220,null,true);
                imagebitmap = Bitmap.createScaledBitmap(imagebitmap,150,150, false);
                bitmapimage = imagebitmap;
                bitmapimage.setHasAlpha(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public class ImageManipulation implements View.OnTouchListener, View.OnClickListener
    {

        // these matrices will be used to move and zoom image
        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();
        // we can be in one of these 3 states
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        private int mode = NONE;
        // remember some things for zooming
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float oldDist = 1f;
        private float d = 0f;
        private float newRot = 0f;
        private float[] lastEvent = null;
        private  Bitmap bmap;

        public boolean onTouch(View v, MotionEvent event)
        {
            // handle touch events here
            preview_image = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        matrix.postTranslate(dx, dy);
                    }
                    else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
//                        if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
//                            newRot = rotation(event);
//                            float r = newRot - d;
//                            float[] values = new float[9];
//                            matrix.getValues(values);
//                            float tx = values[2];
//                            float ty = values[5];
//                            float sx = values[0];
//                            float xc = (preview_image.getWidth() / 2) * sx;
//                            float yc = (preview_image.getHeight() / 2) * sx;
//                            matrix.postRotate(r, tx + xc, ty + yc);
//                        }
                    }
                    break;
               }

            preview_image.setImageMatrix(matrix);

            bmap= Bitmap.createBitmap(preview_image.getWidth(), preview_image.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bmap);
            preview_image.draw(canvas);
            preview_image_selection.setImageBitmap(null);

            Bitmap bitmap = Bitmap.createBitmap(bmap,2,2,222,222,matrix,true);
            bitmap = Bitmap.createScaledBitmap(bitmap,150,150, false);
            bitmap.setHasAlpha(false);
            bitmapimage = bitmap;

            return true;
        }


        public void ButtonClick(View v)
        {
            preview_image_selection.setImageBitmap(bmap);
        }
        /**
          * Determine the space between the first two fingers
          */
                private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            float s=x * x + y * y;
            return (float)Math.sqrt(s);
        }

        /**
          * Calculate the mid point of the first two fingers
          */
                private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        /**
          * Calculate the degree to be rotated by.
          *
          * @param event
          * @return Degrees
          */
                private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }
        @Override
        public void onClick(View view)
        {
            preview_image_selection.setImageMatrix(matrix);
            preview_image_selection.setImageBitmap(bitmapimage);
            if(preview_image.getVisibility() == View.GONE)
                preview_image.setVisibility(View.VISIBLE);
            else
                preview_image.setVisibility(View.GONE);
        }
    }
    private File saveInTemp(Bitmap bitmap)
    {
        OutputStream outStream = null;
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.png");
        try
        {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            outStream.flush();
            outStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if(outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch (Exception ew)
                {

                }

            }
        }
        return file;
    }

}
