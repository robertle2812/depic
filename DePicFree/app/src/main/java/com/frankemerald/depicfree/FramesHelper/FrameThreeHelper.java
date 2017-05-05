package com.frankemerald.depicfree.FramesHelper;

import android.widget.RelativeLayout;

import com.frankemerald.depicfree.Listeners.ImageClickListener;
import com.frankemerald.depicfree.Listeners.TouchImageViewClickListener;
import com.frankemerald.depicfree.Listeners.TouchImageViewDoubleTapListener;
import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.R;
import com.frankemerald.depicfree.UIHelpers.UIHelper;
import com.frankemerald.depicfree.Views.TouchImageView;

/**
 * Created by Mark on 23.10.2015.
 */
public class FrameThreeHelper {

    public static void prepareFrame(MainActivity activity){
        RelativeLayout topLayout = (RelativeLayout) activity.findViewById(R.id.topLayout);
        RelativeLayout bottomlayout = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
        topLayout.setOnClickListener(new ImageClickListener(UIHelper.TOP_CONTAINER, activity));
        bottomlayout.setOnClickListener(new ImageClickListener(UIHelper.BOTTOM_CONTAINER, activity));

        TouchImageView topImage = (TouchImageView) activity.findViewById(R.id.topImage);
        TouchImageView bottomImage = (TouchImageView) activity.findViewById(R.id.bottomImage);
        topImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.TOP_CONTAINER));
        bottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.BOTTOM_CONTAINER));
        topImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.TOP_CONTAINER));
        bottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.BOTTOM_CONTAINER));
    }
}
