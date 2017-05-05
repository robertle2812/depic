package com.frankemerald.depicfree.Utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.frankemerald.depicfree.MainActivity;

/**
 * Created by Mark on 29.11.2015.
 */
public class AppRater {

    public static void goToRate(MainActivity activity){
        Uri uri = Uri.parse("market://details?id=com.skaya.viewearthlivehd");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.skaya.viewearthlivehd")));
        }
    }
}
