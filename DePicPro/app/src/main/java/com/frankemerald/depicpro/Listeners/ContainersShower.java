package com.frankemerald.depicpro.Listeners;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.UIHelper;
import com.frankemerald.depicpro.Views.TouchImageView;

/**
 * Created by Mark on 23.10.2015.
 */
public class ContainersShower implements View.OnClickListener {

    MainActivity activity;

    public ContainersShower(MainActivity activity){
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        LinearLayout secondsContainers = (LinearLayout) activity.findViewById(R.id.seconds_containers);
        if (secondsContainers == null)
            return;
        if(!activity.isOnlyBackgroundDownloaded())
            ((ImageView) activity.findViewById(UIHelper.imageViewsIdsBottomBar[1])).setAlpha(1f);
        activity.currentContainerSelected=-1;
        secondsContainers.setVisibility(View.VISIBLE);
        for (int i = 1; i < UIHelper.relLayoutsIds.length; i++) {
            RelativeLayout view = (RelativeLayout) activity.findViewById(UIHelper.relLayoutsIds[i]);
            if(view!=null) {
                if(((TouchImageView)view.getChildAt(1)).getDrawable()!=null){
                    view.setBackgroundColor(0x00000000);
                }
                else{
                    view.setBackgroundResource(R.drawable.carcas_second_inactive);
                }
            }
        }
        ((ImageView) activity.findViewById(R.id.btn_select_main)).setImageResource(R.drawable.btn_select_bkgr_off);
        activity.hideChangeBtn();
        activity.findViewById(UIHelper.relLayoutsIds[0]).setBackgroundResource(R.drawable.carcas_main_inactive);
        v.setOnClickListener(new ContainersHider(activity));
        if(activity.currentActivedBar!=4) {
            boolean existPhotoInSecondsContainer = false;
            for (int i = 1; i < UIHelper.imageViewsIds.length; i++) {
                TouchImageView t = (TouchImageView) activity.findViewById(UIHelper.imageViewsIds[i]);
                if (t != null && t.getDrawable() != null) {
                    activity.setCurrentContainer(i);
                    existPhotoInSecondsContainer = true;
                    break;
                }
            }
            if(!existPhotoInSecondsContainer && activity.currentActivedBar==1) {
                activity.setBar(0);
            }
        }

    }
}
