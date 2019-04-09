package com.example.musicforlife.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import com.example.musicforlife.listsong.SongModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PlayCenter {
    private static ArrayList<PlayModel> mPlayingList;
    private SongModel mCurrentSongPlaying;
    private static Context mContext;
    private static MediaPlayer mMediaPlayer = null;
    private static PlayCenter mPlayCenter = null;

    public static final int ACTION_PLAY = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_RESUME = 3;
    public static final int ACTION_NEXT = 4;
    public static final int ACTION_PREV = 5;

    private static boolean isPlaying = false;


    public static boolean Shuffle;
    private static final String TAG = "PlayCenter";

    public static PlayCenter newInstance(Context context) {
        if (mContext == null || mPlayCenter == null || mMediaPlayer == null) {
            mPlayCenter = new PlayCenter();
            mContext = context;
            mMediaPlayer = new MediaPlayer();
        }
        return mPlayCenter;
    }

    public void play(final SongModel songModel) {


//        Log.d(TAG, "play: "+songModel.getPath());
//        Log.d(TAG, "play: "+ Uri.parse(songModel.getPath()));
//        File path = Environment.getExternalStorageDirectory();
//        Log.d(TAG, "play: "+ path+songModel.getPath());
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(songModel.getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mCurrentSongPlaying = songModel;
                            isPlaying = true;
                        }
                    }
            );
//            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mMediaPlayer.stop();
                isPlaying = false;
            }
        }.start();
    }

    public void pause() {
        mMediaPlayer.pause();
        isPlaying = false;
    }
    public  void resurme(){
        if (mCurrentSongPlaying!=null && mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
            mMediaPlayer.start();
        }
        isPlaying = true;
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
        mPlayingList = PlayModel.getListPlaying();
        Log.d(TAG, "updatePlayingList: SIZE PLAYING LIST" + mPlayingList.size());
        return mPlayingList.size();
    }

    public ArrayList<PlayModel> getPlayModelsList() {
        return mPlayingList;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }


}
