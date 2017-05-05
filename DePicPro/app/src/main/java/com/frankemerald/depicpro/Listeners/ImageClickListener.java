package com.frankemerald.depicpro.Listeners;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.frankemerald.depicpro.Dialogs.DialogImagePicker;
import com.frankemerald.depicpro.Dialogs.DialogSaveImage;
import com.frankemerald.depicpro.ImagePicker.Action;
import com.frankemerald.depicpro.MainActivity;

import java.io.File;

/**
 * Created by Mark on 23.10.2015.
 */
public class ImageClickListener implements View.OnClickListener{

    MainActivity activity;
    int image;

    public ImageClickListener(int image, MainActivity activity){
        this.activity = activity;
        this.image =image;
    }

    @Override
    public void onClick(View v) {
        DialogImagePicker dialog = new DialogImagePicker(activity, image);
        dialog.show();
    }
}
