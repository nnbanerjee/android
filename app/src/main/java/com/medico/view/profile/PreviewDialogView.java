package com.medico.view.profile;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.medico.model.FileUpload1;
import com.medico.util.ImageLoadTask;
import com.medico.application.R;

/**
 * Created by MNT on 27-Mar-15.
 */
public class PreviewDialogView extends DialogFragment {

    ImageView viewImage;
    Button ok;
    FileUpload1 fileUpload1;

    public static PreviewDialogView newInstance() {
        return new PreviewDialogView();
    }

    public void setFile(FileUpload1 fileUpload1)
    {
        this.fileUpload1 = fileUpload1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.show_image_dialog,container,false);
        viewImage = (ImageView)view.findViewById(R.id.preview_image);
        new ImageLoadTask(fileUpload1.url,viewImage).execute();
        return view;
    }


}
