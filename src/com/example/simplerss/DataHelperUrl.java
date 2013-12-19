package com.example.simplerss;

import org.mcsoxford.rss.RSSItem;

import com.example.simplerss.dburl.DbHelperUrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelperUrl {

	private SQLiteDatabase db;
    private static final String TAG = DataHelperUrl.class.getName();
    
    public DataHelperUrl(Context context) {
        DbHelper openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
        //db.execSQL("PRAGMA foreign_keys = ON;");
    }
    
    public void cleanOldResource() {
        db.delete(DbHelperUrl.RESOURCE_TABLE, null, null);
    }

    public long insertResource(RssResource resource) {
        Log.i(TAG, "insertResource");
        ContentValues values = getResourceValues(resource);
        return db.insert(DbHelper.RESOURCE_TABLE, null, values);
    }
    
    private ContentValues getResourceValues(RssResource resource) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.RESOURCE_TITLE, resource.getTitle());
        values.put(DbHelper.RESOURCE_URL, resource.getUrl());

        return values;
    }
	
    public Cursor getCursor(String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }
}
