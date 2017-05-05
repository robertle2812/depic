package com.frankemerald.depicpro.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.frankemerald.depicpro.R;

/**
 * Created by Mark on 28.09.2015.
 */
public class DialogSaveImage {

    private Context context;
    private ViewGroup root;
    private AlertDialog alertDialog;
    private TextView ok, cancel;
    EditText inputName;
    private boolean shown = false;
    OnOkClickListener okListener;
    String title_image;

    public DialogSaveImage(Context context, String title_image){
        this.context = context;
        this.title_image = title_image;
    }

    private void createRootView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        root = (ViewGroup) inflater.inflate(R.layout.dialog_save_image, null);
        inputName = (EditText) root.findViewById(R.id.nameInput);
        inputName.getText().append(title_image);
        ok = (TextView) root.findViewById(R.id.okButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(okListener!=null) {
                    String name = inputName.getText().toString();
                    if(name.length()>0) {
                        okListener.onClick(name);
                        hide();
                    }
                }
            }
        });
        cancel = (TextView) root.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(root);
        alertDialog = builder.create();
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
