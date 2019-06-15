package com.example.musicforlife;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.album.FragmentAlbum;
import com.example.musicforlife.artist.ArtistModel;
import com.example.musicforlife.artist.ArtistProvider;
import com.example.musicforlife.artist.FragmentArtist;
import com.example.musicforlife.callbacks.MainCallbacks;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.folder.FragmentFolder;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.minimizeSong.MinimizeSongFragment;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements MainCallbacks, MinimizeSongFragment.OnFragmentInteractionListener {//AudioManager.OnAudioFocusChangeListener

    private CoordinatorLayout mLayoutMainContent;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private Toolbar mToolBar;

    private static MainActivity mMainActivity;
    private Intent mIntentPlayActivity;
    private PlayService mPlayService;
    public static DatabaseManager mDatabaseManager;

    private String mSearchValue = "";
    private int mCurrentFragmentActive;
    private MinimizeSongFragment mMinimizeSongFragment;

    public static Boolean isHideMinimize = false;
    public static final Integer PLAY_CHANEL_ID = 103;
    public static final Integer PLAY_NOTIFICATION_ID = 103;
    private static RemoteViews mNotificationlayoutPlaying;
    private TimerReceiver mTimerReceiver;
    private TimerSongService mTimerSongService;
    private final int mIconsTabDefault[] = {
            R.mipmap.tab_recent_default,
            R.mipmap.tab_song_default,
            R.mipmap.tab_playlist_default,
            R.mipmap.tab_artist_default,
            R.mipmap.tab_album_default,
            R.mipmap.tab_folder_default
    };
    private final int mIconsTabActive[] = {
            R.mipmap.tab_recent_active,
            R.mipmap.tab_song_active,
            R.mipmap.tab_playlist_active,
            R.mipmap.tab_artist_active,
            R.mipmap.tab_album_active,
            R.mipmap.tab_folder_active
    };
    private final String mTabMainTitle[] = {"Gần đây", "Bài hát", "Playlist", "Ca sĩ", "Album", "Thư mục"};


    public static MainActivity getMainActivity() {
        return mMainActivity;
    }


    private static final String TAG = "MainActivity";

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.activity_main);
        initFindView();

        mMainActivity = MainActivity.this;

//        togglePlayingMinimize("MAIN");
        mPagerAdapter = new PagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mTabLayout.setupWithViewPager(mViewPager);

        mPlayService = PlayService.newInstance();

        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());

        setSupportActionBar(mToolBar);
        setupLayoutTransparent();
        initMinimizePlaying();

        initTabLayoutIcon();


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mCurrentFragmentActive = i;
                SearchByFragment(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        try {
            registerService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar, menu);


        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_main).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchValue = s;
                SearchByFragment(mCurrentFragmentActive);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSearchValue = s;
                SearchByFragment(mCurrentFragmentActive);
                return false;
            }
        });
        {

        }

        return true;
    }

    /**
     * làm status bar trong suốt và set padding cho layout main
     */
    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(MainActivity.this);
        mLayoutMainContent.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        View decorView = getWindow().getDecorView();
