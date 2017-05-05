package com.frankemerald.depicfree.UIHelpers;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.R;
import com.frankemerald.depicfree.Sharing.EmailSharing;
import com.frankemerald.depicfree.Sharing.FacebookSharing;
import com.frankemerald.depicfree.Sharing.InstagramSharing;
import com.frankemerald.depicfree.Sharing.SMSSharing;
import com.frankemerald.depicfree.Sharing.TumblrSharing;
import com.frankemerald.depicfree.Sharing.TwitterSharing;
import com.frankemerald.depicfree.Utils.AppRater;
import com.frankemerald.depicfree.Utils.Utils;

/**
 * Created by Mark on 29.11.2015.
 */
public class MenuHelper {

    public static boolean groupMenuIsOpen = true;
    public static void init(final MainActivity activity){

        LinearLayout menuL = (LinearLayout) activity.findViewById(R.id.leftMenuBar);

        for (int i = 0; i < menuL.getChildCount()-2; i++) {
            menuL.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionMenu(activity, v.getId());
                }
            });
        }

        activity.findViewById(R.id.facebook_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu(activity, v.getId());
            }
        });
        activity.findViewById(R.id.instagram_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu(activity , v.getId());
            }
        });

        LinearLayout shareMenuSection = (LinearLayout) activity.findViewById(R.id.shareMenuSection);

        for (int i = 0; i < shareMenuSection.getChildCount(); i++) {
            shareMenuSection.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionMenu(activity, v.getId());
                }
            });
        }

    }

    public static void actionMenu(MainActivity activity, int id){
        switch (id){
            case R.id.rate_us:
                AppRater.goToRate(activity);
                break;
            case R.id.instagram_share_left:
                InstagramSharing.shareApp(activity);
                break;
            case R.id.facebook_share_left:
                FacebookSharing.shareApp(activity);
                break;
            case R.id.twitter_share_left:
                TwitterSharing.shareApp(activity);
                break;
            case R.id.tumblr_share_left:
                TumblrSharing.shareApp(activity);
                break;
            case R.id.mms_share_left:
                SMSSharing.shareApp(activity);
                break;
            case R.id.email_share_left:
                EmailSharing.shareApp(activity);
                break;
            case R.id.group_social:
                animateGroupMenu(activity);
                break;
            case R.id.facebook_group:
                FacebookSharing.openGroup(activity);
                break;
            case R.id.instagram_group:
                InstagramSharing.openGroup(activity);
                break;
        }
    }

    public static void animateGroupMenu(MainActivity activity){
        if(groupMenuIsOpen){
            animateLayout(activity, true);
            groupMenuIsOpen = false;
            ((ImageView)activity.findViewById(R.id.group_social)).setImageResource(R.drawable.btn_group_app_on);
            if(activity.sectionShareIsOpen)
                activity.animateLeftSectionShare();
        }
        else{
            animateLayout(activity, false);
            groupMenuIsOpen = true;
            ((ImageView)activity.findViewById(R.id.group_social)).setImageResource(R.drawable.btn_group_app);
        }
    }

    private static void animateLayout(final MainActivity activity, final boolean open){
        ValueAnimator va ;
        if(open)
            va= ValueAnimator.ofInt(0,108);
        else
            va= ValueAnimator.ofInt(108,0);
        va.setDuration(150);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("depic", "value animator, getAnimatedvalue = "+(Integer) animation.getAnimatedValue());
                activity.group_container.getLayoutParams().height= (int) Utils.convertDpToPixel((Integer) animation.getAnimatedValue());
                activity.group_container.requestLayout();
            }
        });
        va.start();
    }
}
