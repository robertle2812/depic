package com.frankemerald.depicfree.Sharing;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.Utils.SaveHelper;

import java.io.File;

/**
 * Created by Mark on 29.11.2015.
 */
public class TwitterSharing {

    public static void sharePhoto(MainActivity activity){

        String type = "image/*";
        String filename = "temp";
        String mediaPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"+filename+".jpg";

        SaveHelper.saveBitmap(activity, filename);

        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.twitter.android");
        if (intent != null)
        {

            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            share.setPackage("com.twitter.android");

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
            intent.setData(Uri.parse("market://details?id=" + "com.twitter.android"));
            try {
                activity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(activity, "Twitter App is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void shareApp(MainActivity activity){
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.twitter.android");
        if (intent != null)
        {

            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            share.setPackage("com.twitter.android");

            // Set the MIME type
            share.setType("text/*");

            share.putExtra(Intent.EXTRA_SUBJECT, "Sapienti Sat");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.skaya.viewearthlivehd");

            // Broadcast the Intent.
            activity.startActivity(Intent.createChooser(share, "Share to"));

        }
        else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.twitter.android"));
            try {
                activity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(activity, "Twitter App is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
