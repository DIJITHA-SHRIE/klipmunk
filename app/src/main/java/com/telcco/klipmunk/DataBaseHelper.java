package com.telcco.klipmunk;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by PHD on 12/23/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Klipmunk";
    public static final String TABLE_NAME = "screenshots";
    public static final String COL_ID = "id";
    public static final String COL_PATH = "path";
    public static final String COL_TAG = "tag";
    public static final String COL_NOTES= "notes";
    public static final String TAG_TABLE_NAME = "tag";
    public static final String TAG_COL_ID = "tag_id";
    public static final String TAG_COL_NAME = "tag_name";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_PATH + " TEXT,"
                    + COL_NOTES + " TEXT,"
                    + COL_TAG  + " TEXT"+")";
    public static final String TAG_CREATE_TABLE =
            "CREATE TABLE " + TAG_TABLE_NAME + "("
                    + TAG_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TAG_COL_NAME + " TEXT"+")";



    public DataBaseHelper(Activity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TableCreated","TableCreated");
        db.execSQL(CREATE_TABLE);
        db.execSQL(TAG_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TAG_TABLE_NAME);
        onCreate(db);

    }

    public boolean insertPath(String path,String tag,String notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  cv = new ContentValues();
        cv.put(COL_PATH,path);
        cv.put(COL_TAG,tag);
        cv.put(COL_NOTES,notes);
        db.insert(TABLE_NAME,null,cv);
        return true;

    }

    public ArrayList<ScreensModel> getScreenShotsPath(String tag){
        ArrayList<ScreensModel> array_list = new ArrayList<ScreensModel>();


        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME , null );
        Cursor res =  db.rawQuery( "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_TAG +" ='"+tag+"'" ,null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new ScreensModel(res.getString(res.getColumnIndex(COL_PATH)),res.getString(res.getColumnIndex(COL_NOTES))));
            res.moveToNext();
        }
        return array_list;
    }
    public boolean insertTag(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  cv = new ContentValues();
        cv.put(TAG_COL_NAME,tag);
        db.insert(TAG_TABLE_NAME,null,cv);
        return true;

    }

    public ArrayList<String> getTag(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TAG_TABLE_NAME , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TAG_COL_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

}
