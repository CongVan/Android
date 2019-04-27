package com.example.musicforlife.album;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.SongModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class AlbumProvider {
    public static ArrayList<AlbumViewModel> getListAlbum(Context context) {
        ArrayList<AlbumViewModel> arr = new ArrayList<AlbumViewModel>();
        //Init databasehelper
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        //Init databasehelper

        String query = MessageFormat.format("select {0},{1},{2},COUNT({3}) from {4} group by {0}"
                , new String[]{SongModel.COLUMN_ALBUM, SongModel.COLUMN_ARTIST, SongModel.COLUMN_PATH, SongModel.COLUMN_ID, SongModel.TABLE_NAME});

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
        return arr;
    }
    public static ArrayList<SongModel> getALbumSongs(Context context, String album){
        ArrayList<SongModel> arr = new ArrayList<SongModel>();
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        String query = MessageFormat.format("select {0},{1},{2},{3},{4},{5},{6},{7} from {8} where {9} = ?"
                , new String[]{
                        SongModel.COLUMN_ID,
                        SongModel.COLUMN_SONG_ID,
                        SongModel.COLUMN_TITLE,
                        SongModel.COLUMN_ALBUM,
                        SongModel.COLUMN_DURATION,
                        SongModel.COLUMN_FOLDER,
                        SongModel.COLUMN_ARTIST,
                        SongModel.COLUMN_PATH,
                        SongModel.TABLE_NAME,
                        SongModel.COLUMN_ALBUM});
        String[] whereArgs = new String[]{
                album,
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
