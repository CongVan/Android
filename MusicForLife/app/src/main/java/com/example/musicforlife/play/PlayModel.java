package com.example.musicforlife.play;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;

public class PlayModel {
    public static final String TABLE_NAME = "plays";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG_ID = "song_id";
    public static final String COLUMN_IS_PLAYING = "is_playing";
    public static final String COLUMN_CURRENT_DURATION = "current_duration";


    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_SONG_ID).append(" INTEGER , ")
            .append(COLUMN_IS_PLAYING).append(" INTEGER, ")
            .append(COLUMN_CURRENT_DURATION).append(" INTEGER ")
            .append(" )")
            .toString();


    private Context mContext;
    private static DatabaseHelper mDatabaseHelper = DatabaseHelper.getInstance();

    private int id;
    private int song_id;
    private int is_playing;
    private int current_duration;

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
    public  static long addSongToPlayingList(SongModel song){
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlayModel.COLUMN_SONG_ID, song.getSongId());
        contentValues.put(PlayModel.COLUMN_SONG_ID,song.getSongId());
        contentValues.put(PlayModel.COLUMN_IS_PLAYING, 1);
        contentValues.put(PlayModel.COLUMN_CURRENT_DURATION, 0);

        long id = database.insert(PlayModel.TABLE_NAME, null, contentValues);
        database.close();
        return id;
    }
    public  static long isSongExsist(SongModel song){
        return 0;
    }
}
