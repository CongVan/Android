package com.example.musicforlife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mDatabaseInstance = null;
    private Context mContext;

    public static  DatabaseHelper getInstance(){
        return  mDatabaseInstance;
    }

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    public static final String DATABASE_NAME = "music_of_life_db.db";

    public static DatabaseHelper newInstance(Context context) {
        if (mDatabaseInstance == null) {
            mDatabaseInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mDatabaseInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SongModel.SCRIPT_CREATE_TABLE);
        db.execSQL(PlayModel.SCRIPT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlayModel.TABLE_NAME);
        onCreate(db);
    }
}
