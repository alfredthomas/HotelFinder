package com.alfredthomas.hotelfinder.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredthomas.hotelfinder.FullScreenImageActivity;
import com.alfredthomas.hotelfinder.Hotel;
import com.alfredthomas.hotelfinder.Util.Async.GetImageTask;
import com.alfredthomas.hotelfinder.Util.ImageLoader;

public class HotelView extends ViewBase {
    public TextView hotelName;
    public ImageView hotelImage;
    GetImageTask getImageTask = null;

    public HotelView(final Context context)
    {
        super(context);

        //two things to show: imageview for the image url and a text view for the hotel name
        hotelName = new TextView(context);
        this.addView(hotelName);
        hotelName.setGravity(Gravity.CENTER_VERTICAL);
        hotelName.setTextSize(20f);

        hotelImage = new ImageView(context);
        this.addView(hotelImage);
//        hotelImage.setBackgroundColor(Color.CYAN);

        //@todo- Finish loading image if we are showing placeholder
        hotelImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoader.saveToFile(((BitmapDrawable)hotelImage.getDrawable()).getBitmap(),hotelName.getText().toString(),context.getApplicationInfo().dataDir);
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                intent.putExtra("image",hotelName.getText());
                context.startActivity(intent);
            }
        });
    }

    //used for replacing items in list
    public void setData(Hotel hotel)
    {
        hotelName.setText(hotel.getName());

        //use Picasso to load images (generally faster and allows for caching)
        ImageLoader.fromURLBetter(hotelImage,hotel.getURL());

//        //manually manage loading images
//        //cancel current running task if we are replacing in scroll
//        if(getImageTask!=null && getImageTask.getStatus() == AsyncTask.Status.RUNNING)
//            getImageTask.cancel(true);
//        //load image using async task
//        getImageTask = ImageLoader.fromURL(hotelImage,hotel.getURL());
//        getImageTask.execute();
    }

/*           ___________________________________
    Layout: |          |                        |
            |          |                        |
            |  <IMG>   |      <HOTEL NAME>      |
            |          |                        |
            |__________|________________________|



 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int padding = (int)(5 * getResources().getDisplayMetrics().density);


        int fourth = width/4;

        //set image to 1/4 the total width;
        measureView(hotelImage,padding,padding,fourth-(2*padding),height-(2*padding));
        measureView(hotelName, fourth + padding, padding,width-fourth-(2*padding),height-(2*padding));

    }
}
