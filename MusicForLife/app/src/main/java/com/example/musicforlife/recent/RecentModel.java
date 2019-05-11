package com.example.musicforlife.recent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.musicforlife.artist.ArtistModel;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.PlaylistModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecentModel {
    public static final String TABLE_NAME = "recent";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LINK_KEY = "link_key";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_LINK_KEY).append(" TEXT , ")
            .append(COLUMN_TYPE).append(" INTEGER, ")
            .append(COLUMN_DATE).append(" DATETIME ")
            .append(" )")
            .toString();


    public static final int TYPE_SONG = 1;
    public static final int TYPE_ALBUM = 2;
    public static final int TYPE_ARTIST = 3;
    public static final int TYPE_PLAYLIST = 4;
    public static final int LIMIT_RECENT = 5;
    private int id;
    private int linkId;
    private int type;
    private Date date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private static final String TAG = "RecentModel";

    public static long addToRecent(int linkId, int type) {
        try {
            if (isExistsRecent(linkId) == -1) {
                SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_LINK_KEY, linkId);
                contentValues.put(COLUMN_TYPE, type);
                contentValues.put(COLUMN_DATE, getDateTimeNow());
                return database.insert(TABLE_NAME, null, contentValues);
            } else {
                return updateRecentToNow(linkId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static int isExistsRecent(int linkId) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            return -1;
        }
    }

    public static long updateRecentToNow(int id) {
        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, getDateTimeNow());
        return database.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    public static ArrayList<SongModel> getRecentSong() {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();


        String query = "SELECT L.* FROM " + TABLE_NAME + " R JOIN " + SongModel.TABLE_NAME + " L ON R." + COLUMN_LINK_KEY + "=L." + SongModel.COLUMN_SONG_ID +
                " WHERE R." + COLUMN_TYPE + "=" + TYPE_SONG +
                " ORDER BY R." + COLUMN_DATE + " desc " +
                " LIMIT 1,3";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<SongModel> result = new ArrayList<>();
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
                result.add(songModel);
            } while (cursor.moveToNext());

        }
        Log.d(TAG, "getRecentSong: "+result.size());
        return result;
    }

    public static ArrayList<ArtistModel> getRecentArtist() {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();


        String query = "SELECT " + SongModel.COLUMN_ARTIST + "," + SongModel.COLUMN_PATH + "," + SongModel.COLUMN_ALBUM_ID + ",COUNT(" + SongModel.COLUMN_SONG_ID + ") SongCount FROM " + TABLE_NAME + " R JOIN " + SongModel.TABLE_NAME + " L ON R." + COLUMN_LINK_KEY + "=L." + SongModel.COLUMN_ARTIST +
                " WHERE R." + COLUMN_TYPE + "=" + TYPE_ARTIST +
                " GROUP BY L." + SongModel.COLUMN_ARTIST +
                " ORDER BY R." + COLUMN_DATE + " desc ";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<ArtistModel> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_PATH));
                int albumId = cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_ALBUM_ID));
                int songCount = cursor.getInt(cursor.getColumnIndex("SongCount"));

                result.add(new ArtistModel(name, path, albumId, songCount));
            } while (cursor.moveToNext());

        }
        return result;
    }

    private static String getDateTimeNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