//        int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOption);
//
//    }

    /**
     * Khởi tạo View
     */
    private void initFindView() {
        mToolBar = findViewById(R.id.tool_bar_main);
        mViewPager = findViewById(R.id.pagerMainContent);

        mLayoutMainContent = findViewById(R.id.mainContent);
        mTabLayout = findViewById(R.id.tablayout_main);

    }

    private void registerService() {
        mTimerReceiver = new TimerReceiver();
        IntentFilter intentFilter = new IntentFilter(TimerSongService.ACTION_FINISH_TIMER);
        intentFilter.addAction(TimerSongService.ACTION_START_TIMER);
        intentFilter.addAction(TimerSongService.ACTION_TICK_TIMER);
        registerReceiver(mTimerReceiver, intentFilter);
    }

    private void initMinimizePlaying() {
        Log.d(TAG, "initMinimizePlaying: ");

        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        SongModel songPlay;
                        songPlay = PlayService.getCurrentSongPlaying();
                        if (songPlay == null) {
                            songPlay = PlayService.getSongIsPlaying();
                        }
                        if (songPlay != null) {
                            Log.d(TAG, "initMinimizePlaying: " + songPlay.getTitle());
//                            showMinimizePlaying(songPlay);
                            PlayService.revertListSongPlaying();
                            initNotificationPlay();
                            mMinimizeSongFragment = MinimizeSongFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().add(R.id.frgMinimizeSong, mMinimizeSongFragment).commit();

                        }

                        Log.d(TAG, "initMinimizePlaying: " + songPlay);
                    }
                }
        );
    }


    private void initNotificationPlay() {
        SongModel songPlaying = PlayService.getCurrentSongPlaying();
        Log.d(TAG, "initNotificationPlay: " + songPlaying);
        if (songPlaying == null) {
            return;
        }
        createNotificationChanel();
        //create layout notification
        mNotificationlayoutPlaying = new RemoteViews(getPackageName(), R.layout.layout_notificatoin_play);

        //set content notification

        mNotificationlayoutPlaying.setImageViewBitmap(R.id.imgSongMinimize, ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_128));
        mNotificationlayoutPlaying.setTextViewText(R.id.txtTitleMinimize, songPlaying.getTitle());
        mNotificationlayoutPlaying.setTextViewText(R.id.txtArtistMinimize, songPlaying.getArtist());
        if (PlayService.isPlaying()) {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_pause_circle_outline_black_32dp);
        } else {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_play_circle_outline_black_32dp);
        }
        //\set content notification

        //playback activity
        Intent mainIntent = new Intent(this, MainActivity.class);//mới change
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PlayActivity.class);
        stackBuilder.addNextIntent(mainIntent);


        //intent back to activity
        PendingIntent pendingIntentPlay = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.notificationLayout, pendingIntentPlay);
        //\intent back to activity

        //intent play song
        Intent playButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        playButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PLAY_NOTIFY);
        playButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent playButtonPending = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);

        Intent nextButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        nextButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_NEXT_NOTIFY);
        nextButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent nextButtonPending = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);

        Intent prevButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        prevButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PREV_NOTIFY);
        prevButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent prevButtonPending = PendingIntent.getBroadcast(this, 0, prevButtonIntent, 0);

        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPlaySong, playButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnNextSong, nextButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPrevSong, prevButtonPending);

        //\intent play song
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PLAY_CHANEL_ID.toString())
                .setSmallIcon(R.drawable.ic_album_black_24dp)
                .setDefaults(0)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntentPlay)//mới change
                .setContentIntent(playButtonPending)
                .setContentIntent(nextButtonPending)
                .setContentIntent(prevButtonPending)
//                .setOngoing(true)
                .setCustomContentView(mNotificationlayoutPlaying);//mới change
//                .setOngoing(true);
//                .setCustomBigContentView(notifcationlayoutExpand);//mới change

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(PLAY_NOTIFICATION_ID, builder.build());


    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence chanelName = PLAY_CHANEL_ID.toString();
            String description = "TEST NOTIFICATION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel chanel = new NotificationChannel(PLAY_CHANEL_ID.toString(), chanelName, importance);
            chanel.setDescription(description);
            chanel.setSound(null, null);
            chanel.setVibrationPattern(new long[]{0});
            chanel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);

