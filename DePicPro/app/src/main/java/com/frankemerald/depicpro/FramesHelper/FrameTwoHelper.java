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
public class FrameTwoHelper {

    public static void prepareFrame(final MainActivity activity){
        RelativeLayout leftLayout = (RelativeLayout) activity.findViewById(R.id.leftLayout);
        RelativeLayout rightLayout = (RelativeLayout) activity.findViewById(R.id.rightLayout);
        leftLayout.setOnClickListener(new ImageClickListener(1, activity));
        rightLayout.setOnClickListener(new ImageClickListener(2, activity));

        TouchImageView imageLeft = (TouchImageView) activity.findViewById(R.id.leftImage);
        TouchImageView rightImage = (TouchImageView) activity.findViewById(R.id.rightImage);
        imageLeft.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.LEFT_CONTAINER));
        rightImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_CONTAINER));
        imageLeft.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.LEFT_CONTAINER));
        rightImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_CONTAINER));
     }
}
