package com.example.simplerss.dburl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelperUrl extends SQLiteOpenHelper{
	private static final String TAG = DbHelperUrl.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rssreader";
    public static final String RESOURCE_TABLE = "resource";

    public static final String RESOURCE_TITLE = "resourceTitle";
    public static final String RESOURCE_URL = "resourceUrl";
    
    public static final String CREATE_TABLE = "CREATE TABLE " + DbHelperUrl.RESOURCE_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + RESOURCE_URL + " TEXT," + RESOURCE_TITLE + " TEXT)";
    
    public DbHelperUrl(Context context) {
        super(context, DbHelperUrl.DATABASE_NAME, null, DbHelperUrl.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbHelperUrl.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database, adding new changes from " + oldVersion + " to " + newVersion);
    }
	
}
