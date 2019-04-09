package com.example.musicforlife.play;

import android.content.Context;
import android.util.Log;

import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;


public class PlayCenter {
    private static  ArrayList<PlayModel> mPlayModelsList;
    private SongModel currentSong;
    private static Context mContext;
    private static PlayCenter mPlayCenter = null;

    public static TypeLoop LOOP_TYPE;
    public static boolean Shuffle;
    private static final String TAG = "PlayCenter";

    public static PlayCenter newInstance(Context context) {
        if (mContext == null || mPlayCenter == null) {
            mPlayCenter = new PlayCenter();
            mContext=context;
        }
        return mPlayCenter;
    }

    public void play() {

    }

    public void pause() {

    }

    public void next() {

    }

    public void prev() {

    }

    public static int addSongsToPlayingList(ArrayList<SongModel> songs) {
        for (SongModel song : songs) {
            long result = PlayModel.addSongToPlayingList(song);
        }
        updatePlayingList();
        return 1;
    }

    public static int createPlayingList(ArrayList<SongModel> songs) {
        PlayModel.clearPlayingList();
        for (SongModel song : songs) {
            long result = PlayModel.addSongToPlayingList(song);
        }
        updatePlayingList();
        return 1;
    }

    private static int updatePlayingList() {
        mPlayModelsList = PlayModel.getListPlaying();
        Log.d(TAG, "updatePlayingList: SIZE PLAYING LIST"+mPlayModelsList.size());
        return mPlayModelsList.size();
    }

    public ArrayList<PlayModel> getPlayModelsList() {
        return mPlayModelsList;
    }

}
