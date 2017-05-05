package com.frankemerald.depicfree.Listeners;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.UIHelpers.TextBarHelper;

/**
 * Created by Mark on 05.11.2015.
 */
public class ColorTextClickListener implements View.OnClickListener {

    final int FIRST_BLOCKED_COLOR =0;
    MainActivity activity;
    int color;
    public ColorTextClickListener(MainActivity activity, int color) {
        this.activity = activity;
        this.color = color;
    }

    @Override
    public void onClick(View v) {
        if(color>=FIRST_BLOCKED_COLOR){
            activity.showProposeDialog(7);
            return;
        }

        if(activity.currentTextSelected!=null) {
            activity.setTextColor(color);
            for (int i = 0; i < TextBarHelper.colorsLayout.getChildCount(); i++) {
                ((ImageView) ((RelativeLayout)TextBarHelper.colorsLayout.getChildAt(i)).getChildAt(0)).setImageResource(TextBarHelper.inactiveColors[i]);
            }
            ((ImageView) ((RelativeLayout)v).getChildAt(0)).setImageResource(TextBarHelper.activeColors[color]);
        }
    }
}
