package com.frankemerald.depicpro.Listeners;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.FilterBarHelper;
import com.frankemerald.depicpro.UIHelpers.UIHelper;

/**
 * Created by Mark on 23.10.2015.
 */
public class ContainersHider implements View.OnClickListener{

    MainActivity activity;
    public ContainersHider(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if(activity.currentActivedBar==1){
            activity.hideChangeBtn();
            activity.showProposeDialog(4);
        }

        ((ImageView) activity.findViewById(UIHelper.imageViewsIdsBottomBar[1])).setAlpha(0.6f);

        if(activity.currentActivedBar!=1) {
            activity.showChangeBtn();
            activity.findViewById(UIHelper.layoutsSecundaryIds[1]).setVisibility(View.INVISIBLE);
            activity.currentContainerSelected=UIHelper.MAIN_CONTAINER;
        }
        if(activity.currentActivedBar==2) {
            activity.findViewById(R.id.filters_layout).setVisibility(View.VISIBLE);
        }
        FilterBarHelper.setThumbnails(activity, activity.currentContainerSelected,true);
        activity.replacePic.setOnClickListener(new ImageClickListener(UIHelper.MAIN_CONTAINER, activity));
        ((ImageView) activity.findViewById(R.id.btn_select_main)).setImageResource(R.drawable.btn_select_bkgr_on);
        activity.findViewById(UIHelper.relLayoutsIds[UIHelper.MAIN_CONTAINER]).setBackgroundResource(R.drawable.carcas_main_active);
        v.setOnClickListener(new ContainersShower(activity));
        LinearLayout secondsContainers = (LinearLayout) activity.findViewById(R.id.seconds_containers);
//        ((SeekBar) activity.findViewById(R.id.seekbar)).setProgress((int)((activity.findViewById(UIHelper.imageViewsIds[UIHelper.MAIN_CONTAINER])).getAlpha()*100));
        if(secondsContainers == null)
            return;
        secondsContainers.setVisibility(View.INVISIBLE);
    }
}
