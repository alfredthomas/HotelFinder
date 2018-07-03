package com.alfredthomas.hotelfinder.Util.Async;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alfredthomas.hotelfinder.Hotel;
import com.alfredthomas.hotelfinder.Util.Adapter.HotelRecyclerAdapter;
import com.alfredthomas.hotelfinder.Util.HotelParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class GetHotelsTask extends AsyncTask <Void, Void, List<Hotel>> {
    final String hotelURLString = "hipmunk.com/mobile/coding_challenge";

    //maintain RecyclerView reference to update adapter
    WeakReference<RecyclerView> recyclerViewWeakReference = null;
    //maintain SwipeRefreshLayout referenece to set refreshing status
    WeakReference<SwipeRefreshLayout> swipeRefreshLayoutWeakReference = null;


    public GetHotelsTask(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        this.recyclerViewWeakReference = new WeakReference<>(recyclerView);
        swipeRefreshLayoutWeakReference = new WeakReference<>(swipeRefreshLayout);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(swipeRefreshLayoutWeakReference!=null)
            //if we are executing this task, we should set the refreshing status on the swipe refresh
            swipeRefreshLayoutWeakReference.get().setRefreshing(true);
    }

    @Override
    protected List<Hotel> doInBackground(Void... voids) {
        List<Hotel> hotels = new ArrayList<>();
        try {

            //build the hardcoded URI based on the given string
            Uri.Builder uribuilder = new Uri.Builder();
            uribuilder.scheme("https");
            uribuilder.encodedAuthority(hotelURLString);

            URL url = new URL(uribuilder.toString());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* 10 seconds */ );
            urlConnection.setConnectTimeout(15000 /* 15 seconds */ );
            urlConnection.setDoOutput(false); //getting only
            urlConnection.connect();


            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder response = new StringBuilder();
            String nextLine;
            //read in all the response
            while((nextLine = bufferedReader.readLine())!=null)
            {
                response.append(nextLine);
            }

            bufferedReader.close();
            inputStreamReader.close();

            //parse the response to JSON
            JSONObject responseJson = new JSONObject(response.toString());
            //get the "hotel" array to parse
            JSONArray hotelsObject = responseJson.optJSONArray("hotels");
            return HotelParser.parseHotel(recyclerViewWeakReference.get().getContext(),hotelsObject);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hotels;
    }

    @Override
    protected void onPostExecute(List<Hotel> hotels) {
        //as long as we have a recyclerview, we will update its adapter
        if(recyclerViewWeakReference !=null) {
            //no adapter, so create one
            if(recyclerViewWeakReference.get().getAdapter()==null)
                recyclerViewWeakReference.get().setAdapter(new HotelRecyclerAdapter(hotels));
            //update the existing adapter
            else
                ((HotelRecyclerAdapter)recyclerViewWeakReference.get().getAdapter()).addItems(hotels);
        }
        if(swipeRefreshLayoutWeakReference!=null)
        {
            //turn off the refreshing status
            swipeRefreshLayoutWeakReference.get().setRefreshing(false);
        }
    }

}
