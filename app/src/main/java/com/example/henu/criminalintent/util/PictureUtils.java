package com.example.henu.criminalintent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by mqh in 12/4/2018
 * 注意DialogFragment是SupportV4包下的fragment
 */
public class PictureUtils {
    public static Bitmap getScaledBitmap(String path,int desWidth,int destHeight)
    {
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//设置为true用于获取图片的长度和宽度信息
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Figure out how much to scale down by
        int inSampleSize = 1; //缩放比例
        if(srcHeight > destHeight || srcWidth >desWidth){
            if(srcWidth > srcHeight)
            {
                inSampleSize = Math.round(srcHeight/destHeight);
            }else{
                inSampleSize = Math.round(srcWidth/desWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and create final bitmap
        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        //先确定屏幕的尺寸
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        //然后按此缩放图片
        return getScaledBitmap(path,size.x,size.y);
    }
}
