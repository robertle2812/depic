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
