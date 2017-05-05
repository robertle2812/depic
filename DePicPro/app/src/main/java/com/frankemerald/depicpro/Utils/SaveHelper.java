package com.frankemerald.depicpro.Utils;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.frankemerald.depicpro.MainActivity;
import com.frankemerald.depicpro.Models.Text;
import com.frankemerald.depicpro.R;
import com.frankemerald.depicpro.UIHelpers.UIHelper;
import com.frankemerald.depicpro.Dialogs.DialogSaveImage;
import com.frankemerald.depicpro.Views.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mark on 27.11.2015.
 */
public class SaveHelper {

    public static Bitmap image;
    public static MainActivity activity;
    public static String fileNameT;

    public static void showChooseDialog(final MainActivity activity){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String currentDateandTime = "depic_" + sdf.format(new Date());
        DialogSaveImage dialog = new DialogSaveImage(activity,currentDateandTime);
        dialog.SetOnItemViewClickListener(new DialogSaveImage.OnOkClickListener() {
            @Override
            public void onClick(String name) {
                saveToCard(activity,name);
            }
        });
        dialog.show();
    }

    public static boolean saveBitmap(MainActivity activity, String name) {
        String nameFinal = name.replace(' ','_');
        Bitmap bitmap = null;
        OutputStream output;

        image = makeBitmap(activity);

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/DCIM/Camera/");
        dir.mkdirs();
        File file = new File(dir, nameFinal + ".jpg");
        int i = 1;
        String term="";
        do {
            file = new File(dir, nameFinal + term + ".jpg");
            fileNameT = nameFinal+term;
            term = "_" + i++;
        }while(file.exists());
        try {
            output = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void saveToCard(MainActivity activity, String filename){
        if(saveBitmap(activity, filename)) {
            MediaScannerConnection.scanFile(activity, new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + fileNameT + ".jpg" }, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri)
                {
                }
            });
            Toast.makeText(activity, "Saved Succesfully", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(activity, "Save failed", Toast.LENGTH_LONG).show();
        }
    }

