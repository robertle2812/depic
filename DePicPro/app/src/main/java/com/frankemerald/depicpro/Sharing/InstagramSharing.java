package com.frankemerald.depicpro.Sharing;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.Utils.SaveHelper;

import java.io.File;

/**
 * Created by Mark on 28.11.2015.
 */
public class InstagramSharing {

    public static void sharePhoto(MainActivity activity){

        String type = "image/*";
        String filename = "temp";
        String mediaPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"+filename+".jpg";

        SaveHelper.saveBitmap(activity, filename);

        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {

            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            share.setPackage("com.instagram.android");

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
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            try {
                activity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(activity, "Instagram App is not installed", Toast.LENGTH_LONG).show();
            }
        }

    }

    public static void shareApp(MainActivity activity){
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {

            Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + "/" + R.raw.icon);;
            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            share.setPackage("com.instagram.android");

            // Set the MIME type
            share.setType("image/*");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            share.putExtra(Intent.EXTRA_TEXT, "https://www.codeofaninja.com");

            activity.startActivity(Intent.createChooser(share, "Share to"));

        }
        else
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            try {
                activity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(activity, "Instagram App is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void openGroup(MainActivity activity){
        Uri uri = Uri.parse("http://instagram.com/depicapp");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            activity.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/depicapp")));
        }
    }
}
