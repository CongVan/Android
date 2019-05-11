package com.example.musicforlife;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.artist.ArtistModel;
import com.example.musicforlife.artist.ArtistProvider;
import com.example.musicforlife.callbacks.MainCallbacks;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.playlist.PlaylistSongActivity;
import com.example.musicforlife.playlist.PlaylistSongModel;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainCallbacks, View.OnClickListener {//AudioManager.OnAudioFocusChangeListener


    private LinearLayout mLayoutPlayingMinimizie;
    private TextView mTextViewTitleSongMinimize;
    private TextView mTextViewArtistMinimize;
    private ImageView mImageViewSongMinimize;
    private CardView mCardViewPlayingMinimize;
    private CoordinatorLayout mLayoutMainContent;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private ImageButton mButtonPlayMinimize;
    private ImageButton mButtonNextMinimize;
    private ImageButton mButtonPrevMinimize;

    private static MainActivity mMainActivity;
    private Intent mIntentPlayActivity;
    private PlayService mPlayService;
    public static DatabaseManager mDatabaseManager;
    private AudioManager mAudioManager;

    public static final Integer PLAY_CHANEL_ID = 101;
    public static final Integer PLAY_NOTIFICATION_ID = 101;

    public static MainActivity getMainActivity() {
        return mMainActivity;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    private static final String TAG = "MainActivity";

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
        mViewPager.setPageTransformer(true, null);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mTabLayout.setupWithViewPager(mViewPager);
        mPlayService = PlayService.newInstance();
//        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        setSupportActionBar(mToolBar);
        setupLayoutTransparent();
        initDataBaseFromDevice();
        initMinimizePlaying();
        initReceiver();
//        initNotificationPlay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar, menu);


        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_main).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);


        return true;
    }

    /**
     * làm status bar trong suốt và set padding cho layout main
     */
    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(MainActivity.this);
        mLayoutMainContent.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);

