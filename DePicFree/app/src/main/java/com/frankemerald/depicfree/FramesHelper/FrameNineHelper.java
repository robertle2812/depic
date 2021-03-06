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
 * Created by Mark on 27.11.2015.
 */
public class FrameNineHelper {

    public static void prepareFrame(MainActivity activity){
        RelativeLayout topLayout = (RelativeLayout) activity.findViewById(R.id.topLayout);
        RelativeLayout bottomLayout = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
        RelativeLayout topCenterLayout = (RelativeLayout) activity.findViewById(R.id.topCenterLayout);
        RelativeLayout bottomCenterLayout = (RelativeLayout) activity.findViewById(R.id.bottomCenterLayout);
        topLayout.setOnClickListener(new ImageClickListener(UIHelper.TOP_CONTAINER, activity));
        bottomLayout.setOnClickListener(new ImageClickListener(UIHelper.BOTTOM_CONTAINER, activity));
        topCenterLayout.setOnClickListener(new ImageClickListener(UIHelper.TOP_CENTER_CONTAINER, activity));
        bottomCenterLayout.setOnClickListener(new ImageClickListener(UIHelper.BOTTOM_CENTER_LAYOUT, activity));

        TouchImageView topImage = (TouchImageView) activity.findViewById(R.id.topImage);
        TouchImageView bottomImage = (TouchImageView) activity.findViewById(R.id.bottomImage);
        TouchImageView topCenterImage = (TouchImageView) activity.findViewById(R.id.topCenterImage);
        TouchImageView bottomCenterImage = (TouchImageView) activity.findViewById(R.id.bottomCenterImage);
        topImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.TOP_CONTAINER));
        bottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.BOTTOM_CONTAINER));
        topCenterImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.TOP_CENTER_CONTAINER));
        bottomCenterImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.BOTTOM_CENTER_LAYOUT));
        topImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.TOP_CONTAINER));
        bottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.BOTTOM_CONTAINER));
        topCenterImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.TOP_CENTER_CONTAINER));
        bottomCenterImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.BOTTOM_CENTER_LAYOUT));
    }
}
