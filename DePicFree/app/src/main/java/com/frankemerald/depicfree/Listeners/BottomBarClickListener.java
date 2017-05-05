package com.frankemerald.depicfree.Listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.UIHelpers.UIHelper;


/**
 * Created by Mark on 13.10.2015.
 */
public class BottomBarClickListener implements OnClickListener {

    private MainActivity activity;
    public BottomBarClickListener(MainActivity ctx){
        activity = ctx;
    }

    @Override
    public void onClick(View v) {
        for(int i=0; i<UIHelper.imageViewsIdsBottomBar.length;i++){
            if(v.getId()==UIHelper.imageViewsIdsBottomBar[i]){
                activity.setBar(i);
            }
        }
    }
}
