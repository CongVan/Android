package com.example.musicforlife.play;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PlayModel {
    public static final String TABLE_NAME = "plays";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG_ID = "song_id";
    public static final String COLUMN_IS_PLAYING = "is_playing";
    public static final String COLUMN_CURRENT_DURATION = "current_duration";
    public static final String COLUMN_CREATE_DATE = "create_date";


    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_SONG_ID).append(" INTEGER , ")
            .append(COLUMN_IS_PLAYING).append(" INTEGER, ")
            .append(COLUMN_CURRENT_DURATION).append(" INTEGER,")
            .append(COLUMN_CREATE_DATE).append(" DATETIME ")
            .append(" )")
            .toString();


    private Context mContext;
    private static DatabaseHelper mDatabaseHelper = DatabaseHelper.getInstance();

    private int id;
    private int song_id;
    private int is_playing;
    private int current_duration;
    private static final String TAG = "PlayModel";
//    public PlayModel newInstance(Context context){
//        if (mContext==null ){
//            mContext=context;
//        }
//        mDatabaseHelper=DatabaseHelper.getInstance();
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public int getIs_playing() {
        return is_playing;
    }

    public void setIs_playing(int is_playing) {
        this.is_playing = is_playing;
    }

    public int getCurrent_duration() {
        return current_duration;
    }

    public void setCurrent_duration(int current_duration) {
        this.current_duration = current_duration;
    }

    public static ArrayList<PlayModel> getListPlaying() {
        ArrayList<PlayModel> playingList = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String[] projection = {
                PlayModel.COLUMN_ID,
                PlayModel.COLUMN_SONG_ID,
                PlayModel.COLUMN_IS_PLAYING,
                PlayModel.COLUMN_CURRENT_DURATION,

        };
        Cursor cursor = db.query(PlayModel.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                PlayModel play = new PlayModel();
                play.setId(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_ID)));
                play.setSong_id(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_SONG_ID)));
                play.setIs_playing(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_IS_PLAYING)));
                play.setCurrent_duration(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_CURRENT_DURATION)));

                playingList.add(play);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return playingList;
    }

    public static ArrayList<SongModel> getSongPlayingList() {
        ArrayList<SongModel> songModelList = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT s.* FROM " + PlayModel.TABLE_NAME + " pm INNER JOIN " + SongModel.TABLE_NAME + " s ON pm." + PlayModel.COLUMN_SONG_ID
                + "= s." + SongModel.COLUMN_SONG_ID + " ORDER BY pm." + PlayModel.COLUMN_CREATE_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);
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
                songModelList.add(songModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songModelList;
    }

    public static long addSongToPlayingList(SongModel song) {
        Log.d(TAG, "addSongToPlayingList: SONG_ID" + song.getSongId());
        boolean existSong = isSongExsist(song);
        Log.d(TAG, "addSongToPlayingList: EXIST" + existSong);
        if (!existSong) {
            SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayModel.COLUMN_ID, song.getId());
            contentValues.put(PlayModel.COLUMN_SONG_ID, song.getSongId());
            contentValues.put(PlayModel.COLUMN_IS_PLAYING, 1);
            contentValues.put(PlayModel.COLUMN_CURRENT_DURATION, 0);
            contentValues.put(PlayModel.COLUMN_CREATE_DATE, getDateTimeNow());
            long id = database.insert(PlayModel.TABLE_NAME, null, contentValues);
            database.close();
            return id;
        }
        return -1;
    }

    private static String getDateTimeNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isSongExsist(SongModel song) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        boolean result = false;
        String query = MessageFormat.format("SELECT {0} FROM {1} WHERE {2}={3}",
                new String[]{PlayModel.COLUMN_ID, PlayModel.TABLE_NAME, PlayModel.COLUMN_SONG_ID, String.valueOf(song.getSongId())});
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public static boolean clearPlayingList() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(PlayModel.TABLE_NAME, null, null) > 0;

    }
}
