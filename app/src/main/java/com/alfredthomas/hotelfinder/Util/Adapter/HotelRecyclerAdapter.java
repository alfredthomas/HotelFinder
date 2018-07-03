package com.alfredthomas.hotelfinder.Util.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;

import com.alfredthomas.hotelfinder.Hotel;
import com.alfredthomas.hotelfinder.UI.HotelView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class HotelRecyclerAdapter extends RecyclerView.Adapter {
    LinkedHashSet<Hotel> hotelSet;
    List<Hotel> hotels;

    public HotelRecyclerAdapter(List<Hotel> hotels) {
        //create set to remove duplicates
        this.hotelSet = new LinkedHashSet<>(hotels);
        this.hotels = new ArrayList<>(hotelSet);
    }

    public HotelRecyclerAdapter() {
        this.hotels = new ArrayList<>();
        this.hotelSet = new LinkedHashSet<>(hotels);
    }
        @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(new HotelView(parent.getContext()));
        viewHolder.hotelView.setLayoutParams(getDefaultCellSize(parent));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecyclerViewHolder)holder).hotelView.setData(hotels.get(position));
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }


    //do some magic and figure out cell height based on orientation
    private static ViewGroup.LayoutParams getDefaultCellSize(ViewGroup parent)
    {
        int rowHeight;
        boolean isTablet = isTablet(parent.getContext());
        if(parent.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //8 cells visible at a time for portrait phone, 15 for tablet portrait
            rowHeight = parent.getHeight() / (isTablet? 15:8);
        }
        else
        {
            // 5 cells for landscape or undefined phone, 10 cells for tablet landscape
            rowHeight = parent.getHeight() / (isTablet?10:5);
        }
        return new ViewGroup.LayoutParams(parent.getMeasuredWidth(), rowHeight);
    }

    //from https://forums.xamarin.com/discussion/106774/how-to-properly-detect-if-an-android-device-is-a-phone-or-a-tablet
    private static boolean isTablet(Context context)
    {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        //get height and width inches
        double wInches = displayMetrics.widthPixels / (double)displayMetrics.densityDpi;
        double hInches = displayMetrics.heightPixels / (double)displayMetrics.densityDpi;

        //a^2 + b^2 = c^2
        double screenDiagonal = Math.sqrt(Math.pow(wInches, 2) + Math.pow(hInches, 2));
        //using 7 inches or greater as a tablet
        return (screenDiagonal >= 7.0);

    }

    //merge new list with old list
    public void addItems(List<Hotel> newList)
    {
        int oldSize = hotelSet.size();
        //insert at end
        hotelSet.addAll(newList);
        //convert to set and back to remove duplicates
        hotels = new ArrayList<>(hotelSet);

        //notify adapter it changed
        this.notifyItemRangeInserted(oldSize,hotelSet.size()-oldSize);
};
}
