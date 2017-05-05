package com.frankemerald.depicpro.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.ArrayList;

/**
 * Created by Mark on 06.11.2015.
 */
public final class FontManager {

    private static ArrayList<Typeface> fontList;

    public static void init(Context context){
        fontList = new ArrayList<Typeface>();
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Ailerons.ttf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"BOMB.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Cocogoose_Outlined_trial.ttf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Eutopia.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Galpon_Black.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Gecko_PersonalUseOnly.ttf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Havana.ttf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"HelveticaInseratLTStd_Roman.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Intro.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"pier_regular.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"Squares_Bold_Free.otf"));
        fontList.add(Typeface.createFromAsset(context.getAssets(),"The_Northern_Block_Ltd_Webnar_Bold.otf"));
    }

    public static ArrayList<Typeface> getFonts(){
        return fontList;
    }

}
