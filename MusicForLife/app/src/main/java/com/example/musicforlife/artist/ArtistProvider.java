package com.example.musicforlife.artist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ArtistProvider {
    public static ArrayList<ArtistModel> getArtistModel(Context context){
        ArrayList<ArtistModel> result = new ArrayList<ArtistModel>();
        DatabaseHelper databaseHelper = DatabaseHelper.newInstance(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = MessageFormat.format("select {0},{1},COUNT({2}) from {3} group by {0}"
                ,new String[] {SongModel.COLUMN_ARTIST,SongModel.COLUMN_PATH,SongModel.COLUMN_ID,SongModel.TABLE_NAME});
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new ArtistModel(cursor.getString(0),cursor.getString(1),cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }
}