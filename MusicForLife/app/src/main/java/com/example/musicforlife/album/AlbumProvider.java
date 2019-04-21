package com.example.musicforlife.album;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class AlbumProvider {
    public static ArrayList<AlbumViewModel> getListAlbum(Context context){
        ArrayList<AlbumViewModel> arr = new ArrayList<AlbumViewModel>();
        //Init databasehelper
        DatabaseHelper databaseHelper = DatabaseHelper.newInstance(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //Init databasehelper

        String query = MessageFormat.format("select {0},{1},{2},COUNT({3}) from {4} group by {0}"
                , new String[]{SongModel.COLUMN_ALBUM,SongModel.COLUMN_ARTIST, SongModel.COLUMN_PATH, SongModel.COLUMN_ID, SongModel.TABLE_NAME});

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                arr.add(new AlbumViewModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)));
            } while (cursor.moveToNext());
        }
        db.close();
        return arr;
    }
}
