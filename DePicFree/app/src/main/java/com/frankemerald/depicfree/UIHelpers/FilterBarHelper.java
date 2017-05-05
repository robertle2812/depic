package com.frankemerald.depicfree.UIHelpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.R;
import com.frankemerald.depicfree.Utils.Utils;
import com.frankemerald.depicfree.Views.TouchImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mark on 28.10.2015.
 */
public final class FilterBarHelper {

    static  final int[] filtersViewId = {
        R.id.original,
        R.id.blackwhite,
        R.id.coffee,
        R.id.filter2,
        R.id.toast,
        R.id.winter,
        R.id.hdr,
        R.id.filter1,
        R.id.vintage
};
    static final int[] indexId = {
            R.id.indexFilter1,
            R.id.indexFilter2,
            R.id.indexFilter3,
            R.id.indexFilter4,
            R.id.indexFilter5,
            R.id.indexFilter6,
            R.id.indexFilter7,
            R.id.indexFilter8,
            R.id.indexFilter9
    };

    private static Bitmap bitmap;

    public static final int FIRST_BLOCKED_FILTER=4;

    private static int getFilterCount(int id){
        for(int i=0;i<filtersViewId.length;i++){
            if(id==filtersViewId[i])
                return i;
        }
        return 0;
    }

    public static void init(final MainActivity activity) {
        for(int i=0; i<UIHelper.imageViewsIds.length; i++){
            activity.smallBitmaps.add(i, null);
        }
        clearThumbnails(activity);


        LinearLayout filterL = (LinearLayout) activity.findViewById(R.id.filters_layout);
        filterL.setVisibility(View.INVISIBLE);
        for (int i = 0; i < filtersViewId.length; i++) {
            activity.findViewById(filtersViewId[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getFilterCount(v.getId())>= FIRST_BLOCKED_FILTER){
                        activity.showProposeDialog(7);
                        return;
                    }
                    if(activity.currentContainerSelected>-1)
                        setFilter(activity, v.getId(), true);
                }
            });
        }
    }

    public static void clearThumbnails(MainActivity activity){
        for(int i=0; i<UIHelper.imageViewsIds.length;i++){
            activity.smallBitmaps.set(i, null);
        }
    }

    private static void setFilter(MainActivity context, int filter, boolean clickFilter) {
        context.curentFilter[context.currentContainerSelected] = filter;
        TouchImageView i = (TouchImageView) context.findViewById(UIHelper.imageViewsIds[context.currentContainerSelected == -1 ? 0 : context.currentContainerSelected]);
        Bitmap b = context.orignalBitmaps.get(context.currentContainerSelected==-1?0:context.currentContainerSelected);
        switch(filter){
            case R.id.original:
                i.setFilterBitmap(b);
                break;
            case R.id.coffee:
                i.setFilterBitmap(applyCoffeeFilter(b));
                break;
            case R.id.vintage:
                i.setFilterBitmap(applyVintageFilter(b));
                break;
            case R.id.toast:
                i.setFilterBitmap(applyToastFilter(b));
                break;
            case R.id.winter:
                i.setFilterBitmap(applyWinterFilter(b));
                break;
            case R.id.hdr:
                i.setFilterBitmap(applyHDRFilter(b));
                break;
            case R.id.filter1:
                i.setFilterBitmap(applyMyFilter1(b));
                break;
            case R.id.filter2:
                i.setFilterBitmap(applyMyFilter2(b));
                break;
            case R.id.blackwhite:
                i.setFilterBitmap(applyBlackWhiteFilter(b));
        }
        setActiveFilter(context, context.currentContainerSelected, clickFilter);

    }

    public static void setActiveFilter(MainActivity activity, int container, boolean clickFromTopPanel){
        TextView t;
        for(int i = 0 ; i<filtersViewId.length; i++){
            ((CircleImageView) activity.findViewById(filtersViewId[i])).setBorderColor(0xffdadada);
            t = (TextView) activity.findViewById(indexId[i]);
            t.setBackgroundResource(R.drawable.index_inactiv);
            t.setTextColor(Color.BLACK);
        }
//        if(!clickFromTopPanel)
//            return;
        ((CircleImageView) activity.findViewById(activity.curentFilter[container==-1?0:container])).setBorderColor(0xFFDC9C16);
        int index = 0;
        switch(activity.curentFilter[container==-1?0:container]){
            case R.id.original:index = 0;break;
            case R.id.blackwhite:index=1;break;
            case R.id.coffee:index = 2;break;
            case R.id.filter2:index = 3;break;
            case R.id.toast:index = 4;break;
            case R.id.winter:index =5;break;
            case R.id.hdr:index = 6;break;
            case R.id.filter1:index = 7;break;
            case R.id.vintage:index = 8;break;

        }
        t = (TextView) activity.findViewById(indexId[index]);
        t.setBackgroundResource(R.drawable.font_actived);
        t.setTextColor(0xffdadada);
    }

    public static Bitmap applyCoffeeFilter(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        float brightness =-51f;
        colorMatrix.set(new float[]{0, 0, 0, 0, brightness, 0,
                0, 0, 0, brightness, 0, 0, 0, 0,
                brightness, 0, 0, 0, 1, 0});
        colorMatrix.setSaturation(0.5f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap applyBlackWhiteFilter(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap applyVintageFilter(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);
        canvas.drawColor(0xffe0d9b4, PorterDuff.Mode.MULTIPLY);
        return bitmap;
    }

    public static Bitmap applyWinterFilter(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(0.8f, 0.8f, 1.2f, 1);
        paint.setColorFilter(new ColorMatrixColorFilter(colorScale));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap applyToastFilter(Bitmap original) {
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1.2f, 0.8f, 0.8f, 1);
        paint.setColorFilter(new ColorMatrixColorFilter(colorScale));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap applyHDRFilter(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(1.5f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap applyMyFilter1(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        float contrast = 1.3f;
        float brightness =-30f;
        cm.set(new float[]{contrast, 0, 0, 0, brightness, 0,
                contrast, 0, 0, brightness, 0, 0, contrast, 0,
                brightness, 0, 0, 0, 1, 0});

        cm.setScale(1f, 1f, 1.3f, 1);

        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        Matrix matrix = new Matrix();
        canvas.drawBitmap(original, matrix, paint);
        return bitmap;
    }

    public static Bitmap applyMyFilter2(Bitmap original){
        bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        float contrast = 1.6f;
        float brightness =-50f;
        cm.set(new float[]{contrast, 0, 0, 0, brightness, 0,
                contrast, 0, 0, brightness, 0, 0, contrast, 0,
                brightness, 0, 0, 0, 1, 0});

        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        Matrix matrix = new Matrix();
        canvas.drawBitmap(original, matrix, paint);
        return bitmap;
    }

    public static void setThumbnails(MainActivity activity, int image, boolean fromFilter ){
        if(activity.smallBitmaps.get(image==-1?0:image)==null) {
            new GetterThumbnail(activity, image==-1?0:image, fromFilter).execute();
        }
        else{
            new SetterThumbnails(activity, activity.smallBitmaps.get(image==-1?0:image),image==-1?0:image, fromFilter).execute();
        }
    }

    public static void updateThumbnail(MainActivity activity, int container, boolean fromFilter){
        new GetterThumbnail(activity, container, fromFilter).execute();
    }

    private static class GetterThumbnail extends AsyncTask<Void, Void, Bitmap>{

        public GetterThumbnail(MainActivity activity, int container, boolean fromFilter){
            this.activity = activity;
            this.container = container;
            this.fromFilter = fromFilter;
        }

        MainActivity activity = null;
        int container;
        boolean fromFilter;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            (activity.findViewById(R.id.original)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.coffee)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.vintage)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.toast)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.winter)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.hdr)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.filter1)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.filter2)).setVisibility(View.INVISIBLE);
            (activity.findViewById(R.id.blackwhite)).setVisibility(View.INVISIBLE);

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap thumbnail = Utils.getThumbnail(activity, container);
            return thumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap thumbnail) {
            super.onPostExecute(thumbnail);
            if(thumbnail==null) return;
            activity.smallBitmaps.set(container, thumbnail);
            ((CircleImageView) activity.findViewById(R.id.original)).setImageBitmap(thumbnail);
            (activity.findViewById(R.id.original)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.coffee)).setImageBitmap(applyCoffeeFilter(thumbnail));
            (activity.findViewById(R.id.coffee)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.vintage)).setImageBitmap(applyVintageFilter(thumbnail));
            (activity.findViewById(R.id.vintage)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.toast)).setImageBitmap(applyToastFilter(thumbnail));
            (activity.findViewById(R.id.toast)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.winter)).setImageBitmap(applyWinterFilter(thumbnail));
            (activity.findViewById(R.id.winter)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.hdr)).setImageBitmap(applyHDRFilter(thumbnail));
            (activity.findViewById(R.id.hdr)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.filter1)).setImageBitmap(applyMyFilter1(thumbnail));
            (activity.findViewById(R.id.filter1)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.filter2)).setImageBitmap(applyMyFilter2(thumbnail));
            (activity.findViewById(R.id.filter2)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.blackwhite)).setImageBitmap(applyBlackWhiteFilter(thumbnail));
            (activity.findViewById(R.id.blackwhite)).setVisibility(View.VISIBLE);
            setActiveFilter(activity,container, fromFilter);

        }
    }

    private static class SetterThumbnails extends AsyncTask<Void, Void, Void>{

        MainActivity activity;
        Bitmap thumbnail;
        int container;
        boolean fromFilter;

        public SetterThumbnails(MainActivity activity, Bitmap thumbnail, int image, boolean fromFilter){
            this.activity = activity;
            this.thumbnail = thumbnail;
            this.container = image;
            this.fromFilter = fromFilter;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((CircleImageView) activity.findViewById(R.id.original)).setImageBitmap(thumbnail);
            (activity.findViewById(R.id.original)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.coffee)).setImageBitmap(applyCoffeeFilter(thumbnail));
            (activity.findViewById(R.id.coffee)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.vintage)).setImageBitmap(applyVintageFilter(thumbnail));
            (activity.findViewById(R.id.vintage)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.toast)).setImageBitmap(applyToastFilter(thumbnail));
            (activity.findViewById(R.id.toast)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.winter)).setImageBitmap(applyWinterFilter(thumbnail));
            (activity.findViewById(R.id.winter)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.hdr)).setImageBitmap(applyHDRFilter(thumbnail));
            (activity.findViewById(R.id.hdr)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.filter1)).setImageBitmap(applyMyFilter1(thumbnail));
            (activity.findViewById(R.id.filter1)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.filter2)).setImageBitmap(applyMyFilter2(thumbnail));
            (activity.findViewById(R.id.filter2)).setVisibility(View.VISIBLE);
            ((CircleImageView)activity.findViewById(R.id.blackwhite)).setImageBitmap(applyBlackWhiteFilter(thumbnail));
            (activity.findViewById(R.id.blackwhite)).setVisibility(View.VISIBLE);

            setActiveFilter(activity,container, fromFilter);
        }
    }
}
