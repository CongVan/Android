package com.example.musicforlife.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.musicforlife.PlayActivity;
import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;

import java.io.IOException;
import java.util.ArrayList;


public class PlayCenter implements PlayInterface {
    private static ArrayList<PlayModel> mPlayingList;
    private SongModel mCurrentSongPlaying;
    private static int mCurrentIndexSong;
    private static Context mContext;
    private static PlayActivity mPlayActivity;
    private static MediaPlayer mMediaPlayer = null;
    private static PlayCenter mPlayCenter = null;
    private static DatabaseHelper mDatabaseHelper = null;
    private static boolean mIsPlaying = false;


    public static final int ACTION_PLAY = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_RESUME = 3;
    public static final int ACTION_NEXT = 4;
    public static final int ACTION_PREV = 5;


    public static boolean Shuffle;
    private static final String TAG = "PlayCenter";
    public static final String SENDER = "PLAY_CENTER";

    public static PlayCenter newInstance(Context context, PlayActivity playActivity, DatabaseHelper databaseHelper) {
        if (mContext == null || mPlayCenter == null || mMediaPlayer == null || mDatabaseHelper == null) {
            mPlayCenter = new PlayCenter();
            mContext = context;
            mPlayActivity = playActivity;
            mMediaPlayer = new MediaPlayer();
            mDatabaseHelper = databaseHelper;
        }

        return mPlayCenter;
    }

    public void play(final SongModel songModel) {
//        Log.d(TAG, "play: "+songModel.getPath());
//        Log.d(TAG, "play: "+ Uri.parse(songModel.getPath()));
//        File path = Environment.getExternalStorageDirectory();
//        Log.d(TAG, "play: "+ path+songModel.getPath());
        try {
            mCurrentSongPlaying = songModel;
            mIsPlaying = true;
            setIndexSongInPlayingList();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(songModel.getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            new CountDownTimer(songModel.getDuration(), 1000) {

                                public void onTick(long millisUntilFinished) {
                                    updateSeekbar(SENDER, mMediaPlayer.getCurrentPosition());
                                }

                                public void onFinish() {
                                    mMediaPlayer.stop();
                                    mIsPlaying = false;
                                }
                            }.start();
                            mp.start();
                        }
                    }
            );
//            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void pause() {
        mMediaPlayer.pause();
        mIsPlaying = false;
    }

    public void resurme() {
        if (mCurrentSongPlaying != null && mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
            mMediaPlayer.start();
        }
        mIsPlaying = true;
    }

    public void next() {
        if (mIsPlaying) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
        }
        if (mCurrentIndexSong == mPlayingList.size()-1) {
            mCurrentIndexSong = 0;
        } else {
            mCurrentIndexSong++;
        }
        mCurrentSongPlaying = SongModel.getSongFromSongId(mDatabaseHelper, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(mCurrentSongPlaying);
        mPlayActivity.updateControlPlaying(SENDER, mCurrentSongPlaying);
    }

    public void prev() {
        if (mIsPlaying) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
        }
        if (mCurrentIndexSong == 0) {
            mCurrentIndexSong = mPlayingList.size() - 1;
        } else {
            mCurrentIndexSong--;
        }
        mCurrentSongPlaying = SongModel.getSongFromSongId(mDatabaseHelper, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(mCurrentSongPlaying);
        mPlayActivity.updateControlPlaying(SENDER, mCurrentSongPlaying);
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
        return mIsPlaying;
    }

    public void updateDuration(int progress) {
        mMediaPlayer.seekTo(progress * 1000);
    }

    @Override
    public void controlSong(String sender, SongModel songModel, int action) {

    }

    @Override
    public void updateControlPlaying(String sender, SongModel songModel) {

    }

    @Override
    public void updateDuration(String sender, int progress) {

    }

    @Override
    public void updateSeekbar(String sender, int duration) {
        mPlayActivity.updateSeekbar(sender, duration);
    }

    private void setIndexSongInPlayingList() {

        for (int i = 0; i < mPlayingList.size(); i++) {
            if (mPlayingList.get(i).getSongId() == mCurrentSongPlaying.getSongId()) {
                mCurrentIndexSong = i;
            }
        }
    }
}
