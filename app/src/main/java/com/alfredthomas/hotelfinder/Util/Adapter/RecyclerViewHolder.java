package com.alfredthomas.hotelfinder.Util.Adapter;

import android.support.v7.widget.RecyclerView;

import com.alfredthomas.hotelfinder.UI.HotelView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public HotelView hotelView;
    public RecyclerViewHolder(HotelView v) {
        super(v);
        hotelView = v;
    }
}
