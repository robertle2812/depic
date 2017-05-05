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
 * Created by Mark on 15.12.2015.
 */
public class FrameFirstHelper {

    public static void prepareFrame(final MainActivity activity){
        RelativeLayout fullLayout = (RelativeLayout) activity.findViewById(R.id.fullLayout);
        fullLayout.setOnClickListener(new ImageClickListener(UIHelper.FULL_LAYOUT, activity));

        TouchImageView imagefull = (TouchImageView) activity.findViewById(R.id.fullImage);
        imagefull.setOnClickListener(new TouchImageViewClickListener(activity, UIHelper.FULL_LAYOUT));
        imagefull.setOnDoubleTapListener(new TouchImageViewDoubleTapListener(activity, UIHelper.FULL_LAYOUT));
    }
}
