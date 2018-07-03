package com.alfredthomas.hotelfinder;

import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.TextView;


import com.alfredthomas.hotelfinder.Util.DB.HotelDBHelper;
import com.alfredthomas.hotelfinder.Util.Async.GetHotelsTask;
import com.alfredthomas.hotelfinder.Util.Adapter.HotelRecyclerAdapter;

/*
    MainActivity runs as follows:
        Create both a SwipeRefresh Layout and RecyclerView
        Try to load data from SQLite DB of cached hotels
            If no data, do async request (GetHotelsTask) for hotels from specified server
        SwipeRefresh allows for pulldown refresh to do another async call for more hotels
        GetHotelsTask will add results to SQLite after parsing to ensure we cache each request
        Save scroll sate of RecyclerView in case of screen rotation
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    final String recyclerState = "RECYCLER_STATE";

    HotelDBHelper hotelDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        hotelDBHelper = new HotelDBHelper(this);

        //create swipe refresh layout to load new data
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //pulling down the swiperefresh will kickoff an update task
                GetHotelsTask hotelsTask = new GetHotelsTask(recyclerView,swipeRefreshLayout);
                hotelsTask.execute();
            }
        });

        //create recyclerview to put data into
        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //@todo create and register an observer to show emptyTextView if the recycler is empty

        //load recyclerview with data from SQLite DB if possible
        HotelRecyclerAdapter hotelRecyclerAdapter = new HotelRecyclerAdapter(hotelDBHelper.getHotelList());
        recyclerView.setAdapter(hotelRecyclerAdapter);

        GetHotelsTask hotelsTask = new GetHotelsTask(recyclerView,swipeRefreshLayout);
        //check to see if we have data cached first, if not then kickoff an update task
        if(recyclerView.getAdapter().getItemCount()<=0)
            hotelsTask.execute();

        swipeRefreshLayout.addView(recyclerView);

        setContentView(swipeRefreshLayout);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //close reference to our DB
        hotelDBHelper.close();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
            //restore scroll position
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(recyclerState);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saving scroll position
        outState.putParcelable(recyclerState, recyclerView.getLayoutManager().onSaveInstanceState());

    }
}
