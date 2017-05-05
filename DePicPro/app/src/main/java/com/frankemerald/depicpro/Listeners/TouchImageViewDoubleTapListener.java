package com.frankemerald.depicpro.Listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.UIHelpers.UIHelper;

/**
 * Created by Mark on 16.12.2015.
 */
public class TouchImageViewDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    int image;
    MainActivity activity;

    public TouchImageViewDoubleTapListener(MainActivity activity, int image){
        this.activity = activity;
        this.image = image;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(activity.currentContainerSelected!=-1&& activity.currentContainerSelected!=image)
            activity.deactivateCurrentContainer(activity.currentContainerSelected);
        activity.setCurrentContainer(image);
        activity.currentContainerSelected=-1;
        activity.setCurrentContainer(image);
        activity.replacePic.callOnClick();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
