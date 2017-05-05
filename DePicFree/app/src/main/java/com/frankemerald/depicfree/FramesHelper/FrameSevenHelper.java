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
public class FrameSevenHelper {

    public static void prepareFrame(MainActivity activity){
        RelativeLayout leftLayout = (RelativeLayout) activity.findViewById(R.id.leftLayout);
        RelativeLayout rightLayout = (RelativeLayout) activity.findViewById(R.id.rightLayout);
        RelativeLayout leftCenterLayout = (RelativeLayout) activity.findViewById(R.id.leftCenterLayout);
        RelativeLayout rightCenterLayout = (RelativeLayout) activity.findViewById(R.id.rightCenterLayout);
        leftLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_CONTAINER, activity));
        rightLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_CONTAINER, activity));
        leftCenterLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_CENTER_CONTAINER, activity));
        rightCenterLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_CENTER_LAYOUT, activity));

        TouchImageView leftImage = (TouchImageView) activity.findViewById(R.id.leftImage);
        TouchImageView rightImage = (TouchImageView) activity.findViewById(R.id.rightImage);
        TouchImageView leftCenterImage = (TouchImageView) activity.findViewById(R.id.leftCenterImage);
        TouchImageView rightCenterImage = (TouchImageView) activity.findViewById(R.id.rightCenterImage);
        leftImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.LEFT_CONTAINER));
        rightImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_CONTAINER));
        leftCenterImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.LEFT_CENTER_CONTAINER));
        rightCenterImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_CENTER_LAYOUT));
        leftImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.LEFT_CONTAINER));
        rightImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_CONTAINER));
        leftCenterImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.LEFT_CENTER_CONTAINER));
        rightCenterImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_CENTER_LAYOUT));
    }
}
