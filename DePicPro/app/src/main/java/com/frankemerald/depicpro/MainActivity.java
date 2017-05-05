package com.frankemerald.depicpro;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.frankemerald.depicpro.FramesHelper.FrameEightHelper;
import com.frankemerald.depicpro.FramesHelper.FrameFirstHelper;
import com.frankemerald.depicpro.FramesHelper.FrameNineHelper;
import com.frankemerald.depicpro.Listeners.ContainersHider;
import com.frankemerald.depicpro.Listeners.ImageClickListener;
import com.frankemerald.depicpro.FramesHelper.FrameFiveHelper;
import com.frankemerald.depicpro.FramesHelper.FrameFourHelper;
import com.frankemerald.depicpro.FramesHelper.FrameSevenHelper;
import com.frankemerald.depicpro.FramesHelper.FrameSixHelper;
import com.frankemerald.depicpro.FramesHelper.FrameThreeHelper;
import com.frankemerald.depicpro.FramesHelper.FrameTwoHelper;
import com.frankemerald.depicpro.Models.Text;
import com.frankemerald.depicpro.Sharing.FacebookSharing;
import com.frankemerald.depicpro.UIHelpers.FilterBarHelper;
import com.frankemerald.depicpro.UIHelpers.MenuHelper;
import com.frankemerald.depicpro.UIHelpers.TextBarHelper;
import com.frankemerald.depicpro.UIHelpers.UIHelper;
import com.frankemerald.depicpro.Utils.FontManager;
import com.frankemerald.depicpro.Utils.SaveHelper;
import com.frankemerald.depicpro.Utils.Utils;
import com.frankemerald.depicpro.Views.TouchImageView;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    private int offset_y_resize =0;
    Boolean touchFlag = false;
    boolean resizeFlag = false;
    boolean dropFlag = false;
    int eX, eY;
    int topY, leftX, rightX, bottomY;
    int idTextLayout=9999, idEditText = 8888, idText = 7777, idResize = 6666, idDelete = 5555, idRTextLayout=4444, idMove=3333;
    RelativeLayout.LayoutParams lp;
    AlertDialog.Builder ad;
    AlertDialog adj;

    public ImageView setting, rateUs, restorePurchases,
            group_social, group_facebook, group_instagram,
            shareLeft, shareInstagramL, shareFacebookL, shareTwitterL,
            shareTumblrL, shareMMSL, shareEmailL,
            replacePic,deletePic,
            delete, scale;

    public LinearLayout leftMenuBar, shareLeftSection,group_container,
            frameworkScrolllayout, transparencyBarLayout, filtersBarLayout, textBarLayout, socialBarLayout;
    boolean menuLeftIsOpen = false;
    public boolean sectionShareIsOpen = false;
    boolean textBarSelected =false;
    ArrayList<ImageView> bottomBarViews;
    ArrayList<LinearLayout> listSecundarBarLayouts;
    Animation openRotate, closeRotate;
    int widthLeftMenu;
    RelativeLayout frameworkContainer,  addButon, mainContainer, textFrame, textRLayout, move;
    public int currentContainerSelected = -1, textFields =0 , currentActivedBar=0, currentFramework = -1;
    EditText edtext;
    TextView txt;
    FrameLayout textLayout;

    DisplayMetrics metrics = new DisplayMetrics();

    public ArrayList<Text> texts = new ArrayList<Text>();
    int textCount = 0;
    public Text currentTextSelected = null;
    int initialTouchX =0 , initialTouchY=0, centerX=0, centerY=0;
    float originalSize=0;
    public boolean textIsInShowMod = false, preparedForAddingText = false;

    public ArrayList<Bitmap> orignalBitmaps = new ArrayList<Bitmap>(14);
    public ArrayList<Bitmap> smallBitmaps = new ArrayList<Bitmap>(14);
    public int[] alphaList = {255,255,255,255,255,255,255,255,255,255,255,255,255,255};
    int id0 = R.id.original, dp;
    public int[] curentFilter = {id0,id0,id0,id0,id0,id0,id0,id0,id0,id0,id0,id0,id0,id0};

    public int containersCount = 0;

    public int clickImage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        smallBitmaps = new ArrayList<Bitmap>(14);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        dp = (int)(1.0 * ((double)metrics.densityDpi/160.0));
        frameworkContainer = (RelativeLayout) findViewById(R.id.framework_container);
        UIHelper.init(this);
        prepareLeftMenu();
        prepareBottomBar();
        prepareTopRightButton();

    }

    private void prepareTopRightButton() {
        replacePic = (ImageView) findViewById(R.id.replace_pic);
        deletePic = (ImageView) findViewById(R.id.deleteContainer);
    }


    private void prepareLeftMenu() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthLeftMenu = (int)(80 * ((double)metrics.densityDpi/160));
        leftMenuBar = (LinearLayout) findViewById(R.id.leftMenuBar);
        leftMenuBar.setTranslationX(-widthLeftMenu);
        shareLeftSection = (LinearLayout) findViewById(R.id.share_left_menu_section);
        shareLeftSection.setTranslationX(-widthLeftMenu);

        rateUs = (ImageView) findViewById(R.id.rate_us);

        group_social = (ImageView) findViewById(R.id.group_social);
        group_container = (LinearLayout) findViewById(R.id.group_container);
        group_facebook = (ImageView) findViewById(R.id.facebook_group);
        group_instagram = (ImageView) findViewById(R.id.instagram_group);

        restorePurchases = (ImageView) findViewById(R.id.restore_purchases);
        shareLeft = (ImageView) findViewById(R.id.share_left_menu_button);
        shareInstagramL = (ImageView) findViewById(R.id.instagram_share_left);
        shareFacebookL = (ImageView) findViewById(R.id.facebook_share_left);
        shareTwitterL = (ImageView) findViewById(R.id.twitter_share_left);
        shareTumblrL = (ImageView) findViewById(R.id.tumblr_share_left);
        shareMMSL = (ImageView) findViewById(R.id.mms_share_left);
        shareEmailL = (ImageView) findViewById(R.id.email_share_left);

        shareLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateLeftSectionShare();
            }
        });

        setting = (ImageView) findViewById(R.id.settingButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateLeftMenuBar();
            }
        });

        openRotate = AnimationUtils.loadAnimation(this, R.anim.open_setting_rotate);
        closeRotate = AnimationUtils.loadAnimation(this, R.anim.close_setting_rotate);

    }

    public void animateLeftSectionShare() {
        if(sectionShareIsOpen){
            shareLeft.setImageResource(R.drawable.btn_share_app);
            animateLayout(0, -widthLeftMenu, 250, true, shareLeftSection);
            sectionShareIsOpen = false;
        }
        else {
            shareLeft.setImageResource(R.drawable.btn_share_app_on);
            animateLayout(-widthLeftMenu, 0, 250, true, shareLeftSection);
            sectionShareIsOpen = true;
            if(!MenuHelper.groupMenuIsOpen)
                MenuHelper.animateGroupMenu(this);
        }
    }

    private void animateLeftMenuBar() {
        if(menuLeftIsOpen){
            setting.setImageResource(R.drawable.btn_settings_off);
            setting.startAnimation(openRotate);
            animateLayout(0, -widthLeftMenu, 250, true, leftMenuBar);
            menuLeftIsOpen = false;
            if(sectionShareIsOpen)
                animateLeftSectionShare();
            if(!MenuHelper.groupMenuIsOpen)
                MenuHelper.animateGroupMenu(this);
        }
        else {
            setting.setImageResource(R.drawable.btn_settings_on);
            setting.startAnimation(closeRotate);
            animateLayout(-widthLeftMenu, 0, 250, true, leftMenuBar);
            menuLeftIsOpen = true;
        }
    }

    public void animateLayout(int a, int b, int duration, final boolean axaIsX, final LinearLayout v){
        ValueAnimator va = ValueAnimator.ofInt(a, b);
        va.setDuration(duration);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (axaIsX) {
                    v.setTranslationX((Integer) animation.getAnimatedValue());
                } else {
                    v.setTranslationY((Integer) animation.getAnimatedValue());
                }
            }
        });
        va.start();
    }

    private void prepareBottomBar() {
        frameworkScrolllayout = (LinearLayout) findViewById(R.id.framework_scroll_layout);
        bottomBarViews = new ArrayList<>(UIHelper.getBottomBarViews(this));
        listSecundarBarLayouts = new ArrayList<>(UIHelper.getSecundaryBarLayouts(this));
    }

    public boolean existPhoto(){
        boolean existPhoto = true;
        for(int j=0;j<orignalBitmaps.size();j++){
            if(orignalBitmaps.get(j) != null) {
                existPhoto = true;
                break;
            }
            existPhoto = false;
        }
        return  existPhoto;
    }

    public void setBar(int i){

        if(findViewById(R.id.main_container)==null){
            showProposeDialog(3);
            return;
        }
        if(!existPhoto()) {
            showProposeDialog(0);
            return;
        }

        if(isOnlyBackgroundDownloaded()&&i==1&&currentContainerSelected!=0){
            return;
        }

        if(currentContainerSelected==0&&i==1) {
            showProposeDialog(4);
                return;
        }

        if((currentContainerSelected<0&&(i==2 || i==1)&&!existPhoto())){
            showProposeDialog(2);
            return;
        }

        if (i == 3) {
            textBarSelected=true;
            if (!textBarSelected) {
                textBarSelected = true;
                if(texts.size()==0){
                    TextView tv;
                    for (int k = 0; k < TextBarHelper.fontsLayout.getChildCount(); k++) {
                        tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(k)).getChildAt(0);
                        tv.setBackgroundResource(R.drawable.font_inactived);
                        tv.setTextColor(0xFF93989C);
                        tv = (TextView) findViewById(TextBarHelper.indexText[k]);
                        tv.setBackgroundResource(R.drawable.index_inactiv);
                        tv.setTextColor(Color.BLACK);
                    }
                    preparedForAddingText = true;
                    if(!TextBarHelper.fontPanelIsCurrent){
                        findViewById(R.id.switch_mode_text_button).callOnClick();
                    }
                }
                if(currentTextSelected!=null){
                    currentTextSelected.getBorder().setBackgroundResource(R.drawable.carcas_main_active);
                    currentTextSelected.getDelete().setVisibility(View.VISIBLE);
                    currentTextSelected.getResize().setVisibility(View.VISIBLE);
                    currentTextSelected.getMove().setVisibility(View.VISIBLE);
                    textIsInShowMod = true;
                }
            }else{
                if(mainContainer!=null) {
                    TextView tv;
                    for (int k = 0; k < TextBarHelper.fontsLayout.getChildCount(); k++) {
                        tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(k)).getChildAt(0);
                        tv.setBackgroundResource(R.drawable.font_inactived);
                        tv.setTextColor(0xFF93989C);
                        tv = (TextView) findViewById(TextBarHelper.indexText[k]);
                        tv.setBackgroundResource(R.drawable.index_inactiv);
                        tv.setTextColor(Color.BLACK);
                    }
                    if(currentTextSelected!=null){
                        currentTextSelected.getBorder().setBackgroundColor(0x00000000);
                        currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
                        currentTextSelected.getResize().setVisibility(View.INVISIBLE);
                        currentTextSelected.getMove().setVisibility(View.INVISIBLE);
                        textIsInShowMod = true;
                    }
                    preparedForAddingText = true;
                    if(!TextBarHelper.fontPanelIsCurrent){
                        findViewById(R.id.switch_mode_text_button).callOnClick();
                    }
                }
            }
        }
        else{
            textIsInShowMod=false;
            textBarSelected = false;
            if(i<3&&currentContainerSelected>-1){
                if(currentTextSelected!=null){
                    currentTextSelected.getBorder().setBackgroundColor(0x00000000);
                    currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
                    currentTextSelected.getResize().setVisibility(View.INVISIBLE);
                    currentTextSelected.getMove().setVisibility(View.INVISIBLE);
                    textIsInShowMod=false;
                }
                if(currentContainerSelected==0)
                    findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundResource(R.drawable.carcas_main_active);
                else
                    findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundResource(R.drawable.carcas_second_active);
            }
        }

        if(i==4||i==0){
            if(i==0)
                showDebug();
            showResult(i);
            if(i==0)
                findViewById(R.id.watermark).setVisibility(View.INVISIBLE);
        }
        else{
            if(currentActivedBar==4||currentActivedBar==0){
                showDebug();
            }
        }

        if(i==3&&currentContainerSelected>0){
            findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundColor(0x00000000);
        }

        for(int j=0; j<UIHelper.imageViewsIdsBottomBar.length ;j++){
            bottomBarViews.get(j).setImageResource(UIHelper.imgOffForBottomBarIds[j]);
            listSecundarBarLayouts.get(j).setVisibility(View.INVISIBLE);
        }
        bottomBarViews.get(i).setImageResource(UIHelper.imgOnForBottomBarIds[i]);
        if(!((i==1&&currentContainerSelected==0)||((i==1||i==2)&&currentContainerSelected==-1)))
        listSecundarBarLayouts.get(i).setVisibility(View.VISIBLE);

        if(i==2&&currentActivedBar!=2)
            FilterBarHelper.setThumbnails(this, currentContainerSelected, false);



        if((i==1||i==2)&&currentContainerSelected==-1){
            if(isOnlyBackgroundDownloaded() && currentContainerSelected==-1){
                listSecundarBarLayouts.get(currentActivedBar).setVisibility(View.INVISIBLE);
                listSecundarBarLayouts.get(i).setVisibility(View.VISIBLE);
                if (i == 2)
                    FilterBarHelper.setThumbnails(this, UIHelper.MAIN_CONTAINER, false);
            }
            else {
                for (int j = 1; j < UIHelper.imageViewsIds.length; j++) {
                    TouchImageView t = (TouchImageView) findViewById(UIHelper.imageViewsIds[j]);
                    if (t != null && t.getDrawable() != null) {
                        currentActivedBar = i;
                        clickImage = 1;
                        t.callOnClick();
                        listSecundarBarLayouts.get(i).setVisibility(View.VISIBLE);
                        if (i == 2)
                            FilterBarHelper.setThumbnails(this, currentContainerSelected, false);
                        break;
                    }
                }
            }
        }

        if((i==1||i==2)&&currentContainerSelected!=-1){
            showChangeBtn();
        }
        if(i==3){
            hideChangeBtn();
        }

        textIsInShowMod = false;
        currentActivedBar = i;
    }

    public boolean isOnlyBackgroundDownloaded(){
        boolean onlyBackgroundDownloaded = true;
        for(int j=1;j<orignalBitmaps.size();j++){
            if(orignalBitmaps.get(j)!=null){
                onlyBackgroundDownloaded = false;
                break;
            }
        }
        if(orignalBitmaps.get(0)==null){
            onlyBackgroundDownloaded=false;
        }

        return onlyBackgroundDownloaded;
    }

    public void showResult(int m){
        if(m==4) {
            findViewById(R.id.add_main_image).setVisibility(View.INVISIBLE);
            findViewById(R.id.watermark).setVisibility(View.VISIBLE);

            for(int j = 1; j<UIHelper.relLayoutsIds.length;j++) {
                RelativeLayout view = (RelativeLayout) findViewById(UIHelper.relLayoutsIds[j]);
                if (view != null) {
                    TouchImageView t = (TouchImageView) view.getChildAt(1);

                    t.setEnabled(false);
                    if (t.getDrawable() == null) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    else{
                        view.setBackgroundColor(0x00000000);
                    }
                }
            }
            findViewById(UIHelper.imageViewsIds[0]).setEnabled(false);

            for(int k=0; k<texts.size(); k++){
                findViewById(texts.get(k).getId()).setEnabled(false);
            }
			
			findViewById(UIHelper.relLayoutsIds[0]).setBackgroundResource(R.drawable.carcas_main_without_border);
		}

        if(m==0&&currentContainerSelected>0){

            findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundColor(0x00000000);
        }

        if(currentContainerSelected==0){
            currentActivedBar=4;
            findViewById(R.id.add_main_image).callOnClick();
        }

        if(currentTextSelected!=null) {
            currentTextSelected.getBorder().setBackgroundColor(0x00000000);
            currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
            currentTextSelected.getResize().setVisibility(View.INVISIBLE);
            currentTextSelected.getMove().setVisibility(View.INVISIBLE);
        }

        hideChangeBtn();
        textIsInShowMod = false;

    }

    public void showDebug(){
        findViewById(R.id.add_main_image).setVisibility(View.VISIBLE);
        findViewById(R.id.watermark).setVisibility(View.INVISIBLE);
        if(currentContainerSelected==0){
            findViewById(UIHelper.relLayoutsIds[0]).setBackgroundResource(R.drawable.carcas_main_active);
        }
        else{
            findViewById(UIHelper.relLayoutsIds[0]).setBackgroundResource(R.drawable.carcas_main_inactive);
        }

        for(int j = 1; j<UIHelper.relLayoutsIds.length;j++) {
            RelativeLayout view = (RelativeLayout) findViewById(UIHelper.relLayoutsIds[j]);
            if (view != null) {
                TouchImageView t = (TouchImageView) view.getChildAt(1);
                t.setEnabled(true);
                if (t.getDrawable() == null) {
                view.setVisibility(View.VISIBLE);
                }
                else{
                    if(j!=currentContainerSelected)
                        view.setBackgroundColor(0x00000000);
                    else
                        view.setBackgroundResource(R.drawable.carcas_second_active);
                }
            }
        }
        findViewById(UIHelper.imageViewsIds[0]).setEnabled(true);

        for(int k=0; k<texts.size(); k++){
            findViewById(texts.get(k).getId()).setEnabled(true);
        }
        textIsInShowMod = false;
    }

    public Text getTextFromId(int id){
        for(int i=0; i<texts.size();i++){
            if(texts.get(i).getId()==id){
                return texts.get(i);
            }
        }
        return null;
    }

    public void addTextField(int font) {

        if(textCount==TextBarHelper.MAX_COUNT_TEXT)
            return;

        if(currentContainerSelected==0){
            findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundResource(R.drawable.carcas_main_inactive);
        }
        else if(currentContainerSelected!=-1){
            findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundColor(0x00000000);
        }

        textFrame.inflate(this, R.layout.text_view, textFrame);
        textLayout = (FrameLayout) findViewById(R.id.textView);
        textLayout.setId(idTextLayout++);
        RelativeLayout.LayoutParams lpt= (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
        int marginTop = 30*dp +textCount*70*dp;
        int marginLeft = 10*dp + textCount/5*100*dp;
        lpt.setMargins(marginLeft, marginTop, 0, 0);
        textLayout.setLayoutParams(lpt);

        edtext = (EditText) findViewById(R.id.edtext);
        edtext.setId(idEditText++);
        edtext.setTypeface(FontManager.getFonts().get(0));

        delete  =(ImageView) findViewById(R.id.delete_btn);
        delete.setId(idDelete++);

        scale = (ImageView) findViewById(R.id.resize_btn);
        scale.setId(idResize++);

        move = (RelativeLayout) findViewById(R.id.move_button);
        move.setId(idMove++);

        textRLayout = (RelativeLayout) textLayout.getChildAt(0);
        textRLayout.setId(idRTextLayout++);

        txt = (TextView) findViewById(R.id.text);
        txt.setId(idText++);
        txt.setTypeface(FontManager.getFonts().get(0));

        if(currentTextSelected!=null && currentTextSelected.getId()!=textLayout.getId()) {
                    currentTextSelected.getBorder().setBackgroundColor(0x00000000);
                    currentTextSelected.getResize().setVisibility(View.INVISIBLE);
                    currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
                    currentTextSelected.getMove().setVisibility(View.INVISIBLE);
                    textIsInShowMod=false;
                }

        currentTextSelected = new Text(move, textLayout, textRLayout, 0, delete, new int[]{marginLeft,marginTop,0,0}, edtext, 0, textLayout.getId(), lpt, 0, scale, txt, "text");

        texts.add(textCount, currentTextSelected);

        getTextFromId(currentTextSelected.getId()).getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textCount--;
                textFrame.removeView(getTextFromId(currentTextSelected.getId()).getFrame());
                texts.remove(getTextFromId(currentTextSelected.getId()));
                TextView tv;
                for (int k = 0; k < TextBarHelper.fontsLayout.getChildCount(); k++) {
                    tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(k)).getChildAt(0);
                    tv.setBackgroundResource(R.drawable.font_inactived);
                    tv.setTextColor(0xFF93989C);
                    tv = (TextView) findViewById(TextBarHelper.indexText[k]);
                    tv.setBackgroundResource(R.drawable.index_inactiv);
                    tv.setTextColor(Color.BLACK);
                }
                preparedForAddingText = true;
                textIsInShowMod = false;
            }
        });

        getTextFromId(currentTextSelected.getId()).getMove().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Text current = getTextFromId(currentTextSelected.getId());
                EditText edittext = current.getEditText();
                TextView textView = current.getTextView();
                textView.setText(edittext.getText().toString());
                textView.setVisibility(View.VISIBLE);
                edittext.setVisibility(View.INVISIBLE);
                textIsInShowMod = false;
                return false;
            }
        });

        getTextFromId(currentTextSelected.getId()).getResize().setOnTouchListener(new ResizeTouchListener());

        textLayout.setOnTouchListener(this);

        textFrame.setOnTouchListener(new FrameTouchListener());
        textFrame.setEnabled(false);

        textCount++;
        preparedForAddingText = false;

        ((LinearLayout) findViewById(R.id.colorsLayout)).getChildAt(0).callOnClick();
        ((RelativeLayout)((LinearLayout) findViewById(R.id.fontslayout)).getChildAt(font)).getChildAt(0).callOnClick();

        currentTextSelected.getBorder().setBackgroundResource(R.drawable.carcas_main_active);
        currentTextSelected.getDelete().setVisibility(View.VISIBLE);
        currentTextSelected.getResize().setVisibility(View.VISIBLE);
        currentTextSelected.getMove().setVisibility(View.VISIBLE);
    }

    private class ResizeTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    resizeFlag = true;
                    offset_y_resize = (int) event.getY();
                    textFrame.setEnabled(true);
                    break;
            }
            return false;
        }
    }

    private class FrameTouchListener implements View.OnTouchListener{

        public boolean onTouch(View v, MotionEvent event) {
            int itemTop, itemLeft, itemBottom = 0, itemRight;
            if (touchFlag) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    {
                        View r, t;
                        for (int i = 1; i < UIHelper.relLayoutsIds.length; i++) {
                            r = findViewById(UIHelper.imageViewsIds[i]);
                            t = findViewById(UIHelper.relLayoutsIds[i]);
                            if (r != null) r.setEnabled(false);
                            if (t != null) t.setEnabled(false);
                            ;
                        }
                    }
                    topY = textFrame.getTop();
                    leftX = textFrame.getLeft();
                    rightX = textFrame.getRight();
                    rightX = textFrame.getWidth();
                    bottomY = textFrame.getBottom();
                    bottomY = textFrame.getHeight();
                    break;
                    case MotionEvent.ACTION_MOVE:
                        itemLeft = selected_item.getLeft();
                        itemBottom = selected_item.getBottom();
                        itemRight = selected_item.getRight();
                        itemTop = selected_item.getTop();

                        if(itemLeft>=0 && itemBottom<bottomY && itemRight<rightX && itemTop>=0) {
                            int x = (int) event.getX() - offset_x;
                            if(!resizeFlag) {
                                int y = (int) event.getY() - offset_y;
                                currentTextSelected.setCoord(new int[]{x, y, 0, 0});
                                lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                lp.setMargins(x, y, 0, 0);
                                selected_item.setLayoutParams(lp);
                                eY = (int) event.getY();
                            }
                            else{
                                if(eY==0) eY= (int) event.getY()-offset_y;
                                currentTextSelected.setCoord(new int[]{x, eY-offset_y_resize, 0, 0});
                                lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                lp.setMargins(x, eY-offset_y_resize, 0, 0);
                                selected_item.setLayoutParams(lp);
                            }

                            eX = (int) event.getX();
                        }

                        else if((itemRight>=rightX && event.getX()<eX) ||
                                (itemBottom>=bottomY && event.getY()<eY) ||
                                (itemTop<0 && event.getY()>eY) ||
                                (itemLeft<0 && event.getX()>eX)){
                            int x = (int) event.getX() - offset_x;
                            if(!resizeFlag) {
                                int y = (int) event.getY() - offset_y;
                                currentTextSelected.setCoord(new int[]{x, y, 0, 0});
                                lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                lp.setMargins(x, y, 0, 0);
                                selected_item.setLayoutParams(lp);
                            }
                            else{
                                currentTextSelected.setCoord(new int[]{x, eY-offset_y_resize, 0, 0});
                                lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                lp.setMargins(x, eY-offset_y_resize, 0, 0);
                                selected_item.setLayoutParams(lp);
                            }
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        selected_item = null;
                        touchFlag = false;
                    {
                        View r, t;
                        for (int i = 1; i < UIHelper.relLayoutsIds.length; i++) {
                            r = findViewById(UIHelper.imageViewsIds[i]);
                            t = findViewById(UIHelper.relLayoutsIds[i]);
                            if (r != null) r.setEnabled(true);
                            if (t != null) t.setEnabled(true);
                        }
                    }
                    if (dropFlag) {
                        dropFlag = false;
                    }
                    if ((int) event.getX() == offset_x && (int) event.getY() == offset_y) {
                        currentTextSelected.getBorder().setBackgroundResource(R.drawable.carcas_main_active);
                        currentTextSelected.getDelete().setVisibility(View.VISIBLE);
                        currentTextSelected.getResize().setVisibility(View.VISIBLE);
                        currentTextSelected.getMove().setVisibility(View.VISIBLE);
                        if(textIsInShowMod) {
                            currentTextSelected.getTextView().setVisibility(View.INVISIBLE);
                            EditText et = currentTextSelected.getEditText();
                            et.setVisibility(View.VISIBLE);
                            et.setSelection(et.getText().length());
                            et.setFocusable(true);
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
                        }
                        else{
                            textIsInShowMod = true;
                        }
                    }
                        textFrame.setEnabled(false);
                        eY=0;
                    break;
                    default:
                        break;
                }
            }
            if(resizeFlag){
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialTouchX = (int) event.getX();
                        initialTouchY = (int) event.getY();
                        itemLeft = selected_item.getLeft();
                        itemBottom = selected_item.getBottom();
                        itemRight = selected_item.getRight();
                        itemTop = selected_item.getTop();
                        centerX = (itemRight+itemLeft)/2;
                        centerY = (itemBottom+itemTop)/2;
                        originalSize = currentTextSelected.getTextView().getTextSize();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int height = y - initialTouchY;
                        float size = originalSize + height;
                        if(size>20) {
                            currentTextSelected.getEditText().setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                            currentTextSelected.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        selected_item = null;
                        resizeFlag =false;
                        touchFlag = false;
                        dropFlag  = false;
                        textFrame.setEnabled(false);
                        break;
                }
            }
            return true;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                preparedForAddingText=false;
                if(currentContainerSelected==0||currentContainerSelected==-1){
                    findViewById(UIHelper.relLayoutsIds[currentContainerSelected==0?currentContainerSelected:0]).setBackgroundResource(R.drawable.carcas_main_inactive);
                }
                else{
                    findViewById(UIHelper.relLayoutsIds[currentContainerSelected]).setBackgroundColor(0x00000000);
                }
                hideChangeBtn();


                if(currentActivedBar==2)
                findViewById(R.id.filters_layout).setVisibility(View.INVISIBLE);
                if(currentActivedBar==1)
                findViewById(R.id.transparency_layout).setVisibility(View.INVISIBLE);
                selected_item =  v;
                if(currentTextSelected!=null&&currentTextSelected.getId()!=selected_item.getId()) {
                    currentTextSelected.getBorder().setBackgroundColor(0x00000000);
                    currentTextSelected.getResize().setVisibility(View.INVISIBLE);
                    currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
                    currentTextSelected.getMove().setVisibility(View.INVISIBLE);
                    if(textIsInShowMod)
                        textIsInShowMod=false;
                }
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                textFrame.setEnabled(true);
                currentTextSelected = getTextFromId(selected_item.getId());
                currentTextSelected.getBorder().setBackgroundResource(R.drawable.carcas_main_active);
                currentTextSelected.getDelete().setVisibility(View.VISIBLE);
                currentTextSelected.getResize().setVisibility(View.VISIBLE);
                currentTextSelected.getMove().setVisibility(View.VISIBLE);
                if(textIsInShowMod) {
                    currentTextSelected.getTextView().setVisibility(View.INVISIBLE);
                    EditText et = currentTextSelected.getEditText();
                    et.setVisibility(View.VISIBLE);
                    et.setSelection(et.getText().length());
                    et.setFocusable(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
                }
                else{
                    textIsInShowMod =true;
                }
                for (int i = 0; i < TextBarHelper.colorsLayout.getChildCount(); i++) {
                    ((ImageView) TextBarHelper.colorsLayout.getChildAt(i)).setImageResource(TextBarHelper.inactiveColors[i]);
                }
                ImageView imageView = (ImageView) ((LinearLayout) findViewById(R.id.colorsLayout)).getChildAt(currentTextSelected.getColor());
                imageView.setImageResource(TextBarHelper.activeColors[currentTextSelected.getColor()]);
                for (int i = 0; i < TextBarHelper.fontsLayout.getChildCount(); i++) {
                    TextView tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(i)).getChildAt(0);
                    tv.setBackgroundResource(R.drawable.font_inactived);
                    tv.setTextColor(0xFF93989C);
                    tv = (TextView) findViewById(TextBarHelper.indexText[i]);
                    tv.setBackgroundResource(R.drawable.index_inactiv);
                    tv.setTextColor(Color.BLACK);
                }
                TextView textView = (TextView) ((RelativeLayout)((LinearLayout) findViewById(R.id.fontslayout)).getChildAt(currentTextSelected.getFont())).getChildAt(0);
                textView.setBackgroundResource(R.drawable.font_actived);
                textView.setTextColor(0xFFFEFFFD);
                textView = (TextView) findViewById(TextBarHelper.indexText[currentTextSelected.getFont()]);
                textView.setBackgroundResource(R.drawable.index_text_actived);
                textView.setTextColor(0xffdadada);
                break;
            case MotionEvent.ACTION_UP:
                selected_item = null;
                touchFlag = false;
                textFrame.setEnabled(false);
                break;
        }
        return false;
    }

    public void setFramework(int numberOfFrame){

        for(int i=0;i<orignalBitmaps.size();i++){
            orignalBitmaps.set(i, null);
        }
        texts.clear();
        textCount = 0;
        currentTextSelected = null;
        FilterBarHelper.clearThumbnails(this);
        findViewById(R.id.transparency_bottom_bar).setAlpha(0.6f);
        findViewById(R.id.filter_bottom_bar).setAlpha(0.6f);
        findViewById(R.id.text_bottom_bar).setAlpha(0.6f);
        findViewById(R.id.share_bottom_bar).setAlpha(0.6f);
        currentFramework = numberOfFrame;
        currentContainerSelected = -1;
        findViewById(R.id.filters_layout).setVisibility(View.INVISIBLE);
        ((SeekBar) findViewById(R.id.seekbar)).setProgress(100);
        for(int i=0;i<UIHelper.imageViewsIds.length;i++)
            curentFilter[i] = id0;
        replacePic.setVisibility(View.INVISIBLE);
        deletePic.setVisibility(View.INVISIBLE);
        deletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProposeDialog(6);
            }
        });
        for(int i=0; i<frameworkScrolllayout.getChildCount();i++){
            if(i==numberOfFrame){
                ((ImageView)frameworkScrolllayout.getChildAt(i)).setImageResource(UIHelper.drawableFrameActiveIds[i]);

            }
            else{
                ((ImageView)frameworkScrolllayout.getChildAt(i)).setImageResource(UIHelper.drawableFrameInactiveIds[i]);
            }
        }
        frameworkContainer.removeAllViews();
        textBarSelected =false;
        frameworkContainer.inflate(this, UIHelper.layoutFrameworks[numberOfFrame], frameworkContainer);

        mainContainer = (RelativeLayout) findViewById(R.id.main_container);
        mainContainer.getLayoutParams().height= UIHelper.widhtFramePixels;
        mainContainer.getLayoutParams().width= UIHelper.widhtFramePixels;

        frameworkContainer.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        textFrame = (RelativeLayout) findViewById(R.id.containerForTexts);

        addButon = (RelativeLayout) findViewById(R.id.add_main_image);
        addButon.setOnClickListener(new ImageClickListener(UIHelper.MAIN_CONTAINER, this));

        switch(numberOfFrame){
            case 0: FrameFirstHelper.prepareFrame(this); containersCount=2; break;
            case 1: FrameTwoHelper.prepareFrame(this); containersCount = 3; break;
            case 2: FrameThreeHelper.prepareFrame(this); containersCount = 3; break;
            case 3: FrameFourHelper.prepareFrame(this); containersCount = 4; break;
            case 4: FrameFiveHelper.prepareFrame(this); containersCount = 4; break;
            case 5: FrameSixHelper.prepareFrame(this); containersCount = 5; break;
            case 6: FrameSevenHelper.prepareFrame(this); containersCount = 5; break;
            case 7: FrameEightHelper.prepareFrame(this); containersCount = 4; break;
            case 8: FrameNineHelper.prepareFrame(this); containersCount = 5; break;
        }
    }


    public void clearContainer(){
        int curCont = currentContainerSelected;
        TouchImageView t = (TouchImageView) findViewById(UIHelper.imageViewsIds[currentContainerSelected]);
        t.setImageDrawable(null);

        if(currentContainerSelected!=0) {
            RelativeLayout r = (RelativeLayout) findViewById(UIHelper.relLayoutsIds[currentContainerSelected]);
            ((RelativeLayout) r.getChildAt(0)).setVisibility(View.VISIBLE);
            deactivateCurrentContainer(currentContainerSelected);
            r.setBackgroundResource(R.drawable.carcas_second_inactive);
            r.setClickable(true);
        }
        else{
            addButon.callOnClick();
            addButon.setOnClickListener(new ImageClickListener( UIHelper.MAIN_CONTAINER, MainActivity.this));
            ImageView plus = (ImageView) findViewById(R.id.plus_button_main);
            ImageView select = (ImageView) findViewById(R.id.btn_select_main);
            select.setImageResource(R.drawable.btn_select_bkgr_off);
            plus.setVisibility(View.VISIBLE);
            RelativeLayout r = (RelativeLayout) findViewById(UIHelper.relLayoutsIds[0]);
            r.setBackgroundResource(R.drawable.carcas_main_inactive);
        }
        for(int j=UIHelper.imageViewsIds.length-1; j>=0; j--){
            TouchImageView ti = (TouchImageView) findViewById(UIHelper.imageViewsIds[j]);
            if(ti!=null && ti.getDrawable()!=null){
                if(j>0) {
                    setCurrentContainer(j);
                    break;
                }
                else{
                    addButon.callOnClick();
                    break;
                }
            }
            if (j==0) setBar(0);
        }
        int container = curCont==-1?0:curCont;
        orignalBitmaps.set(container,null);
        smallBitmaps.set(container,null);
        curentFilter[container]=id0;
        alphaList[container]=255;
        if(!existPhoto()){
            findViewById(R.id.transparency_bottom_bar).setAlpha(0.6f);
            findViewById(R.id.filter_bottom_bar).setAlpha(0.6f);
            findViewById(R.id.text_bottom_bar).setAlpha(0.6f);
            findViewById(R.id.share_bottom_bar).setAlpha(0.6f);
        }
        if(isOnlyBackgroundDownloaded()){
            findViewById(R.id.transparency_bottom_bar).setAlpha(0.6f);
        }
    }

    public void showChangeBtn(){
        replacePic.setVisibility(View.VISIBLE);
        deletePic.setVisibility(View.VISIBLE);
    }

    public void hideChangeBtn(){
        replacePic.setVisibility(View.INVISIBLE);
        deletePic.setVisibility(View.INVISIBLE);
    }


    public void setCurrentContainer(int container){
        if(currentActivedBar==1){
            ((ImageView) findViewById(UIHelper.imageViewsIdsBottomBar[1])).setImageResource(R.drawable.btn_transparancy_on);
            findViewById(UIHelper.layoutsSecundaryIds[1]).setVisibility(View.VISIBLE);
        }
        findViewById(UIHelper.imageViewsIdsBottomBar[1]).setAlpha(1f);
        showChangeBtn();
        if(currentTextSelected!=null) {
            currentTextSelected.getBorder().setBackgroundColor(0x00000000);
            currentTextSelected.getDelete().setVisibility(View.INVISIBLE);
            currentTextSelected.getResize().setVisibility(View.INVISIBLE);
            currentTextSelected.getMove().setVisibility(View.INVISIBLE);
            textIsInShowMod=false;
        }
        TextView tv;
        for (int k = 0; k < TextBarHelper.fontsLayout.getChildCount(); k++) {
            tv = (TextView) ((RelativeLayout) TextBarHelper.fontsLayout.getChildAt(k)).getChildAt(0);
            tv.setBackgroundResource(R.drawable.font_inactived);
            tv.setTextColor(0xFF93989C);
            tv = (TextView) findViewById(TextBarHelper.indexText[k]);
            tv.setBackgroundResource(R.drawable.index_inactiv);
            tv.setTextColor(Color.BLACK);
        }
        preparedForAddingText = true;
        if(currentActivedBar==2||currentActivedBar==1||currentActivedBar == 0) {
            ((RelativeLayout) findViewById(UIHelper.relLayoutsIds[container])).getChildAt(0).setVisibility(View.INVISIBLE);
            if(currentActivedBar==2)
            findViewById(R.id.filters_layout).setVisibility(View.VISIBLE);
        }
        replacePic.setOnClickListener(new ImageClickListener(container, this));
        int c = currentContainerSelected>-1?currentContainerSelected:container;
        RelativeLayout view = (RelativeLayout) findViewById(UIHelper.relLayoutsIds[c]);
        if (view != null) {
            if (((TouchImageView) view.getChildAt(c==0?0:1)).getDrawable() != null) {
                if(c==0)
                    view.setBackgroundResource(R.drawable.carcas_main_inactive);
                else
                    view.setBackgroundColor(0x00000000);
            } else {
                view.setBackgroundResource(R.drawable.carcas_second_inactive);
            }
        }
        currentContainerSelected = container;
        ((SeekBar) findViewById(R.id.seekbar)).setProgress((int) ((findViewById(UIHelper.imageViewsIds[container])).getAlpha() * 100));
        findViewById(UIHelper.relLayoutsIds[container]).setBackgroundResource(R.drawable.carcas_second_active);

        if(currentActivedBar==2) {
            FilterBarHelper.setThumbnails(this, currentContainerSelected, true);
        }

        showChangeBtn();
    }

    public void deactivateCurrentContainer(int image) {
        findViewById(UIHelper.relLayoutsIds[image]).setBackgroundColor(0x00000000);
        currentContainerSelected=-1;
        hideChangeBtn();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev)
    {
        View v = getCurrentFocus();
        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText
                )
        {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                if (getWindow() != null && getWindow().getDecorView() != null)
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    if(textFrame.getChildCount()>0){
                        for(int i=0;i<textFrame.getChildCount();i++) {
                            FrameLayout f = (FrameLayout) textFrame.getChildAt(i);
                            if(getTextFromId(f.getId()).equals(currentTextSelected) && selected_item!=null)
                                continue;
                            RelativeLayout r = (RelativeLayout) f.getChildAt(0);
                            TextView textview = (TextView) r.getChildAt(1);
                            EditText edittext = (EditText) r.getChildAt(0);
                            ImageView delete = (ImageView) f.getChildAt(2);
                            ImageView scale = (ImageView) f.getChildAt(1);
                            RelativeLayout move = (RelativeLayout) f.getChildAt(3);
                            r.setBackgroundColor(0x00000000);
                            textview.setText(edittext.getText().toString());
                            textview.setVisibility(View.VISIBLE);
                            edittext.setVisibility(View.INVISIBLE);
                            edittext.setVisibility(View.GONE);
                            delete.setVisibility(View.INVISIBLE);
                            scale.setVisibility(View.INVISIBLE);
                            move.setVisibility(View.INVISIBLE);
                            if(!textIsInShowMod)
                            textIsInShowMod = true;
                        }
                    }
                }
            }
        }

        if(menuLeftIsOpen){
            if(ev.getX()>(findViewById(R.id.leftMenuBar).getWidth()))
                animateLeftMenuBar();
        }
        return super.dispatchTouchEvent(ev);
    }

     public void setTextColor(int color){
         if(currentTextSelected!=null) {
             currentTextSelected.setColor(color);
             currentTextSelected.getEditText().setTextColor(TextBarHelper.colors[color]);
             currentTextSelected.getTextView().setTextColor(TextBarHelper.colors[color]);
         }
     }

    public void setFont(int font){
        if(currentTextSelected!=null) {
            currentTextSelected.setFont(font);
            currentTextSelected.getEditText().setTypeface(FontManager.getFonts().get(font));
            currentTextSelected.getTextView().setTypeface(FontManager.getFonts().get(font));
            if(textIsInShowMod)
                textIsInShowMod=false;
            currentTextSelected.getDelete().setVisibility(View.VISIBLE);
            currentTextSelected.getBorder().setBackgroundResource(R.drawable.carcas_main_active);
            currentTextSelected.getResize().setVisibility(View.VISIBLE);
            currentTextSelected.getMove().setVisibility(View.VISIBLE);
        }
    }

    public void showChangeFrameworkDialog(final int frame){
        ad = new AlertDialog.Builder(this);
        ad.setMessage("Are you sure you want to change to other templates?  All arrangements will be lost."); // сообщение
        ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                setFramework(frame);
            }
        });
        ad.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                adj.hide();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                adj.hide();
            }
        });
        adj = ad.create();
        adj.show();
    }

    public void showProposeDialog(final int i){
        ad = new AlertDialog.Builder(this);
        if(i ==0)
        ad.setMessage("Add photo"); // сообщение
        if(i==1)
            ad.setMessage("Добавьте фото на фон"); // сообщение
        if(i==2)
            ad.setMessage("First, activate the element on which you want to work"); // сообщение
        if(i==3)
            ad.setMessage("First, choose a frame"); // сообщение
        if(i==4)
            ad.setMessage("The background image cannot be transparent");
        ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(i==4){
                    int curCont = currentContainerSelected;
                    addButon.callOnClick();
                    if(curCont>0)
                        setCurrentContainer(curCont);
                }
                adj.hide();
            }
        });
        if(i==5){
            ad.setTitle("Exit");
            ad.setMessage("Attention, want to close? The arrangement will be lost!");
            ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adj.hide();
                }
            });
            ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        if(i==6){
            ad.setMessage("Remove photo?");
            ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearContainer();
                }
            });
            ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adj.hide();
                }
            });
        }
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            if(i==4){
                addButon.callOnClick();
            }
            adj.hide();
            }
        });
        adj = ad.create();
        adj.show();
    }


    @Override
    protected void onActivityResult(int container, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(FacebookSharing.callbackManager != null){
                FacebookSharing.callbackManager.onActivityResult(container, resultCode, data);
            }
            SaveHelper.deleteTempBitmap();
            if(container>-1&&container<(100+UIHelper.imageViewsIds.length))
                new BackgroundDownloadPhoto().execute(container, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SaveHelper.deleteTempBitmap();
    }

    @Override
    public void onBackPressed() {
        if(existPhoto())
            showProposeDialog(5);
        else
            super.onBackPressed();
    }

    private class BackgroundDownloadPhoto extends AsyncTask<Object, Void, Integer>{

        Bitmap b = null;
        Uri imageUri = null;
        File fi = null;
        String path ="";
        String allPaths[];
        Intent data;
        int container;
        int[] containers = null;
        int[] completedContainers = new int[5];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Object... params) {
            data = (Intent) params[1];
            container = (int) params[0];
            if(data!= null&& data.getStringArrayExtra("all_path")!=null) {
                curentFilter[container%100] = R.id.original;
                switch (currentFramework){
                    case 0:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.FULL_LAYOUT
                        };
                        break;
                    case 1:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.LEFT_CONTAINER,
                                UIHelper.RIGHT_CONTAINER
                        };
                        break;
                    case 2:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.TOP_CONTAINER,
                                UIHelper.BOTTOM_CONTAINER
                        };
                        break;
                    case 3:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.TOP_CONTAINER,
                                UIHelper.LEFT_BOTTOM_CONTAINER,
                                UIHelper.RIGHT_BOTTOM_CONTAINER
                        };
                        break;
                    case 4:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.LEFT_CONTAINER,
                                UIHelper.RIGHT_TOP_CONTAINER,
                                UIHelper.RIGHT_BOTTOM_CONTAINER
                        };
                        break;
                    case 5:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.LEFT_TOP_CONTAINER,
                                UIHelper.RIGHT_TOP_CONTAINER,
                                UIHelper.LEFT_BOTTOM_CONTAINER,
                                UIHelper.RIGHT_BOTTOM_CONTAINER
                        };
                        break;
                    case 6:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.LEFT_CONTAINER,
                                UIHelper.LEFT_CENTER_CONTAINER,
                                UIHelper.RIGHT_CENTER_LAYOUT,
                                UIHelper.RIGHT_CONTAINER
                        };
                        break;
                    case 7:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.LEFT_TOP_CONTAINER,
                                UIHelper.RIGHT_TOP_CONTAINER,
                                UIHelper.BOTTOM_CONTAINER
                        };
                        break;
                    case 8:
                        containers = new int[]{
                                UIHelper.MAIN_CONTAINER,
                                UIHelper.TOP_CONTAINER,
                                UIHelper.TOP_CENTER_CONTAINER,
                                UIHelper.BOTTOM_CENTER_LAYOUT,
                                UIHelper.BOTTOM_CONTAINER
                        };
                        break;
                }
                allPaths = data.getStringArrayExtra("all_path");
                for (int k =0 ,j=0 , l =0 ; k<allPaths.length; k++) {
                    String string = allPaths[k];
                    b = Utils.getThumbnail(MainActivity.this, string, 1080);
                    if(j==0 && k>0 &&((TouchImageView)MainActivity.this.findViewById(UIHelper.imageViewsIds[containers[j]])).getDrawable() != null){
                        j++;
                    }
                    if(k>0){
                        if(containers[j]==container%100)
                            j++;
                        completedContainers[l++] = containers[j];
                        orignalBitmaps.set(containers[j++],b);
                    }
                    else{
                        orignalBitmaps.set(container%100,b);
                        completedContainers[l++] = container%100;
                    }
                }
            }
            else{
                try {
                    fi = new File("/sdcard/temp.jpg");
                    imageUri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), fi.getAbsolutePath(), null, null));
