package com.alfredthomas.hotelfinder.Util.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.alfredthomas.hotelfinder.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class GetImageTask extends AsyncTask<Void,Void,Bitmap> {
    WeakReference<ImageView> imageViewWeakReference;
    String imageURL;

    public GetImageTask (String url, ImageView imageView)
    {
        //url as defined in JSON and parsed to the Hotel object
        imageURL = url;
        imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected void onPreExecute() {
        //set fallback image
        ImageView iv = imageViewWeakReference.get();

        //free use image from https://pixabay.com/en/room-hotel-motel-bed-bedroom-40309/
        Drawable placeholder = iv.getContext().getResources().getDrawable(R.drawable.hotel_fallback);
        iv.setImageDrawable(placeholder);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            //open connection to url and download bitmap
            URL url = new URL(imageURL);
            InputStream inputStream = url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        }
        catch(Exception e)
        {
            Log.e("GetImageTask",e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewWeakReference.get();

        //if we have an imageview
        if(imageView!=null)
        {
            //if the bitmap returned exists
            if(bitmap!=null)
            {
                //set the bitmap on the imageview
                imageView.setImageBitmap(bitmap);
            }
        }

    }
}
