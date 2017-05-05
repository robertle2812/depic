package com.frankemerald.depicpro.Sharing;

import android.content.Intent;
import android.widget.Toast;

import com.frankemerald.depicpro.MainActivity;

/**
 * Created by Mark on 30.11.2015.
 */
public class SMSSharing {

    public static void shareApp(MainActivity activity){
        String smsBody="Sapienti Sat";
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        try {
            activity.startActivity(sendIntent);
        }catch(Exception e){
            Toast.makeText(activity, "SMS App is not installed", Toast.LENGTH_LONG).show();
        }
    }
}
