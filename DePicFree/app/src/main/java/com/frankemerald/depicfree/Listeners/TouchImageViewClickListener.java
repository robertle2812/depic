package com.frankemerald.depicfree.Listeners;

import android.view.View;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.Views.TouchImageView;

/**
 * Created by Mark on 23.10.2015.
 */
public class TouchImageViewClickListener implements View.OnClickListener{

    int image, singleClick, doubleClick;
    MainActivity activity;

    public TouchImageViewClickListener(MainActivity activity, int image){
        this.activity = activity;
        this.image = image;
    }

    @Override
    public void onClick(View v) {
        if (((TouchImageView)v).getDrawable() != null && activity.clickImage++ ==1) {
            activity.clickImage=0;
            if(image!=activity.currentContainerSelected) {
                activity.setCurrentContainer(image);
            }
            else{
                if(activity.currentActivedBar==0|| activity.currentActivedBar==3)
                    activity.deactivateCurrentContainer(image);
            }
        }
    }
}