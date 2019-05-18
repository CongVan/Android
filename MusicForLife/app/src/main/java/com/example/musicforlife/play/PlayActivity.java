package com.example.musicforlife.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.TimerSongService;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements PlayInterface {

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private DatabaseManager mDatabaseManager;
    private CoordinatorLayout mLayoutPlay;
    private PagerAdapter mPagerAdapter;
    private PlayService mPlayService;
    private ImageView imageViewBackgroundMain;
    private MainActivity mMainActivity;
    private ArrayList<SongModel> mPlayList;

    private static final String TAG = "PlayActivity";
    public static final String EXTRA_PLAYING_LIST = "EXTRA_PLAYING_LIST";
    public static final String SENDER = "PLAY_ACTIVITY";

    public static final int TYPE_SHOW_NEW = 1;
    public static final int TYPE_SHOW_RESUME = 2;

    private SongModel mSongPlaying = null;
    private static PlayActivity mPlayActivity;
    private Toolbar mToolbar;
    private Menu mMenuPlay;
    private TimerReceiver mTimerReceiver;

    public static PlayActivity getActivity() {
        return mPlayActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mToolbar = findViewById(R.id.toolbarListPlaying);
        mLayoutPlay = findViewById(R.id.layoutPlayActivity);
        mPager = (ViewPager) findViewById(R.id.pager);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Đang phát");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Utility.setTransparentStatusBar(PlayActivity.this);
//        getSupportActionBar().setElevation(0);
        mLayoutPlay.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something here, such as start an Intent to the parent activity.
                onBackPressed();
            }
        });
//        mLayoutPlay.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);

//        mLayoutPlay.setBackground(ImageHelper.getMainBackgroundDrawable());


        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
        mPlayActivity = this;
        mMainActivity = MainActivity.getMainActivity();
        mPlayService = PlayService.newInstance();

        mSongPlaying = PlayService.getCurrentSongPlaying();


        mPagerAdapter = new FragmentPlayAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);
        mTimerReceiver = new TimerReceiver();
        IntentFilter intentFilter = new IntentFilter(TimerSongService.ACTION_FINISH_TIMER);
        registerReceiver(mTimerReceiver, intentFilter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                if (i==0){//Page list playing
//                    ((FragmentPlayAdapter) mPagerAdapter).getFragmentListPlaying().updateListPlaying();
//                }
                updateToolbarTitle();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: RESTORE " + savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", 1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void controlSong(String sender, SongModel songModel, int action) {

        switch (action) {
            case PlayService.ACTION_PLAY:
//                if (sender.equals(FragmentListPlaying.SENDER)) {
//
//                }
                Log.d(TAG, "controlSong: PLAY " + sender + " " + songModel.getTitle());
                mPager.setCurrentItem(1);
                mPlayService.play(songModel);
                mMainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                Log.d(TAG, "controlSong: ");

                break;
            case PlayService.ACTION_PAUSE:
                mPlayService.pause();
                mMainActivity.refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                break;
            case PlayService.ACTION_RESUME:
                mPlayService.resurme();
                mMainActivity.refreshNotificationPlaying(PlayService.ACTION_RESUME);
                break;
            case PlayService.ACTION_PREV:
                mPlayService.prev(PlayService.ACTION_FROM_USER);
                mMainActivity.refreshNotificationPlaying(PlayService.ACTION_PREV);
                refreshListPlaying();
                break;
            case PlayService.ACTION_NEXT:
                mPlayService.next(PlayService.ACTION_FROM_USER);
                mMainActivity.refreshNotificationPlaying(PlayService.ACTION_NEXT);
                refreshListPlaying();
                break;
            default:
                break;
        }
        MainActivity.getMainActivity().togglePlayingMinimize("PlayActivity");
    }

    public void refreshListPlaying() {
        FragmentListPlaying fragmentListPlaying = ((FragmentPlayAdapter) mPagerAdapter).getFragmentListPlaying();
        if (fragmentListPlaying != null) {
            fragmentListPlaying.refreshListPlaying();
        }
    }


    @Override
    public void updateControlPlaying(String sender, SongModel songModel) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) mPagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateControlPlaying(songModel);
        }
//        ((FragmentPlayAdapter) mPagerAdapter).getFragmentPlaying().updateControlPlaying(songModel);
        MainActivity.getMainActivity().togglePlayingMinimize(sender);
    }

    @Override
    public void updateDuration(String sender, int progress) {
        mPlayService.updateDuration(progress);
    }

    @Override
    public void updateSeekbar(String sender, int duration) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) mPagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateSeekbar(duration);
        }
    }

    @Override
    public void updateButtonPlay(String sender) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) mPagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateButtonPlay();
        }
//        ((FragmentPlayAdapter) mPagerAdapter).getFragmentPlaying().updateButtonPlay();
    }

    @Override
    public void updateSongPlayingList() {
        FragmentListPlaying fragmentListPlaying = ((FragmentPlayAdapter) mPagerAdapter).getFragmentListPlaying();
        if (fragmentListPlaying != null) {
            fragmentListPlaying.updateListPlaying();
        }
    }

    @Override
    public void updateToolbarTitle() {
        int index = mPager.getCurrentItem();
        if (index == 0) {
            getSupportActionBar().setTitle("Danh sách phát");
            if (mMenuPlay != null) {
                mMenuPlay.findItem(R.id.actionSetTimerSong).setVisible(false);
            }
        } else if (index == 1) {
            getSupportActionBar().setTitle("Đang phát");
            if (mMenuPlay != null) {
                mMenuPlay.findItem(R.id.actionSetTimerSong).setVisible(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer_song, menu);
        mMenuPlay = menu;
//        mMenuPlay.findItem(R.id.actionSetTimerSong).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSetTimerSong: {
                DialogFragment dialogCreatePlaylist = new FragmentDialogTimerSong(PlayActivity.this);
                dialogCreatePlaylist.show(getSupportFragmentManager(), "SetTimerSong");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTimerService(Intent intentStart) {
        startService(intentStart);

    }

    public void stopTimerService(Intent intentStart) {
        stopService(intentStart);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //unregisterReceiver(mTimerReceiver);
    }

    public class TimerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Timer Song " + intent.getAction());
            if (intent.getAction() == TimerSongService.ACTION_FINISH_TIMER) {
                if (PlayService.isPlaying()) {
                    mPlayService.pause();
                    mMainActivity.refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                    updateButtonPlay(SENDER);
                }

            }
        }
    }
}
