package com.alfredthomas.hotelfinder;

//base class for hotels, parse JSON into this class and then retrieve info when displaying in HotelView
public class Hotel {
    String name;
    String imageURL;

    public Hotel(String name, String imageURL)
    {
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName()
    {
        return name;
    }
    public String getURL()
    {
        return imageURL;
    }


    //hotel object doesn't make sense without a name
    public boolean isEmpty()
    {
        if(name == null)
            return false;
        return name.isEmpty();
    }

    //useful for adding to set to compare objects together
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Hotel))
            return false;

        Hotel hotel = (Hotel)obj;

        return this.name.equals(hotel.name) && this.imageURL.equals(hotel.imageURL);
    }
}
