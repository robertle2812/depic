package com.frankemerald.depicpro.UIHelpers;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frankemerald.depicpro.Listeners.ColorTextClickListener;
import com.frankemerald.depicpro.Listeners.FontTextClickListener;
import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.Models.Text;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.Utils.FontManager;

import java.util.ArrayList;

/**
 * Created by Mark on 28.10.2015.
 */
public final class TextBarHelper {


    public static LinearLayout fontTextLayout,colorTextLayout, colorsLayout, fontsLayout;
    static ImageView switchModeTextButton;
    public static boolean fontPanelIsCurrent=true;
    static public ArrayList<Text> textList ;
    public static final int MAX_COUNT_TEXT = 4;

    public final static int[] colors ={
            Color.BLACK,
            Color.WHITE,
            0xFF3086DD,
            0xFFD0244C,
            0xFFDEFF00,
            0xFF00FFF6
    };

    public final static int[] activeColors={
            R.drawable.btn_color_1_on,
            R.drawable.btn_color_2_on,
            R.drawable.btn_color_3_on,
            R.drawable.btn_color_4_on,
            R.drawable.btn_color_5_on,
            R.drawable.btn_color_6_on
    };

    public final static int[] inactiveColors={
            R.drawable.btn_color_1_off,
            R.drawable.btn_color_2_off,
            R.drawable.btn_color_3_off,
            R.drawable.btn_color_4_off,
            R.drawable.btn_color_5_off,
            R.drawable.btn_color_6_off
    };

    public static final int[] indexText = {
            R.id.indexText1,
            R.id.indexText2,
            R.id.indexText3,
            R.id.indexText4,
            R.id.indexText5,
            R.id.indexText6,
            R.id.indexText7,
            R.id.indexText8,
            R.id.indexText9,
            R.id.indexText10,
            R.id.indexText11,
            R.id.indexText12
    };

    public final static int[] fontViews = {
            R.id.fontView1,
            R.id.fontView2,
            R.id.fontView3,
            R.id.fontView4,
            R.id.fontView5,
            R.id.fontView6,
            R.id.fontView7,
            R.id.fontView8,
            R.id.fontView9,
            R.id.fontView10,
            R.id.fontView11,
            R.id.fontView12
    };

    public static void initTextBar(final MainActivity activity){
        textList = new ArrayList<Text>();
        fontTextLayout = (LinearLayout) activity.findViewById(R.id.font_text_layout);
        colorTextLayout = (LinearLayout) activity.findViewById(R.id.color_text_layout);
        fontsLayout = (LinearLayout) activity.findViewById(R.id.fontslayout);
        colorsLayout = (LinearLayout) activity.findViewById(R.id.colorsLayout);
        UIHelper.fullWidth = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        UIHelper.nullWidth = new LinearLayout.LayoutParams(0, 0);
        switchModeTextButton = (ImageView) activity.findViewById(R.id.switch_mode_text_button);
        switchModeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTextPanel();
            }
        });

        FontManager.init(activity);
        initFontBar(activity);
        initColorBar(activity);
    }

    private static void initColorBar(MainActivity activity) {
        for(int i=0; i<colorsLayout.getChildCount();i++){
            colorsLayout.getChildAt(i).setOnClickListener(new ColorTextClickListener(activity, i));
        }
    }

    private static void initFontBar(MainActivity activity) {
        for(int i=0; i<fontsLayout.getChildCount(); i++){
            ((RelativeLayout)fontsLayout.getChildAt(i)).getChildAt(0).setOnClickListener(new FontTextClickListener(activity, i));
            ((TextView)((RelativeLayout)fontsLayout.getChildAt(i)).getChildAt(0)).setTypeface(FontManager.getFonts().get(i));
        }
    }

    public static void switchTextPanel() {
        if(fontPanelIsCurrent){
            switchModeTextButton.setImageResource(R.drawable.btn_font_types);
            fontTextLayout.setLayoutParams(UIHelper.nullWidth);
            colorTextLayout.setLayoutParams(UIHelper.fullWidth);
            fontPanelIsCurrent=false;
        }
        else{
            switchModeTextButton.setImageResource(R.drawable.btn_colors_types);
            fontTextLayout.setLayoutParams(UIHelper.fullWidth);
            colorTextLayout.setLayoutParams(UIHelper.nullWidth);
            fontPanelIsCurrent=true;
        }
    }
}
