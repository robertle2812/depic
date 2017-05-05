package com.frankemerald.depicfree.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frankemerald.depicfree.ImagePicker.Action;
import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.R;
import com.frankemerald.depicfree.UIHelpers.UIHelper;

import java.io.File;

/**
 * Created by Mark on 04.12.2015.
 */
public class DialogImagePicker {

    private MainActivity context;
    private ViewGroup root;
    private Dialog alertDialog;
    private LinearLayout camera, single, multiply;
    private ImageView close;
    private boolean shown = false;
    OnOkClickListener okListener;
    int image;

    public DialogImagePicker(MainActivity context, int image){
        this.context = context;
        this.image = image;
    }

    private void createRootView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        root = (ViewGroup) inflater.inflate(R.layout.dialog_image_picker, null);
        camera = (LinearLayout) root.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File storage = Environment.getExternalStorageDirectory();
                Uri uri = Uri.fromFile(new File(storage, "temp.jpg"));
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                String pickTitle = "Take a photo";
                Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);
                context.startActivityForResult(chooserIntent, image);
                hide();
            }
        });
        multiply = (LinearLayout) root.findViewById(R.id.multiply);
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                int maxCount =0 ;
                if(context.orignalBitmaps.get(UIHelper.MAIN_CONTAINER)== null){
                    maxCount = context.containersCount;
                }
                else{
                    maxCount = context.containersCount-1;
                }
                if(context.currentContainerSelected==0){
                    maxCount = 1;
                }
                i.putExtra("containersCount",maxCount);
                context.startActivityForResult(i, 100+image);
                hide();
            }
        });
        close = (ImageView) root.findViewById(R.id.closeDialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    private void prepareDialog(){
        if(root!=null)
            return;
        createRootView();
    }

    public void show(){
        if(shown)
            return;
        prepareDialog();
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(root);
//        alertDialog = builder.create();
        alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setContentView(root);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        alertDialog.setContentView(root);
        alertDialog.show();
        shown = true;
    }

    public void hide(){
        if(!shown || alertDialog==null)
            return;
        alertDialog.hide();
    }

    public interface OnOkClickListener{
        public void onClick(String name);
    }

    public void SetOnItemViewClickListener(final OnOkClickListener mItemViewClickListener){
        this.okListener = mItemViewClickListener;
    }
}
