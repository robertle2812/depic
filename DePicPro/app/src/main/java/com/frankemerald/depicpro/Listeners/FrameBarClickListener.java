package com.frankemerald.depicpro.Listeners;

import android.graphics.Bitmap;
import android.view.View;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;

/**
 * Created by Mark on 13.10.2015.
 */
public class FrameBarClickListener implements View.OnClickListener {

    private MainActivity activity;
    private int numberOfFrame;

    public FrameBarClickListener(MainActivity ctx, int i){
        activity = ctx;
        numberOfFrame = i;
    }

    @Override
    public void onClick(View v) {
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
