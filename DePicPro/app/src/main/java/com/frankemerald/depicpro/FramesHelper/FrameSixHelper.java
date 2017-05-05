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
public class FrameSixHelper {

    public static void prepareFrame(MainActivity activity){
        RelativeLayout leftTopLayout = (RelativeLayout) activity.findViewById(R.id.leftTopLayout);
        RelativeLayout leftBottomLayout = (RelativeLayout) activity.findViewById(R.id.leftBottomLayout);
        RelativeLayout rightTopLayout = (RelativeLayout) activity.findViewById(R.id.rightTopLayout);
        RelativeLayout rightBottomLayout = (RelativeLayout) activity.findViewById(R.id.rightBottomLayout);
        leftTopLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_TOP_CONTAINER, activity));
        leftBottomLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_BOTTOM_CONTAINER, activity));
        rightTopLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_TOP_CONTAINER, activity));
        rightBottomLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_BOTTOM_CONTAINER, activity));

        TouchImageView leftTopImage = (TouchImageView) activity.findViewById(R.id.leftTopImage);
        TouchImageView leftBottomImage = (TouchImageView) activity.findViewById(R.id.leftBottomImage);
        TouchImageView rightTopImage = (TouchImageView) activity.findViewById(R.id.rightTopImage);
        TouchImageView rightBottomImage = (TouchImageView) activity.findViewById(R.id.rightBottomImage);
        leftTopImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.LEFT_TOP_CONTAINER));
        leftBottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.LEFT_BOTTOM_CONTAINER));
        rightTopImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_TOP_CONTAINER));
        rightBottomImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_BOTTOM_CONTAINER));
        leftTopImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.LEFT_TOP_CONTAINER));
        leftBottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.LEFT_BOTTOM_CONTAINER));
        rightTopImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_TOP_CONTAINER));
        rightBottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_BOTTOM_CONTAINER));
    }
}
