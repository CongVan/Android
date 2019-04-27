package com.example.musicforlife.artist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.SongModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ArtistProvider {
    public static ArrayList<ArtistViewModel> getArtistModel(Context context) {
        ArrayList<ArtistViewModel> result = new ArrayList<ArtistViewModel>();
        DatabaseManager databaseManager = DatabaseManager.newInstance(context);
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String query = MessageFormat.format("select {0},{1},COUNT({2}) from {3} group by {0}"
                , new Object[]{SongModel.COLUMN_ARTIST, SongModel.COLUMN_PATH, SongModel.COLUMN_ID, SongModel.TABLE_NAME});
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new ArtistViewModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static ArrayList<SongModel> getArtistSongs(Context context, String artist) {
        ArrayList<SongModel> arr = new ArrayList<SongModel>();
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        String query = MessageFormat.format("select {0},{1},{2},{3},{4},{5},{6},{7} from {8} where {9} = ?"
                , new Object[]{
                        SongModel.COLUMN_ID,
                        SongModel.COLUMN_SONG_ID,
                        SongModel.COLUMN_TITLE,
                        SongModel.COLUMN_ALBUM,
                        SongModel.COLUMN_DURATION,
                        SongModel.COLUMN_FOLDER,
                        SongModel.COLUMN_ARTIST,
                        SongModel.COLUMN_PATH,
                        SongModel.TABLE_NAME,
                        SongModel.COLUMN_ARTIST});
        String[] whereArgs = new String[]{
                artist,
        };
        Cursor cursor = db.rawQuery(query, whereArgs);
        if (cursor.moveToFirst()) {
            do {
                SongModel songModel = new SongModel();
                songModel.setId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_ID)));
                songModel.setSongId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_SONG_ID)));
                songModel.setTitle(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_TITLE)));
                songModel.setAlbum(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ALBUM)));
                songModel.setArtist(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ARTIST)));
                songModel.setFolder(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_FOLDER)));
                songModel.setDuration(cursor.getLong(cursor.getColumnIndex(SongModel.COLUMN_DURATION)));
                songModel.setPath(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_PATH)));
                arr.add(songModel);
            } while (cursor.moveToNext());
        }
        return arr;
    }
}
