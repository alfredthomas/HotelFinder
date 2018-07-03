package com.alfredthomas.hotelfinder.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.alfredthomas.hotelfinder.R;
import com.alfredthomas.hotelfinder.Util.Async.GetImageTask;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;

//static class used to load/save images from web/file
public class ImageLoader {

    //load image from file (for full screen images)
    public static void fromFile(ImageView imageView, String filename, String path, int fallback)
    {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path+"/"+filename);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(fallback));
        }
    }

    //use async task to load image from url
    public static GetImageTask fromURL(ImageView imageView, String imageURL)
    {
        GetImageTask getImageTask = new GetImageTask(imageURL,imageView);
        return getImageTask;
    }

    //use picasso since it can better load images
    //http://square.github.io/picasso/
    public static void fromURLBetter(ImageView imageView,String imageURL)
    {
        Picasso.get().load(imageURL).placeholder(R.drawable.hotel_fallback).into(imageView);
    }

    //saving image to file to show again fullscreen (reduces web calls)
    public static void saveToFile(Bitmap bitmap, String filename, String path)
    {
        FileOutputStream outputStream = null;

        try{
            outputStream = new FileOutputStream(path+"/"+filename+".png");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