//                    b = Utils.getThumbnail(MainActivity.this, imageUri, 1080);
                    b = Utils.decodeFile(fi.getAbsolutePath());
                    fi.delete();
                    orignalBitmaps.set(container, b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SaveHelper.deleteTempBitmap();
            return (container);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer<100) {
                ((TouchImageView) findViewById(UIHelper.imageViewsIds[integer])).setImageBitmap(b);
                if (integer == UIHelper.MAIN_CONTAINER) {
                    addButon.setOnClickListener(new ContainersHider(MainActivity.this));
                    ImageView plus = (ImageView) findViewById(R.id.plus_button_main);
                    plus.setVisibility(View.INVISIBLE);
                    FilterBarHelper.setThumbnails(MainActivity.this, integer, false);
                    FilterBarHelper.updateThumbnail(MainActivity.this, integer, false);
                    addButon.callOnClick();
                }
                if (existPhoto()) {
                    findViewById(R.id.transparency_bottom_bar).setAlpha(1f);
                    findViewById(R.id.filter_bottom_bar).setAlpha(1f);
                    findViewById(R.id.text_bottom_bar).setAlpha(1f);
                    findViewById(R.id.share_bottom_bar).setAlpha(1f);
                    if (orignalBitmaps.get(0) != null)
                        findViewById(R.id.share_bottom_bar).setAlpha(1f);
                }
                FilterBarHelper.setThumbnails(MainActivity.this, currentContainerSelected, false);
                if(currentContainerSelected>0)
                    deactivateCurrentContainer(currentContainerSelected);
            }
            else{
                for(int k = 0;k<allPaths.length;k++){
                    int image = completedContainers[k];
                    ((TouchImageView) findViewById(UIHelper.imageViewsIds[image])).setImageBitmap(orignalBitmaps.get(image));
                    findViewById(UIHelper.relLayoutsIds[image]).setClickable(false);
                    if (image == UIHelper.MAIN_CONTAINER) {
                        addButon.setOnClickListener(new ContainersHider(MainActivity.this));
                        ImageView plus = (ImageView) findViewById(R.id.plus_button_main);
                        plus.setVisibility(View.INVISIBLE);
                        FilterBarHelper.setThumbnails(MainActivity.this, image, true);
                        FilterBarHelper.updateThumbnail(MainActivity.this, image, true);
                        addButon.callOnClick();
                        if(k==allPaths.length-1) {
                            addButon.callOnClick();
                        }
                    }

                    if (image > 0) {
                        if(currentContainerSelected==0){
                            addButon.callOnClick();
                        }
                        setCurrentContainer(image);
                        FilterBarHelper.setThumbnails(MainActivity.this, image, true);
                        FilterBarHelper.updateThumbnail(MainActivity.this, image, true);
                    }
                    if (existPhoto()) {
                        findViewById(R.id.transparency_bottom_bar).setAlpha(1f);
                        findViewById(R.id.filter_bottom_bar).setAlpha(1f);
                        findViewById(R.id.text_bottom_bar).setAlpha(1f);
                        findViewById(R.id.share_bottom_bar).setAlpha(1f);
                        if (orignalBitmaps.get(0) != null)
                            findViewById(R.id.share_bottom_bar).setAlpha(1f);
                    }
                }
                if(allPaths.length==1&&completedContainers[0]==UIHelper.MAIN_CONTAINER){
                    addButon.callOnClick();
                    FilterBarHelper.setThumbnails(MainActivity.this, 0, true);
                    FilterBarHelper.updateThumbnail(MainActivity.this, 0, true);
                }
//                if(currentContainerSelected>0)
//                    deactivateCurrentContainer(currentContainerSelected);
            }
            if(isOnlyBackgroundDownloaded()){
                findViewById(R.id.transparency_bottom_bar).setAlpha(0.6f);
            }
//            if(currentActivedBar==1||currentActivedBar==2) {
//                setCurrentContainer(completedContainers[0]);
//            }
            findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
        }
    }

}