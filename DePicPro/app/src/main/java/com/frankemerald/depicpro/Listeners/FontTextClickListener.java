package com.frankemerald.depicpro.Listeners;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.TextBarHelper;

/**
 * Created by Mark on 28.10.2015.
 */
public class FontTextClickListener implements View.OnClickListener {

    MainActivity activity;
    int font;

    public FontTextClickListener(MainActivity activity, int font){
        this.activity = activity;
        this.font = font;
    }
    @Override
    public void onClick(View v) {
        TextView tv;
            for (int i = 0; i < TextBarHelper.fontsLayout.getChildCount(); i++) {
                tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(i)).getChildAt(0);
                tv.setBackgroundResource(R.drawable.font_inactived);
                tv.setTextColor(0xFF93989C);
                tv = (TextView) activity.findViewById(TextBarHelper.indexText[i]);
                tv.setBackgroundResource(R.drawable.index_inactiv);
                tv.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.font_actived);
            ((TextView) v).setTextColor(0xFFFEFFFD);
        tv = (TextView) activity.findViewById(TextBarHelper.indexText[font]);
        tv.setBackgroundResource(R.drawable.index_text_actived);
        tv.setTextColor(0xffdadada);
        if(!activity.preparedForAddingText) {
            activity.setFont(font);
        }
        else{
            activity.addTextField(font);
        }
    }
}
