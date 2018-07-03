package com.alfredthomas.hotelfinder.Util.DB;

import android.provider.BaseColumns;

public final class HotelContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HotelContract() {}

    /* Inner class that defines the table contents */
    public static class HotelEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_HOTELNAME = "name";
        public static final String COLUMN_NAME_HOTELURL = "url";
    }
}
