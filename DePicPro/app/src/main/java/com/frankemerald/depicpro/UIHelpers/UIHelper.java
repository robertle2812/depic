package com.frankemerald.depicpro.UIHelpers;

import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.frankemerald.depicpro.Listeners.BottomBarClickListener;
import com.frankemerald.depicpro.Listeners.FrameBarClickListener;
import com.frankemerald.depicpro.Listeners.SeekbarListener;
import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.R;

import java.util.ArrayList;

/**
 * Created by Mark on 13.10.2015.
 */

public final class UIHelper {

    public static final int MAIN_CONTAINER = 0;
    public static final int LEFT_CONTAINER = 1;
    public static final int RIGHT_CONTAINER =2;
    public static final int TOP_CONTAINER = 3;
    public static final int BOTTOM_CONTAINER = 4;
    public static final int LEFT_BOTTOM_CONTAINER = 5;
    public static final int RIGHT_BOTTOM_CONTAINER = 6;
    public static final int RIGHT_TOP_CONTAINER = 7;
    public static final int LEFT_TOP_CONTAINER = 8;
    public static final int LEFT_CENTER_CONTAINER = 9;
    public static final int RIGHT_CENTER_LAYOUT = 10;
    public static final int TOP_CENTER_CONTAINER = 11;
    public static final int BOTTOM_CENTER_LAYOUT = 12;
    public static final int FULL_LAYOUT=13;

    public final static int[] imgOffForBottomBarIds = {
            R.drawable.btn_frames_off,
            R.drawable.btn_transparancy_off,
            R.drawable.btn_filtres_off,
            R.drawable.btn_add_font_off,
            R.drawable.btn_share_off
    };

    public final static int[] imgOnForBottomBarIds = {
            R.drawable.btn_frames_on,
            R.drawable.btn_transparancy_on,
            R.drawable.btn_filtres_on,
            R.drawable.btn_add_new_font_on,
            R.drawable.btn_share_on
    };

    public final static int[] imageViewsIdsBottomBar ={
            R.id.framework_bottom_bar,
            R.id.transparency_bottom_bar,
            R.id.filter_bottom_bar,
            R.id.text_bottom_bar,
            R.id.share_bottom_bar
    };

    public final static int[] layoutsSecundaryIds = {
            R.id.framework_layout,
            R.id.transparency_layout,
            R.id.filters_layout,
            R.id.text_layout,
            R.id.social_layout
    };

    public final static int[] drawableFrameActiveIds = {
            R.drawable.btn_frame_1_on,
            R.drawable.btn_frame_2_on,
            R.drawable.btn_frame_3_on,
            R.drawable.btn_frame_4_on,
            R.drawable.btn_frame_5_on,
            R.drawable.btn_frame_6_on,
            R.drawable.btn_frame_7_on,
            R.drawable.btn_frame_8_on,
            R.drawable.btn_frame_9_on
    };

    public final static int[] drawableFrameInactiveIds = {
            R.drawable.btn_frame_1_off,
            R.drawable.btn_frame_2_off,
            R.drawable.btn_frame_3_off,
            R.drawable.btn_frame_4_off,
            R.drawable.btn_frame_5_off,
            R.drawable.btn_frame_6_off,
            R.drawable.btn_frame_7_off,
            R.drawable.btn_frame_8_off,
            R.drawable.btn_frame_9_off
    };

    public final static int[] layoutFrameworks = {
            R.layout.frame_first,
            R.layout.frame_two,
            R.layout.frame_three,
            R.layout.frame_four,
            R.layout.frame_five,
            R.layout.frame_six,
            R.layout.frame_seven,
            R.layout.frame_eight,
            R.layout.frame_nine
    };

    public final static int[] imageViewsIds = {
            R.id.main_image,
            R.id.leftImage,
            R.id.rightImage,
            R.id.topImage,
            R.id.bottomImage,
            R.id.leftBottomImage,
            R.id.rightBottomImage,
            R.id.rightTopImage,
            R.id.leftTopImage,
            R.id.leftCenterImage,
            R.id.rightCenterImage,
            R.id.topCenterImage,
            R.id.bottomCenterImage,
            R.id.fullImage
    };

    public final static int[] relLayoutsIds ={
            R.id.main_container,
            R.id.leftLayout,
            R.id.rightLayout,
            R.id.topLayout,
            R.id.bottomLayout,
            R.id.leftBottomLayout,
            R.id.rightBottomLayout,
            R.id.rightTopLayout,
            R.id.leftTopLayout,
            R.id.leftCenterLayout,
            R.id.rightCenterLayout,
            R.id.topCenterLayout,
            R.id.bottomCenterLayout,
            R.id.fullLayout
    };


    public static int widhtFramePixels;
    static LinearLayout.LayoutParams lpFrame;
    static LinearLayout.LayoutParams fullWidth,nullWidth;

    public static void init(MainActivity activity){
        for(int i=0;i<imageViewsIds.length;i++){
            activity.orignalBitmaps.add(null);
        }
        initLParamsFromFramework(activity);
        initSecundaryBarLayouts(activity);
        MenuHelper.init(activity);
    }


    public static ArrayList<ImageView> getBottomBarViews(MainActivity ctx){
        ArrayList<ImageView> bottomBarList = new ArrayList<>(5);
        for(int i=0; i< imageViewsIdsBottomBar.length;i++){
            bottomBarList.add((ImageView) ctx.findViewById(imageViewsIdsBottomBar[i]));
            bottomBarList.get(i).setOnClickListener(new BottomBarClickListener(ctx));
        }
        return bottomBarList;
    }

    public static ArrayList<LinearLayout> getSecundaryBarLayouts(MainActivity ctx){
        ArrayList<LinearLayout> secundaryBarLayouts = new ArrayList<>(5);
        for(int i=0;i< layoutsSecundaryIds.length;i++){
            secundaryBarLayouts.add((LinearLayout) ctx.findViewById(layoutsSecundaryIds[i]));
        }
        return secundaryBarLayouts;
    }

    private static void initSecundaryBarLayouts(MainActivity activity) {
        initFrameBar(activity);
        initSeekbar(activity);
        TextBarHelper.initTextBar(activity);
        FilterBarHelper.init(activity);
        ShareBarHelper.init(activity);

    }

    private static void initFrameBar(MainActivity activity) {
        LinearLayout frameBar = (LinearLayout) activity.findViewById(R.id.framework_scroll_layout);
        for(int i=0;i<frameBar.getChildCount();i++){
            frameBar.getChildAt(i).setOnClickListener(new FrameBarClickListener(activity,i));
        }
    }

    private static void initSeekbar(MainActivity activity){
        ((SeekBar) activity.findViewById(R.id.seekbar)).setOnSeekBarChangeListener(new SeekbarListener(activity));
    }

    public static void initLParamsFromFramework(MainActivity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightScreenDip = (int)(metrics.heightPixels/(metrics.densityDpi/160.0));
        int widthScreenDip = (int) (metrics.widthPixels/(metrics.densityDpi/160.0));
        int widthFrameDip = Math.min(heightScreenDip-70-70-50-50, widthScreenDip-32);
        UIHelper.widhtFramePixels = (int)(widthFrameDip * ((double)metrics.densityDpi/160));
        UIHelper.lpFrame = new LinearLayout.LayoutParams(UIHelper.widhtFramePixels,UIHelper.widhtFramePixels);
        UIHelper.lpFrame.setMargins(0, 50 + 50, 0, 70 + 70);
    }
}
