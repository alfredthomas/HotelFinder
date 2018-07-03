package com.alfredthomas.hotelfinder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alfredthomas.hotelfinder.Util.ImageLoader;


public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filename = getIntent().getStringExtra("image");
        ImageView imageView = new ImageView(this);
        ImageLoader.fromFile(imageView, filename+".png", getApplicationInfo().dataDir, R.drawable.hotel_fallback);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.setContentView(imageView);
    }
}
