package com.alfredthomas.hotelfinder.Util.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alfredthomas.hotelfinder.Hotel;

import java.util.ArrayList;
import java.util.List;


//example copied and modified from https://developer.android.com/training/data-storage/sqlite

public class HotelDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Hotels.db";

    public HotelDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //used in sql calls since we are only storing hotels in it
    private String selectAllQuery = "select * from " + HotelContract.HotelEntry.TABLE_NAME;

    //string for creating db
    // NOTE that columns hotelname and hotelurl are a unique pair to prevent duplication

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HotelContract.HotelEntry.TABLE_NAME + " (" +
                    HotelContract.HotelEntry._ID + " INTEGER PRIMARY KEY," +
                    HotelContract.HotelEntry.COLUMN_NAME_HOTELNAME + " TEXT not null," +
                    HotelContract.HotelEntry.COLUMN_NAME_HOTELURL + " TEXT not null,"+
                    "unique ("+HotelContract.HotelEntry.COLUMN_NAME_HOTELNAME+","+HotelContract.HotelEntry.COLUMN_NAME_HOTELURL +"))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HotelContract.HotelEntry.TABLE_NAME;

    public HotelDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //used to cache hotels as we parse them
    public void insertHotel(Hotel hotel)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HotelContract.HotelEntry.COLUMN_NAME_HOTELNAME, hotel.getName());
        contentValues.put(HotelContract.HotelEntry.COLUMN_NAME_HOTELURL, hotel.getURL());

        //add to db, but ignore if the row already exists somewhere
        database.insertWithOnConflict(HotelContract.HotelEntry.TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
    }

    //loading hotels from DB
    public List<Hotel> getHotelList()
    {
        //simple get all rows, aka select * from <table>
        Cursor cursor = getReadableDatabase().rawQuery(selectAllQuery,null);
        List<Hotel> hotelList = new ArrayList<>();

        //move to first position and check for data
        if(cursor.moveToFirst())
        {
            //loop until end of result
            while(!cursor.isAfterLast())
            {
                Hotel hotel = cursorToHotel(cursor);
                if(hotel!=null && !hotel.isEmpty())
                    hotelList.add(hotel);
                cursor.moveToNext();
            }
        }
        return hotelList;
    }

    //convert sql row to hotel
    private Hotel cursorToHotel(Cursor cursor)
    {
        if((cursor == null) || (cursor.getCount() <= 0))
        {
            return null;
        }

        //get hotel name and url from db columns
        return new Hotel(cursor.getString(cursor.getColumnIndex(HotelContract.HotelEntry.COLUMN_NAME_HOTELNAME))
                ,cursor.getString(cursor.getColumnIndex(HotelContract.HotelEntry.COLUMN_NAME_HOTELURL)));
    }

    //unused future call for getting specific hotel
    public Hotel getHotel(int id)
    {
        Cursor cursor = getReadableDatabase().rawQuery(selectAllQuery+ " where "+ HotelContract.HotelEntry._ID+"="+id,null);
        return cursorToHotel(cursor);
    }
}
