package com.example.trialtest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.trialtest.provider.MyContractClass;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private final String TAG = "TrialTest";

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "websiteDB.db";
    public static final String TABLE_WEBSITE = "websiteName";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_URL = "url";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory , DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    /** create database table with particular column */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEBSITE_TABLE = "CREATE TABLE " +
                TABLE_WEBSITE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"  + COLUMN_URL + " STRING" + ")";
        db.execSQL(CREATE_WEBSITE_TABLE);
        Log.d(TAG, "database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBSITE);
        onCreate(db);
    }

    /** add values to the database table column respectively */
    public void addWebsite(Website website){
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, website.getUrl());
        myCR.insert(MyContractClass.CONTENT_URI, values);
        Log.d(TAG,"website url added");
    }

    /** find values from database table according to id for listview */
    public Website findUrl(int id) {

        String[] projection = {COLUMN_ID, COLUMN_URL};
        String selection = null;
        Cursor cursor = myCR.query(MyContractClass.CONTENT_URI,
                projection, selection, null,
                null);

        Website website = new Website();
        if(cursor.moveToFirst()) {
            do{
                if(cursor.getInt(0) == id) {
                    website.setUrl(cursor.getString(1));
                    break;
                }
                Log.d(TAG, String.valueOf(id));
            } while(cursor.moveToNext());
        }

        return website;
    }

    /** retrives all values from database table and insert in a list */
    public List<Website> findAllUrl() {

        String[] projection = {COLUMN_ID, COLUMN_URL};
        String selection = null;
        Cursor cursor = myCR.query(MyContractClass.CONTENT_URI,
                projection, selection, null,
                null);

        List<Website> website = new ArrayList<>();
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

            do{
                Website website1 = new Website();
                website1.setID(cursor.getInt(0));
                website1.setUrl(cursor.getString(1));

                cursor.moveToNext();
                website.add(website1);

            } while(!cursor.isAfterLast());
            cursor.close();
        }

        return website;
    }

    /** delete the record in the database according to the id */
    public boolean deleteUrl(int id){
        boolean result = false;
        int rowsDeleted = myCR.delete(Uri.parse(MyContractClass.CONTENT_URI_STRING + "/" + id),null,null);
        if (rowsDeleted > 0){
            result = true;
        }
        return result;
    }
}
