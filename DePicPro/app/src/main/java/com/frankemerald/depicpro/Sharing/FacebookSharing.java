package com.frankemerald.depicpro.Sharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.Utils.SaveHelper;
import com.frankemerald.depicpro.Views.TouchImageView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mark on 28.11.2015.
 */
public class FacebookSharing {

    public static CallbackManager callbackManager;
    private static LoginManager manager;

    public static void sharePhotoWithoutSDK(MainActivity activity){
        String type = "image/*";
        String filename = "temp";
        String mediaPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"+filename+".jpg";

        SaveHelper.saveBitmap(activity, filename);

        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        if (intent != null)
        {

            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            share.setPackage("com.facebook.katana");

            // Set the MIME type
            share.setType(type);

            // Create the URI from the media

            File media = new File(mediaPath);
            Uri uri = Uri.fromFile(media);

            // Add the URI to the Intent.
            share.putExtra(Intent.EXTRA_STREAM, uri);

            // Broadcast the Intent.
            activity.startActivity(Intent.createChooser(share, "Share to"));

        }
        else
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.facebook.katana"));
            try {
                activity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(activity, "Facebook App is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void shareApp(final MainActivity activity) {

        FacebookSdk.sdkInitialize(activity);

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(activity, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.skaya.viewearthlivehd"))
                        .setContentTitle("Sapienti Sat")
                        .build();
                ShareDialog.show(activity, content);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });
    }

    public static void openGroup(MainActivity activity){
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/NASA")));
        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NASA")));
        }
    }

}
