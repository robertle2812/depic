package com.frankemerald.depicpro.FramesHelper;

import android.widget.RelativeLayout;

import com.frankemerald.depicpro.Listeners.ImageClickListener;
import com.frankemerald.depicpro.Listeners.TouchImageViewClickListener;
import com.frankemerald.depicpro.Listeners.TouchImageViewDoubleTapListener;
import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.UIHelper;
import com.frankemerald.depicpro.Views.TouchImageView;

/**
 * Created by Mark on 23.10.2015.
 */
public class FrameFourHelper {

    public static void prepareFrame(MainActivity activity){
        RelativeLayout topLayout = (RelativeLayout) activity.findViewById(R.id.topLayout);
        RelativeLayout leftBottomLayout = (RelativeLayout) activity.findViewById(R.id.leftBottomLayout);
        RelativeLayout rightBottomLayout = (RelativeLayout) activity.findViewById(R.id.rightBottomLayout);
        topLayout.setOnClickListener(new ImageClickListener(UIHelper.TOP_CONTAINER, activity));
        leftBottomLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_BOTTOM_CONTAINER, activity));
        rightBottomLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_BOTTOM_CONTAINER, activity));

        TouchImageView topImage = (TouchImageView) activity.findViewById(R.id.topImage);
        TouchImageView leftBottomImage = (TouchImageView) activity.findViewById(R.id.leftBottomImage);
        TouchImageView rightBottomImage = (TouchImageView) activity.findViewById(R.id.rightBottomImage);
        topImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.TOP_CONTAINER));
        leftBottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.LEFT_BOTTOM_CONTAINER));
        rightBottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_BOTTOM_CONTAINER));
        topImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.TOP_CONTAINER));
        leftBottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.LEFT_BOTTOM_CONTAINER));
        rightBottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_BOTTOM_CONTAINER));
    }
}
