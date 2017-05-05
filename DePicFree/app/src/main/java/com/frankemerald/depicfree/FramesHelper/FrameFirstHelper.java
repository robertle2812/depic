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
