package com.frankemerald.depicpro.Listeners;

import android.view.View;
import android.widget.ImageView;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.UIHelpers.TextBarHelper;

/**
 * Created by Mark on 05.11.2015.
 */
public class ColorTextClickListener implements View.OnClickListener {

    MainActivity activity;
    int color;
    public ColorTextClickListener(MainActivity activity, int color) {
        this.activity = activity;
        this.color = color;
    }

    @Override
    public void onClick(View v) {
        if(activity.currentTextSelected!=null) {
            activity.setTextColor(color);
            for (int i = 0; i < TextBarHelper.colorsLayout.getChildCount(); i++) {
                ((ImageView) TextBarHelper.colorsLayout.getChildAt(i)).setImageResource(TextBarHelper.inactiveColors[i]);
            }
            ((ImageView) v).setImageResource(TextBarHelper.activeColors[color]);
        }
    }
}
