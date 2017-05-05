package com.frankemerald.depicfree.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.UIHelpers.UIHelper;
import com.frankemerald.depicfree.Views.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Mark on 09.12.2015.
 */
public class SaveTestHelper {


    public static Bitmap image;

    public static Bitmap makeBitmap(MainActivity activity){

        TouchImageView image = (TouchImageView)activity.findViewById(UIHelper.imageViewsIds[0]);
        Bitmap original = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.set(image.getTestMatrix());
//        matrix.postScale(scale, scale);
        Bitmap result = Bitmap.createBitmap(original, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

//        Canvas c = new Canvas(result);


        return result;
    }

    public static boolean saveBitmap(MainActivity activity, String name) {

//        Canvas c = new Canvas

        String nameFinal = name.replace(' ','_');
        Bitmap bitmap = null;
        OutputStream output;

        image = makeBitmap(activity);

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/DePic/");
        dir.mkdirs();
        File file = new File(dir, nameFinal + ".jpg");
        int i = 1;
        String term="";
        do {
            file = new File(dir, nameFinal + term + ".jpg");
            term = "_" + i++;
        }while(file.exists());
//        File file = new File(dir, "test.jpg");
        try {
            output = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
//            dialog.hide();
//            Toast.makeText(activity, "Saved Succesfully", Toast.LENGTH_LONG).show();
//            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
