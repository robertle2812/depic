package com.frankemerald.depicfree.Listeners;

import android.graphics.Bitmap;
import android.view.View;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.R;

/**
 * Created by Mark on 13.10.2015.
 */
public class FrameBarClickListener implements View.OnClickListener {

    private MainActivity activity;
    private int numberOfFrame;
    final int FIRST_BLOCKED_FRAME=3;

    public FrameBarClickListener(MainActivity ctx, int i){
        activity = ctx;
        numberOfFrame = i;
    }

    @Override
    public void onClick(View v) {
        if(numberOfFrame>=FIRST_BLOCKED_FRAME){
            activity.showProposeDialog(7);
            return;
        }

        if(numberOfFrame == activity.currentFramework)
            return;
        for(int i = 0, k=0; i<activity.orignalBitmaps.size();i++){
            if(activity.orignalBitmaps.get(i) == null) k++;
            if(k==activity.orignalBitmaps.size())   {
                activity.setFramework(numberOfFrame);
                return;
            }
        }
        if(activity.findViewById(R.id.frame_first_layout) != null )
            activity.showChangeFrameworkDialog(numberOfFrame);
        else
            activity.setFramework(numberOfFrame);
    }
}
