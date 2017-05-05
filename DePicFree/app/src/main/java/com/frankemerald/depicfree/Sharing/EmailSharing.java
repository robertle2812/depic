package com.frankemerald.depicfree.Sharing;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.frankemerald.depicfree.MainActivity;

/**
 * Created by Mark on 30.11.2015.
 */
public class EmailSharing {

    public static void shareApp(MainActivity activity){
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sapienti Sat");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.skaya.viewearthlivehd");
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        Intent mailer = Intent.createChooser(intent, null);
        try {
            activity.startActivity(mailer);
        }catch(Exception e){
            Toast.makeText(activity, "Email App is not installed", Toast.LENGTH_LONG).show();
        }
    }
}
