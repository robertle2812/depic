package com.frankemerald.depicpro.FramesHelper;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.frankemerald.depicpro.Listeners.ImageClickListener;
import com.frankemerald.depicpro.Listeners.TouchImageViewClickListener;
import com.frankemerald.depicpro.Listeners.TouchImageViewDoubleTapListener;
import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.UIHelper;
import com.frankemerald.depicpro.Views.TouchImageView;

/**
 * Created by Mark on 27.11.2015.
 */
public class FrameEightHelper {

    public static void prepareFrame(final MainActivity activity){
        RelativeLayout bottomLayout = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
        RelativeLayout leftTopLayout = (RelativeLayout) activity.findViewById(R.id.leftTopLayout);
        RelativeLayout rightTopLayout = (RelativeLayout) activity.findViewById(R.id.rightTopLayout);
        bottomLayout.setOnClickListener(new ImageClickListener(UIHelper.BOTTOM_CONTAINER, activity));
        leftTopLayout.setOnClickListener(new ImageClickListener(UIHelper.LEFT_TOP_CONTAINER, activity));
        rightTopLayout.setOnClickListener(new ImageClickListener(UIHelper.RIGHT_TOP_CONTAINER, activity));

        TouchImageView bottomImage = (TouchImageView) activity.findViewById(R.id.bottomImage);
        TouchImageView leftTopImage = (TouchImageView) activity.findViewById(R.id.leftTopImage);
        TouchImageView rightTopImage = (TouchImageView) activity.findViewById(R.id.rightTopImage);
        bottomImage.setOnClickListener(new TouchImageViewClickListener(activity,UIHelper.BOTTOM_CONTAINER));
        leftTopImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.LEFT_TOP_CONTAINER));
        rightTopImage.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.RIGHT_TOP_CONTAINER));
        bottomImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity,UIHelper.BOTTOM_CONTAINER));
        leftTopImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.LEFT_TOP_CONTAINER));
        rightTopImage.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.RIGHT_TOP_CONTAINER));
    }
}
