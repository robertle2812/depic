package com.frankemerald.depicpro.UIHelpers;

import android.view.View;
import android.widget.LinearLayout;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.Sharing.FacebookSharing;
import com.frankemerald.depicpro.Sharing.InstagramSharing;
import com.frankemerald.depicpro.Sharing.TumblrSharing;
import com.frankemerald.depicpro.Sharing.TwitterSharing;
import com.frankemerald.depicpro.Utils.SaveHelper;
import com.frankemerald.depicpro.Views.TouchImageView;

/**
 * Created by Mark on 27.11.2015.
 */
public class ShareBarHelper {


    public static void init(final MainActivity activity) {
        LinearLayout shareL = (LinearLayout) activity.findViewById(R.id.social_layout);
        shareL.setVisibility(View.INVISIBLE);
        for (int i = 0; i < shareL.getChildCount(); i++) {
            shareL.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSocialAction(activity, v.getId());
                }
            });
        }
    }

    private static void setSocialAction(MainActivity activity, int id) {
            switch (id) {
                case R.id.save:
                    SaveHelper.showChooseDialog(activity);
                    break;
                case R.id.facebook:
                    FacebookSharing.sharePhotoWithoutSDK(activity);
                    break;
                case R.id.instagram:
                    InstagramSharing.sharePhoto(activity);
                    break;
                case R.id.tumblr:
                    TumblrSharing.sharePhoto(activity);
                    break;
                case R.id.twitter:
                    TwitterSharing.sharePhoto(activity);
                    break;
            }
    }
}
