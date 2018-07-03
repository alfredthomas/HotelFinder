package com.alfredthomas.hotelfinder.Util;

import android.content.Context;

import com.alfredthomas.hotelfinder.Hotel;
import com.alfredthomas.hotelfinder.Util.DB.HotelDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotelParser {

    //parse each JSON object
    public static Hotel parseHotel(JSONObject jsonObject)
    {
        String imageURL;
        String hotelName;

        //parse name and url with fallbacks
        //@todo generate better fallbacks
        imageURL=jsonObject.optString("image_url","");
        hotelName=jsonObject.optString("name","");

        //if we have an empty name, return null to prevent creating worthless hotel object
        if(hotelName.isEmpty())
            return null;
        return new Hotel(hotelName,imageURL);
    }
    public static List<Hotel> parseHotel(Context context,JSONArray jsonArray)
    {
        HotelDBHelper dbHelper = new HotelDBHelper(context);
        List<Hotel> hotelList = new ArrayList<>();

        //loop through each JSON object in the array, parse it to Hotel and save to SQLite DB
        for(int i = 0; i<jsonArray.length();i++)
        {
            try {
                Hotel hotel = parseHotel(jsonArray.getJSONObject(i));
                //skip over hotels parsed with empty names
                if(hotel!=null) {
                    hotelList.add(hotel);
                    //save off hotel in SQLite DB
                    dbHelper.insertHotel(hotel);
                }
            }
            catch(JSONException jse)
            {
                jse.printStackTrace();
            }
        }
        dbHelper.close();
        return hotelList;
    }
}
