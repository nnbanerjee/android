
package com.medicohealthcare.util;

/*
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
*/


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.medicohealthcare.view.home.ParentActivity;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by keithlei on 3/16/15.
 */

public class ImageUtil {

    public static final String MINIBEAN_TEMP_DIR_NAME = "miniBean";

    public static final int SELECT_PICTURE = 1;

    public static final int PREVIEW_THUMBNAIL_MAX_WIDTH = 300;
    public static final int PREVIEW_THUMBNAIL_MAX_HEIGHT = 300;

    public static final int IMAGE_UPLOAD_MAX_WIDTH = 1024;
    public static final int IMAGE_UPLOAD_MAX_HEIGHT = 1024;

    public static final int PROFILE_IMAGE_UPLOAD_MAX_WIDTH = 100;
    public static final int PROFILE_IMAGE_UPLOAD_MAX_HEIGHT = 100;

    public static final int IMAGE_COMPRESS_QUALITY = 85;

    public static final String IMAGE_URL = "139.162.31.36:9000"+"/getFile/";

  public static DisplayImageOptions DEFAULT_IMAGE_OPTIONS =
            new DisplayImageOptions.Builder().
                    cacheInMemory(true).
                    cacheOnDisk(true).
                    bitmapConfig(Bitmap.Config.RGB_565).
                    imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

    public static DisplayImageOptions ROUNDED_CORNERS_IMAGE_OPTIONS =
            new DisplayImageOptions.Builder().
                    cacheInMemory(true).
                    cacheOnDisk(true).
                    bitmapConfig(Bitmap.Config.RGB_565).
                    imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

    public static DisplayImageOptions ROUND_IMAGE_OPTIONS =
            new DisplayImageOptions.Builder().
                    cacheInMemory(true).
                    cacheOnDisk(true).
                    bitmapConfig(Bitmap.Config.RGB_565).
                    imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

    private static ImageLoader mImageLoader;

    private static File tempDir;

    static {
        init();
    }

    public static synchronized ImageLoader getImageLoader() {
        return mImageLoader;
    }

    private ImageUtil() {}