//        mLayoutMainContent.setBackground(ImageHelper.getMainBackgroundDrawable());
    }

    /**
     * Khởi tạo View
     */
    private void initFindView() {
        mToolBar = findViewById(R.id.tool_bar_main);
        mViewPager = findViewById(R.id.pagerMainContent);
        mLayoutPlayingMinimizie = findViewById(R.id.bottomSheetPlay);
        mTextViewTitleSongMinimize = findViewById(R.id.txtTitleMinimize);
        mTextViewArtistMinimize = findViewById(R.id.txtArtistMinimize);
        mImageViewSongMinimize = findViewById(R.id.imgSongMinimize);
        mCardViewPlayingMinimize = findViewById(R.id.cardViewPlayingMinimize);
        mLayoutMainContent = findViewById(R.id.mainContent);
        mTabLayout = findViewById(R.id.tablayout_main);

        mButtonPlayMinimize = findViewById(R.id.btnPlaySong);
        mButtonNextMinimize = findViewById(R.id.btnNextSong);
        mButtonPrevMinimize = findViewById(R.id.btnPrevSong);


        mButtonPlayMinimize.setOnClickListener(this);
        mButtonNextMinimize.setOnClickListener(this);
        mButtonPrevMinimize.setOnClickListener(this);
        mLayoutPlayingMinimizie.setOnClickListener(this);
        mCardViewPlayingMinimize.setOnClickListener(this);

    }

    /**
     * Khởi tạo data đọc từ bộ nhớ
     */
    private void initDataBaseFromDevice() {
        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
//        mDatabaseManager.resetDB();
        new intitSongFromDevice().execute();
    }

    private void initMinimizePlaying() {
        Log.d(TAG, "initMinimizePlaying: ");
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        SongModel songPlay = null;
                        songPlay = PlayService.getCurrentSongPlaying();
                        if (songPlay == null) {
                            songPlay = PlayService.getSongIsPlaying();
                        }

                        if (songPlay != null) {
                            Log.d(TAG, "initMinimizePlaying: " + songPlay.getTitle());
                            showMinimizePlaying(songPlay);
                            PlayService.revertListSongPlaying();
                            initNotificationPlay();

                        } else {
                            hideMinimizePlaying();
                        }
                        Log.d(TAG, "initMinimizePlaying: " + songPlay);
                    }
                }
        );
    }

    private void initReceiver() {
//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            // could not get audio focus.
//        }

//        IntentFilter mainFilter = new IntentFilter("android.media.AUDIO_BECOMING_NOISY");
//        BroadcastReceiver receiver = new BecomingNoisyReceiver();
//        registerReceiver(receiver, mainFilter);
//
//        sendBroadcast(new Intent("OKOK"));
//
//        MediaSessionCompat.Callback callback = new
//                MediaSessionCompat.Callback() {
//                    @Override
//                    public void onPlay() {
//                        registerReceiver(mBecomingNoisyReceiver, mIntentFilterNoisy);
//                    }
//
//                    @Override
//                    public void onStop() {
//                        unregisterReceiver(mBecomingNoisyReceiver);
//                    }
//                };

    }

    private void initNotificationPlay() {
        SongModel songPlaying = PlayService.getCurrentSongPlaying();
        Log.d(TAG, "initNotificationPlay: " + songPlaying);
        if (songPlaying == null) {
            return;
        }
        createNotificationChanel();
        //create layout notification
        RemoteViews notificationlayout = new RemoteViews(getPackageName(), R.layout.layout_notificatoin_play);
        Bitmap bitmapBg = ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_file_128);
        Bitmap bitmapBgBlur = ImageHelper.blurBitmap(bitmapBg, 2.0f, 4);
        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgBlur.getWidth(), bitmapBgBlur.getHeight(), getResources().getColor(R.color.colorBgPrimaryOverlay));
        Bitmap bitmapBgOverlay = ImageHelper.overlayBitmapToCenter(bitmapBgBlur, bitmapOverlay);
        notificationlayout.setImageViewBitmap(R.id.imgSongMinimize, bitmapBgOverlay);
        notificationlayout.setTextViewText(R.id.txtTitleMinimize, songPlaying.getTitle());
        notificationlayout.setTextViewText(R.id.txtArtistMinimize, songPlaying.getArtist());


        //playback activity
        Intent mainIntent = new Intent(this, PlayActivity.class);//mới change
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(PlayActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(mainIntent);
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //tap notification open play activity
        PendingIntent pendingIntentPlay = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationlayout.setOnClickPendingIntent(R.id.notificationLayout, pendingIntentPlay);

        // adding action to left button
        Intent playIntent = new Intent(this, NotificationIntentService.class);
        playIntent.setAction(String.valueOf(PlayService.ACTION_PLAY));
        notificationlayout.setOnClickPendingIntent(R.id.btnPlaySong, PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PLAY_CHANEL_ID.toString())
                .setSmallIcon(R.drawable.ic_album_black_24dp)
                .setDefaults(0)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntentPlay)//mới change
                .setDeleteIntent(PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent(this, DismissBroadcastReceiver.class), 0))
                .setCustomContentView(notificationlayout);//mới change
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SongModel songModelFromArtist = null;
//        if (resultCode == ArtistModel.RequestCode) { // == 2
//            songModelFromArtist = (SongModel) data.getSerializableExtra(ArtistModel.RequestCodeString);
//            playSongFromFragmentListToMain(FragmentPlaylist.SENDER, songModelFromArtist);
//        }
        if (resultCode == ArtistModel.RequestCode) { // == 2
            songModelFromArtist = (SongModel) data.getSerializableExtra(ArtistModel.RequestCodeString);
            ArrayList<SongModel> listSongFromArtist = ArtistProvider.getArtistSongs(MainActivity.this, songModelFromArtist.getArtist());
            playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
            Log.d(TAG, "onActivityResult: PLAY FROM ARTIST: " + songModelFromArtist.getTitle() + "_" + listSongFromArtist.size());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        togglePlayingMinimize("MAIN");
//        initMinimizePlaying();
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

//    public void showPlayActivity(View view) {
//        handleShowPlayActivity();
//    }

    /**
     * Hiện PlayActivity
     *
     * @param Sender
     */
    @Override
    public void playSongsFromFragmentListToMain(String Sender) {
//        Log.d(TAG, "playSongsFromFragmentListToMain: " + "SONG " + songPlay.getTitle() + " LIST " + songList.size());
        handleShowPlayActivityWithSongList();
    }

    /**
     * Ẩn/ hiện mimimize playing
     *
     * @param sender
     */
    @Override
    public void togglePlayingMinimize(String sender) {
        SongModel songPlay = PlayService.getCurrentSongPlaying();
        if (songPlay != null) {
            showMinimizePlaying(songPlay);

        } else {
            hideMinimizePlaying();
        }

    }

    private void showMinimizePlaying(SongModel songPlaying) {
        Log.d(TAG, "togglePlayingMinimize:  SONG PLAYING " + songPlaying.getTitle());

        mTextViewTitleSongMinimize.setText(songPlaying.getTitle());
        mTextViewArtistMinimize.setText(songPlaying.getArtist());
        Bitmap bitmap = ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_128);
        mImageViewSongMinimize.setImageBitmap(bitmap);

        //update controls play
        if (PlayService.isPlaying()) {
            mButtonPlayMinimize.setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_pause_circle_outline_black_32dp));
        } else {
            mButtonPlayMinimize.setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_play_circle_outline_black_32dp));
        }
        //\ update controls play
        mLayoutPlayingMinimizie.post(new Runnable() {
            @Override
            public void run() {
                mLayoutPlayingMinimizie.measure(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                Log.d(TAG, "run: SET PADDING MINIMIZE " + mLayoutPlayingMinimizie.getMeasuredHeight());
                mViewPager.setPadding(0, 0, 0, mLayoutPlayingMinimizie.getMeasuredHeight() + 16);
                mLayoutPlayingMinimizie.setVisibility(View.VISIBLE);
            }
        });


//        mLayoutPlayingMinimizie.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
////                mLayoutPlayingMinimizie.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//
//            }
//        });

    }


    private void hideMinimizePlaying() {
        mLayoutPlayingMinimizie.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setPadding(0, 0, 0, 0);
                mLayoutPlayingMinimizie.setVisibility(View.GONE);
                Log.d(TAG, "togglePlayingMinimize: HEIGHT" + mLayoutPlayingMinimizie.getMeasuredHeight());
            }
        });
    }

    /**
     * Hiện Play Activity
     */
    private void handleShowPlayActivityWithSongList() {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
//        Intent intent=new Intent(MainActivity.this,PlayActivity.class);
//        Bundle bundle = new Bundle();
////        bundle.putSerializable("PLAY_LIST", songList);
////        bundle.putSerializable("PLAY_SONG", songPlay);
////        bundle.putInt("TYPE_SHOW", typeShow);
////
////        mIntentPlayActivity.putExtras(bundle);
        startActivity(mIntentPlayActivity);
    }


    /**
     * Sự kiện khi nhấn vào minimize play
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomSheetPlay:
            case R.id.cardViewPlayingMinimize:
                handleShowPlayActivityWithSongList();
                break;
            case R.id.btnPlaySong:
                SongModel songPlay = null;
                songPlay = PlayService.getCurrentSongPlaying();
                if (songPlay == null) {
                    songPlay = PlayService.getSongIsPlaying();
                }
                if (songPlay == null) {
                    Toast.makeText(MainActivity.this, "Không tìm thấy bài hát.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (PlayService.isPlaying()) {
                    mPlayService.pause();
                    mButtonPlayMinimize.setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_play_circle_outline_black_32dp));
                } else if (PlayService.isPause()) {
                    mPlayService.resurme();
                    mButtonPlayMinimize.setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_pause_circle_outline_black_32dp));
                } else {
                    mPlayService.play(songPlay);
                    mButtonPlayMinimize.setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_pause_circle_outline_black_32dp));
                }
                break;
            case R.id.btnNextSong:
                mPlayService.next(PlayService.ACTION_FROM_USER);
                break;
            case R.id.btnPrevSong:
                mPlayService.prev(PlayService.ACTION_FROM_USER);
                break;
            default:
                break;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mAudioManager.abandonAudioFocus(this);
//    }
//
//    @Override
//    public void onAudioFocusChange(int focusChange) {
//        if(focusChange<=0) {
//            if (PlayService.isPlaying()){
//                mPlayService.pause();
//            }
//            //LOSS -> PAUSE
//        } else {
//            if (!PlayService.isPlaying()){
//                mPlayService.resurme();
//            }
//            //GAIN -> PLAY
//        }
//    }

    //    private class FragmentThread extends Thread {
//        @Override
//        public void run() {
//
//
//            Handler threadHandle = new Handler(Looper.getMainLooper());
//            threadHandle.post(new Runnable() {
//                @Override
//                public void run() {
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    fragmentListSong = FragmentListSong.newInstance();
//                    transaction.replace(R.id.frame_container, fragmentListSong);
//                    transaction.commit();
//                }
//            });
//        }
//    }
    private class intitSongFromDevice extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
//            super.onPostExecute(songModels);
//            for (SongModel song : songModels) {
//                long id = SongModel.insertSong(mDatabaseManager, song);
//                Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
//            }
            FragmentListSong fragmentListSong = (FragmentListSong) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(1);
            if (fragmentListSong != null) {
                fragmentListSong.updateSizeOfListSong();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {
//            SongModel.deleteAllSong(mDatabaseManager);
            Log.d(TAG, "doInBackground: SIZE AUDIOS " + SongModel.getRowsSong(mDatabaseManager));
            ArrayList<SongModel> tempAudioList = SongModel.getAllAudioFromDevice(getApplicationContext());
            Log.d(TAG, "doInBackground: AUDIO " + tempAudioList.size());
            if (SongModel.getRowsSong(mDatabaseManager) != tempAudioList.size()) {
                for (SongModel song : tempAudioList) {
                    long id = SongModel.insertSong(mDatabaseManager, song);
                    Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
                }
            }

            return tempAudioList;
        }

    }
}