//            notificationManager.notify(PLAY_NOTIFICATION_ID);
        }
    }

    private void initTabLayoutIcon() {

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            Objects.requireNonNull(mTabLayout.getTabAt(i)).setIcon(mIconsTabDefault[i]);
        }
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(mIconsTabActive[0]);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mTabMainTitle[0]);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Objects.requireNonNull(getSupportActionBar()).setTitle(mTabMainTitle[tab.getPosition()]);
                tab.setIcon(mIconsTabActive[tab.getPosition()]);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(mIconsTabDefault[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /*
     * Khởi tạo function search
     * */

    public void SearchByFragment(int fragmentIndex) {
        switch (fragmentIndex) {
            case 1:
                FragmentListSong fragmentListSong = (FragmentListSong) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentListSong != null) {
                    fragmentListSong.UpdateSearch(mSearchValue);
                }
                break;
            case 2:
                FragmentPlaylist fragmentPlaylist = (FragmentPlaylist) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentPlaylist != null) {
                    fragmentPlaylist.UpdateSearch(mSearchValue);
                }
                break;
            case 3:
                FragmentArtist fragmentArtist = (FragmentArtist) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentArtist != null) {
                    fragmentArtist.UpdateSearch(mSearchValue);
                }
                break;
            case 4:
                FragmentAlbum fragmentAlbum = (FragmentAlbum) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentAlbum != null) {
                    fragmentAlbum.UpdateSearch(mSearchValue);
                }
                break;
            case 5:
                FragmentFolder fragmentFolder = (FragmentFolder) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentFolder != null) {
                    fragmentFolder.UpdateSearch(mSearchValue);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SongModel songModelFromArtist;

        if (resultCode == ArtistModel.RequestCode) { // == 2
            songModelFromArtist = (SongModel) Objects.requireNonNull(data).getSerializableExtra(ArtistModel.RequestCodeString);
            ArrayList<SongModel> listSongFromArtist = ArtistProvider.getArtistSongs(MainActivity.this, songModelFromArtist.getArtist());
            playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
            Log.d(TAG, "onActivityResult: PLAY FROM ARTIST: " + songModelFromArtist.getTitle() + "_" + listSongFromArtist.size());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        togglePlayingMinimize("MAIN");
    }

    /**
     * Sự kiện nhấn nút quay lại
     */
    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
//            // Otherwise, select the previous step.

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }

    }

    /**
     * Hiện PlayActivity
     *
     * @param Sender
     */
    @Override
    public void playSongsFromFragmentListToMain(String Sender) {
//        Log.d(TAG, "playSongsFromFragmentListToMain: " + "SONG " + songPlay.getTitle() + " LIST " + songList.size());
        refreshNotificationPlaying(PlayService.ACTION_PLAY);
        handleShowPlayActivityWithSongList();
    }

    /**
     * Ẩn/ hiện mimimize playing
     *
     * @param sender
     */
    @Override
    public void togglePlayingMinimize(String sender) {
        Log.d(TAG, "togglePlayingMinimize: RUNNING SERVICE="+PlayService.isPlaying());
        if (mMinimizeSongFragment != null) {
            if (PlayService.isPlaying()) {
                mMinimizeSongFragment.refreshControls(PlayService.ACTION_PLAY);
            } else {
                mMinimizeSongFragment.refreshControls(-1);
            }

        }

    }

    @Override
    public void refreshNotificationPlaying(int action) {
        SongModel songPlaying = PlayService.getCurrentSongPlaying();
        if (songPlaying != null) {
            Log.d(TAG, "initNotificationPlay: " + songPlaying.getTitle());
        }
        if (songPlaying == null) {
            return;
        }
        createNotificationChanel();
        //create layout notification
        mNotificationlayoutPlaying = new RemoteViews(getPackageName(), R.layout.layout_notificatoin_play);

//        //set content notification

        mNotificationlayoutPlaying.setImageViewBitmap(R.id.imgSongMinimize, ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_128));
        mNotificationlayoutPlaying.setTextViewText(R.id.txtTitleMinimize, songPlaying.getTitle());
        mNotificationlayoutPlaying.setTextViewText(R.id.txtArtistMinimize, songPlaying.getArtist());
        if (action != PlayService.ACTION_PAUSE) {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_pause_circle_outline_black_32dp);
        } else {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_play_circle_outline_black_32dp);
        }
        //\set content notification

        //playback activity
        Intent mainIntent = new Intent(this, MainActivity.class);//mới change
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PlayActivity.class);
        stackBuilder.addNextIntent(mainIntent);


        //intent back to activity
        PendingIntent pendingIntentPlay = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.notificationLayout, pendingIntentPlay);
        //\intent back to activity

        //intent play song
        Intent playButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        playButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PLAY_NOTIFY);
        playButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent playButtonPending = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);

        Intent nextButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        nextButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_NEXT_NOTIFY);
        nextButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent nextButtonPending = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);

        Intent prevButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        prevButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PREV_NOTIFY);
        prevButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent prevButtonPending = PendingIntent.getBroadcast(this, 0, prevButtonIntent, 0);

        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPlaySong, playButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnNextSong, nextButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPrevSong, prevButtonPending);

        //\intent play song
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PLAY_CHANEL_ID.toString())
                .setSmallIcon(R.drawable.ic_album_black_24dp)
                .setDefaults(0)
                .setContentIntent(pendingIntentPlay)//mới change
                .setContentIntent(playButtonPending)
                .setContentIntent(nextButtonPending)
                .setContentIntent(prevButtonPending)
                .setCustomContentView(mNotificationlayoutPlaying);//mới change


        if (action != PlayService.ACTION_PAUSE) {
            builder.setOngoing(true);
        } else {
            builder.setOngoing(false);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(PLAY_NOTIFICATION_ID, builder.build());
        refreshMinimizePlaying(action);
    }

    private void refreshMinimizePlaying(int action) {
        mMinimizeSongFragment.refreshControls(action);
    }

    /**
     * Hiện Play Activity
     */
    private void handleShowPlayActivityWithSongList() {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        startActivity(mIntentPlayActivity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentRefreshNotification(int action) {
        refreshNotificationPlaying(action);
    }

    @Override
    public void onFragmentShowPlayActivity() {
        handleShowPlayActivityWithSongList();
    }

    @Override
    public void onFragmentLoaded(final int heightLayout) {
        Log.d(TAG, "onFragmentLoaded: " + heightLayout);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.requestFocus();
                mViewPager.requestLayout();
                mViewPager.setPadding(0, 0, 0, heightLayout);
            }
        });


    }
}