    public static void init() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                ParentActivity.activity.getApplicationContext()).
                threadPoolSize(5).
                threadPriority(Thread.MIN_PRIORITY + 3).
                denyCacheImageMultipleSizesInMemory().
                memoryCache(new WeakMemoryCache()).
                defaultDisplayImageOptions(DEFAULT_IMAGE_OPTIONS).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();

        initImageTempDir();
    }

    // miniBean temp directory

    public static void initImageTempDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalRoot = Environment.getExternalStorageDirectory();
            Log.d(ImageUtil.class.getSimpleName(), "initImageTempDir: externalRoot="+externalRoot.getAbsolutePath());

            tempDir = new File(externalRoot, MINIBEAN_TEMP_DIR_NAME);
            if (!tempDir.exists()) {
                tempDir.mkdir();
                Log.d(ImageUtil.class.getSimpleName(), "initImageTempDir: create tempDir=" + tempDir.getAbsolutePath());
            } else {
                clearTempDir();
            }
        } else {
            Log.e(ImageUtil.class.getSimpleName(), "initImageTempDir: no external storage!!!");
            tempDir = null;
        }
    }

    public static File getTempDir() {
        return tempDir;
    }

    private static void clearTempDir() {
        if (tempDir != null && tempDir.exists()) {
            File[] children = tempDir.listFiles();
            for (File f : children) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }
        }
    }

    public static void displayImageFile(Long id,ImageView view)
    {
        getImageLoader().displayImage(IMAGE_URL+id,view);
    }
    // Community cover image


    private static void clearImageCache(String url) {
        File imageFile = mImageLoader.getDiskCache().get(url);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        DiskCacheUtils.removeFromCache(url, mImageLoader.getDiskCache());
        MemoryCacheUtils.removeFromCache(url, mImageLoader.getMemoryCache());
    }

    // Select photo

     public static void openPhotoPicker(Activity activity) {
        openPhotoPicker(activity, "Select Picture");//activity.getString(R.string.photo_select));
    }


    public static void openPhotoPicker(Activity activity, String title) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //intent.setType("image*//**//**//**//*//**//**//**//*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, title), SELECT_PICTURE);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                return filePath;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public static Bitmap resizeAsPreviewThumbnail(String path) {
        return resizeImage(path, PREVIEW_THUMBNAIL_MAX_WIDTH, PREVIEW_THUMBNAIL_MAX_HEIGHT);
    }

    public static Bitmap resizeToUpload(String path) {
        return resizeImage(path, IMAGE_UPLOAD_MAX_WIDTH, IMAGE_UPLOAD_MAX_HEIGHT);
    }

    public static Bitmap resizeProfilePictureToUpload(String path) {
        return resizeImage(path, PROFILE_IMAGE_UPLOAD_MAX_WIDTH, PROFILE_IMAGE_UPLOAD_MAX_HEIGHT);
    }

    public static Bitmap resizeImage(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        int originalHeight = opts.outHeight;
        int originalWidth = opts.outWidth;
        int resizeScale = 1;

        Log.d(ImageUtil.class.getSimpleName(), "resizeImage: outWidth="+originalWidth+" outHeight="+originalHeight);
        if ( originalWidth > maxWidth || originalHeight > maxHeight ) {
            final int widthRatio = Math.round((float) originalWidth / (float) maxWidth);
            final int heightRatio = Math.round((float) originalHeight / (float) maxHeight);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
            Log.d(ImageUtil.class.getSimpleName(), "resizeImage: resizeScale="+resizeScale);
        }

        // put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;


        int bmSize = (originalWidth / resizeScale) * (originalHeight / resizeScale) * 4;
        if ( Runtime.getRuntime().freeMemory() > bmSize ) {
            bp = BitmapFactory.decodeFile(path, opts);
        } else {
            return null;
        }



        bp = BitmapFactory.decodeFile(path, opts);
        return bp;
    }

    public static File resizeAsPNG(File image) {
        return resizeAsFormat(Bitmap.CompressFormat.PNG, image);
    }

    public static File resizeAsJPG(File image) {
        return resizeAsFormat(Bitmap.CompressFormat.JPEG, image);
    }

    public static File resizeAsFormatAndSize(Bitmap.CompressFormat format, File image, int width, int height)
    {
        if (tempDir == null) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: tempDir is null!!!");
            return image;
        }

        File resizedImage = new File(tempDir, image.getName());
        if (!tempDir.canWrite()) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: "+tempDir.getAbsolutePath()+" cannot be written!!!");
            return image;
        }

        try {
            FileOutputStream out = new FileOutputStream(resizedImage);
            Bitmap resizedBitmap = ImageUtil.resizeImage(image.getAbsolutePath(),width,height);
            resizedBitmap.compress(format, IMAGE_COMPRESS_QUALITY, out);
            Log.d(ImageUtil.class.getSimpleName(), "resizeAsFormat: successfully resized to path=" + resizedImage.getAbsolutePath());
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception e) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: " + e.getMessage(), e);
        }

        return resizedImage;
    }

    public static File cropAndResizeAsFormatAndSize(Bitmap.CompressFormat format, String imagepath, int width, int height)
    {
        File picture = new File(Environment.getExternalStorageDirectory()+"/");
        Bitmap b= BitmapFactory.decodeFile(imagepath);
        Bitmap out = cropToSquare(b);
        out = Bitmap.createScaledBitmap(b, width, width, false);
        File file = new File(picture, "resize.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(format, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}

        return file;
    }

    public static File resizeAsFormat(Bitmap.CompressFormat format, File image)
    {
        if (tempDir == null) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: tempDir is null!!!");
            return image;
        }

        File resizedImage = new File(tempDir, image.getName());
        if (!tempDir.canWrite()) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: "+tempDir.getAbsolutePath()+" cannot be written!!!");
            return image;
        }

        try {
            FileOutputStream out = new FileOutputStream(resizedImage);
            Bitmap resizedBitmap = ImageUtil.resizeToUpload(image.getAbsolutePath());
            resizedBitmap.compress(format, IMAGE_COMPRESS_QUALITY, out);
            Log.d(ImageUtil.class.getSimpleName(), "resizeAsFormat: successfully resized to path=" + resizedImage.getAbsolutePath());
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception e) {
            Log.e(ImageUtil.class.getSimpleName(), "resizeAsFormat: " + e.getMessage(), e);
        }

        return resizedImage;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        return cropToSquare(bitmap, -1);
    }

    public static Bitmap getProfilePicture(String path)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);
        Bitmap bitmap = cropToSquare(bp);
        Bitmap bitmap1 = bitmap.createScaledBitmap(bitmap,100,100,true);

        return bitmap1;
    }

    public static Bitmap cropToSquare(Bitmap bitmap, int dimension) {
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int crop = (width - height) / 2;
        crop = (crop < 0)? 0: crop;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, crop, 0, newWidth, newHeight);

        if (dimension != -1)
            cropImg = Bitmap.createScaledBitmap(cropImg, dimension, dimension, false);
        return cropImg;
    }


//  public static Drawable getEmptyDrawable() {
//        LevelListDrawable d = new LevelListDrawable();
//        Drawable empty = AppController.getInstance().getResources().getDrawable(R.drawable.);
//        d.addLevel(0, 0, empty);
//        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
//        return d;
//    }

    public static Bitmap cropAndResizeImage(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        int originalHeight = opts.outHeight;
        int originalWidth = opts.outWidth;
        int resizeScale = 1;

        Log.d(ImageUtil.class.getSimpleName(), "resizeImage: outWidth="+originalWidth+" outHeight="+originalHeight);
        if ( originalWidth > maxWidth || originalHeight > maxHeight ) {
            final int widthRatio = Math.round((float) originalWidth / (float) maxWidth);
            final int heightRatio = Math.round((float) originalHeight / (float) maxHeight);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
            Log.d(ImageUtil.class.getSimpleName(), "resizeImage: resizeScale="+resizeScale);
        }

        // put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;


        int bmSize = (originalWidth / resizeScale) * (originalHeight / resizeScale) * 4;
        if ( Runtime.getRuntime().freeMemory() > bmSize ) {
            bp = BitmapFactory.decodeFile(path, opts);
        } else {
            return null;
        }



        bp = BitmapFactory.decodeFile(path, opts);
        return cropToSquare(bp);
    }
}

