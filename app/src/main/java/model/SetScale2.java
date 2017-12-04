package model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Administrator on 2017/11/23.
 */

public class SetScale2 {
    public SetScale2()
    {

    }




    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {


        // load the origial Bitmap

        Bitmap BitmapOrg = bitmap;



        int width = BitmapOrg.getWidth();

        int height = BitmapOrg.getHeight();

        int newWidth = w;

        int newHeight = h;



        // calculate the scale

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;



        // create a matrix for the manipulation

        Matrix matrix = new Matrix();

        // resize the Bitmap

        matrix.postScale(scaleWidth, scaleHeight);

        // if you want to rotate the Bitmap
        // matrix.postRotate(45);



        // recreate the new Bitmap

        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,

                height, matrix, true);



        // make a Drawable from Bitmap to allow to set the Bitmap

        // to the ImageView, ImageButton or what ever

        return resizedBitmap;



    }
}