   public static Bitmap makeBitmap(MainActivity activity){
       TouchImageView ti = (TouchImageView) activity.findViewById(R.id.main_image);
       ti.setDrawingCacheEnabled(false);
       ti.setDrawingCacheEnabled(true);
       ti.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
       Bitmap b;
       if(ti.getDrawable()==null){
           RelativeLayout main = (RelativeLayout) activity.findViewById(R.id.main_container);
           b= Bitmap.createBitmap(main.getWidth()-(int)Utils.convertDpToPixel(5), main.getHeight()-(int)Utils.convertDpToPixel(5), Bitmap.Config.ARGB_8888);
           Canvas c = new Canvas(b);
           c.drawColor(Color.BLACK);
           Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
           p.setColor(0xFFA3A09C);
           RectF r = new RectF(0,0,b.getWidth(),b.getHeight());
           c.drawRoundRect(r,10,10,p);
       }
       else{
           b = ti.getDrawingCache();
       }
       int bheight = b.getHeight(), bwidth = b.getWidth();
       Canvas canvas = new Canvas(b);

       Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
       p.setStyle(Paint.Style.STROKE);

       switch(activity.currentFramework){
           case 0:
           {
               View v = activity.findViewById(R.id.fullImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap full = v.getDrawingCache();
               if(full!=null){
                   int fullHeight = full.getHeight(), fullWidth = full.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.FULL_LAYOUT]);
                   canvas.drawBitmap(full, (bwidth-fullWidth)/2, (bheight- fullHeight)/2, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 1:
           {
               View v = activity.findViewById(R.id.leftImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap left = v.getDrawingCache();
               if(left != null) {
                   int leftheight = left.getHeight(), leftWidth = left.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_CONTAINER]);
                   canvas.drawBitmap(left, (bwidth / 2 - leftWidth)*10f / 13.5f, (bheight - leftheight) / 2, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap right = v.getDrawingCache();
               if(right!=null) {
                   int rightHeight = right.getHeight(), rightWidth = right.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_CONTAINER]);
                   canvas.drawBitmap(right, bwidth / 2 + (bwidth / 2 - rightWidth)*3.5f/ 13.5f, (bheight - rightHeight) / 2, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 2:
           {
               View v = activity.findViewById(R.id.topImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap top = v.getDrawingCache();
               if(top!=null) {
                   int topHeight = top.getHeight(), topWidth = top.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.TOP_CONTAINER]);
                   canvas.drawBitmap(top, (bwidth - topWidth) / 2, (bheight / 2 - topHeight) *10f/ 13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.bottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottom = v.getDrawingCache();
               if(bottom!=null) {
                   int bottomHeight = bottom.getHeight(), bottomWidth = bottom.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottom, (bwidth - bottomWidth) / 2, bheight / 2 + (bheight / 2 - bottomHeight) *3.5f/ 13.5f, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 3:
           {
               View v = activity.findViewById(R.id.topImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap top = v.getDrawingCache();
               if(top!=null) {
                   int topHeight = top.getHeight(), topWidth = top.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.TOP_CONTAINER]);
                   canvas.drawBitmap(top, (bwidth - topWidth) / 2, (bheight / 2 - topHeight) *10f/ 13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.leftBottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottomLeft = v.getDrawingCache();
               if(bottomLeft!=null) {
                   int bottomLeftHeight = bottomLeft.getHeight(), bottomLeftWidth = bottomLeft.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottomLeft, (bwidth / 2 - bottomLeftWidth) *10f/13.5f, bheight / 2 + (bheight / 2 - bottomLeftHeight) *3.5f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightBottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottomRight = v.getDrawingCache();
               if(bottomRight!=null) {
                   int bottomRightHeight = bottomRight.getHeight(), bottomRightWidth = bottomRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottomRight, bwidth / 2 + (bwidth / 2 - bottomRightWidth)*3.5f /13.5f, bheight / 2 + (bheight / 2 - bottomRightHeight) *3.5f/ 13.5f, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 4:
           {
               View v = activity.findViewById(R.id.leftImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap left =v.getDrawingCache();
               if(left!=null) {
                   int leftheight = left.getHeight(), leftWidth = left.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_CONTAINER]);
                   canvas.drawBitmap(left, (bwidth / 2 - leftWidth) *10f/13.5f, (bheight - leftheight) /2, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightTopImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topRight = v.getDrawingCache();
               if(topRight!=null) {
                   int topRightHeight = topRight.getHeight(), topRightWidth = topRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_TOP_CONTAINER]);
                   canvas.drawBitmap(topRight, bwidth / 2 + (bwidth / 2 - topRightWidth) *3.5f/13.5f, (bheight / 2 - topRightHeight) *10f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightBottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottomRight = v.getDrawingCache();
               if(bottomRight!=null) {
                   int bottomRightHeight = bottomRight.getHeight(), bottomRightWidth = bottomRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottomRight, bwidth / 2 + (bwidth / 2 - bottomRightWidth) *3.5f/13.5f, bheight / 2 + (bheight / 2 - bottomRightHeight) *3.5f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 5:
           {
               View v = activity.findViewById(R.id.leftTopImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topleft = v.getDrawingCache();
               if(topleft!=null) {
                   int topLeftHeight = topleft.getHeight(), topLeftWidth = topleft.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_TOP_CONTAINER]);
                   canvas.drawBitmap(topleft, (bwidth / 2 - topLeftWidth) *10f/13.5f, (bheight / 2 - topLeftHeight) *10f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightTopImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topRight =v.getDrawingCache();
               if(topRight!=null) {
                   int topRightHeight = topRight.getHeight(), topRightWidth = topRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_TOP_CONTAINER]);
                   canvas.drawBitmap(topRight, bwidth / 2 + (bwidth / 2 - topRightWidth) *3.5f/13.5f, (bheight / 2 - topRightHeight) *10f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.leftBottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottomLeft = v.getDrawingCache();
               if(bottomLeft!=null) {
                   int bottomLeftHeight = bottomLeft.getHeight(), bottomLeftWidth = bottomLeft.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottomLeft, (bwidth / 2 - bottomLeftWidth) *10f/13.5f, bheight / 2 + (bheight / 2 - bottomLeftHeight) *3.5f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightBottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottomRight = v.getDrawingCache();
               if(bottomRight!=null) {
                   int bottomRightHeight = bottomRight.getHeight(), bottomRightWidth = bottomRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottomRight, bwidth / 2 + (bwidth / 2 - bottomRightWidth) *3.5f/13.5f, bheight / 2 + (bheight / 2 - bottomRightHeight) *3.5f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 6:
           {
               View v = activity.findViewById(R.id.leftImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap left = v.getDrawingCache();
               if(left!=null) {
                   int leftheight = left.getHeight(), leftWidth = left.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_CONTAINER]);
                   canvas.drawBitmap(left, (bwidth / 2 / 2 - leftWidth) *10f/10.25f, (bheight - leftheight) / 2, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap right = v.getDrawingCache();
               if(right!=null) {
                   int rightHeight = right.getHeight(), rightWidth = right.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_CONTAINER]);
                   canvas.drawBitmap(right, bwidth - bwidth/2/2 + (bwidth / 2 / 2 - rightWidth) *0.25f/10.25f, (bheight - rightHeight) / 2, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.leftCenterImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap leftCenter = v.getDrawingCache();
               if(leftCenter!=null) {
                   int leftCenterheight = leftCenter.getHeight(), leftCenterWidth = leftCenter.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_CENTER_CONTAINER]);
                   canvas.drawBitmap(leftCenter, bwidth / 2 - bwidth/2/2 + ((bwidth / 2 / 2) - leftCenterWidth) *6.75f/10.25f, (bheight - leftCenterheight) / 2, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightCenterImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap rightCenter = v.getDrawingCache();
               if(rightCenter!=null) {
                   int rightCenterHeight = rightCenter.getHeight(), rightcenterWidth = rightCenter.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_CENTER_LAYOUT]);
                   canvas.drawBitmap(rightCenter, bwidth / 2 + (bwidth / 2 / 2 - rightcenterWidth)*3.5f /10.25f, (bheight - rightCenterHeight) / 2, p);
                   p.setAlpha(255);
               }
           }
           break;
           case 7:
           {
               View v = activity.findViewById(R.id.leftTopImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topleft =v.getDrawingCache();
               if(topleft!=null) {
                   int topLeftHeight = topleft.getHeight(), topLeftWidth = topleft.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.LEFT_TOP_CONTAINER]);
                   canvas.drawBitmap(topleft, (bwidth / 2 - topLeftWidth) *10f/13.5f, (bheight / 2 - topLeftHeight) *10f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.rightTopImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topRight = v.getDrawingCache();
               if(topRight!=null) {
                   int topRightHeight = topRight.getHeight(), topRightWidth = topRight.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.RIGHT_TOP_CONTAINER]);
                   canvas.drawBitmap(topRight, bwidth / 2 + (bwidth / 2 - topRightWidth)*3.5f/13.5f, (bheight / 2 - topRightHeight) *10f/13.5f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.bottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottom = v.getDrawingCache();
               if(bottom!=null) {
                   int bottomHeight = bottom.getHeight(), bottomWidth = bottom.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottom, (bwidth - bottomWidth) / 2, bheight / 2 + (bheight / 2 - bottomHeight) *3.5f/13.5f, p);

                   p.setAlpha(255);
               }
           }
           break;
           case 8:
           {
               View v = activity.findViewById(R.id.topImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap top = v.getDrawingCache();
               if(top!=null) {
                   int topHeight = top.getHeight(), topWidth = top.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.TOP_CONTAINER]);
                   canvas.drawBitmap(top, (bwidth - topWidth) / 2, (bheight / 2 / 2 - topHeight) *10f/10.25f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.bottomImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap bottom = v.getDrawingCache();
               if(bottom!=null) {
                   int bottomHeight = bottom.getHeight(), bottomWidth = bottom.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.BOTTOM_CONTAINER]);
                   canvas.drawBitmap(bottom, (bwidth - bottomWidth) / 2, bheight - bheight / 2 / 2 + (bheight / 2 / 2 - bottomHeight) *0.25f/10.25f, p);
                   p.setAlpha(255);
               }
           }
           {
               View v = activity.findViewById(R.id.topCenterImage);
               v.setDrawingCacheEnabled(false);
               v.setDrawingCacheEnabled(true);
               v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
               Bitmap topCenter = v.getDrawingCache();
               if(topCenter!=null) {
                   int topCenterHeight = topCenter.getHeight(), topCenterWidth = topCenter.getWidth();
                   p.setAlpha(activity.alphaList[UIHelper.TOP_CENTER_CONTAINER]);
                   canvas.drawBitmap(topCenter, (bwidth - topCenterWidth) / 2, bheight / 2 - bheight / 2 / 2 + (bheight / 2 / 2 - topCenterHeight) *6.75f/10.25f, p);
                   p.setAlpha(255);
               }
        }
        {
            View v = activity.findViewById(R.id.bottomCenterImage);
            v.setDrawingCacheEnabled(false);
            v.setDrawingCacheEnabled(true);
            v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bottomCenter =v.getDrawingCache();
            if(bottomCenter!=null) {
            int bottomCenterHeight = bottomCenter.getHeight(), bottomCenterWidth = bottomCenter.getWidth();
            p.setAlpha(activity.alphaList[UIHelper.BOTTOM_CENTER_LAYOUT]);
            canvas.drawBitmap(bottomCenter, (bwidth - bottomCenterWidth) / 2, bheight / 2 + (bheight / 2 / 2 - bottomCenterHeight) *3.5f/10.25f, p);
            p.setAlpha(255);
        }

        }
        }

        for(int i=0; i<activity.texts.size();i++){
            Text t = activity.texts.get(i);
            View v = activity.texts.get(i).getTextView();
            v.setDrawingCacheEnabled(false);
            v.setDrawingCacheEnabled(true);
            v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmapText = v.getDrawingCache();
            float leftRelative = v.getLeft()+ Utils.convertDpToPixel(9), topRelative = v.getTop() + Utils.convertDpToPixel(9), leftAbsolute = t.getCoord()[0], topAbsolute = t.getCoord()[1];
            canvas.drawBitmap(bitmapText, leftAbsolute+leftRelative, topAbsolute+topRelative, p);
        }

       ImageView watermark = (ImageView) activity.findViewById(R.id.watermark);
       watermark.setDrawingCacheEnabled(true);
       watermark.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
       Bitmap wm = watermark.getDrawingCache();
       float x = b.getWidth() - wm.getWidth() - Utils.convertDpToPixel(9);
       float y = b.getHeight() - wm.getHeight() - Utils.convertDpToPixel(9);
       canvas.drawBitmap(wm, x, y, p);

        return b;
        }



public static void deleteTempBitmap(){
        String filename = "temp";
        String mediaPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"+filename+".jpg";
        File media = new File(mediaPath);
        if(media.exists())
        media.delete();

        }
        }


