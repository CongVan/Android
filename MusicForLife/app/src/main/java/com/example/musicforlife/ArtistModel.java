package com.example.musicforlife;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseHelper;

import java.util.ArrayList;

public class ArtistModel {

    public ArtistModel(String name,int count)
    {
        Name = name;
        SongCount = count;
    }

    private String Name;

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    private int SongCount;

    public void setSongCount(int songCount) {
        SongCount = songCount;
    }

    public int getSongCount() {
        return SongCount;
    }

    public static ArrayList<ArtistModel> getArtistModel(Context context){
        ArrayList<ArtistModel> result = new ArrayList<ArtistModel>();
        DatabaseHelper databaseHelper = DatabaseHelper.newInstance(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "select artist,COUNT(id) from songs group by artist";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new ArtistModel(cursor.getString(0),cursor.getInt(1)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }
}
