package com.example.musicforlife;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.artist.ArtistModel;
import com.example.musicforlife.artist.ArtistProvider;
import com.example.musicforlife.callbacks.MainCallbacks;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;


public class MainActivity extends AppCompatActivity implements MainCallbacks, View.OnClickListener {

    FragmentListSong fragmentListSong;
    FrameLayout frameLayoutContainer = null;
    Handler _mainHandle = new Handler();
    //    FragmentThread fragmentThread = new FragmentThread();
    //    @BindView(R.id.btn_bottom_sheet)
    private LinearLayout mLayoutPlayingMinimizie;
    private TextView mTextViewTitleSongMinimize;
    private TextView mTextViewArtistMinimize;
    private ImageView mImageViewSongMinimize;
    private CardView mCardViewPlayingMinimize;
    private CoordinatorLayout mLayoutMainContent;
    private static MainActivity mMainActivity;
    //    @BindView(R.id.bottom_sheet)
    BottomSheetBehavior bottomSheetBehaviorPlay;


    //    private FragmentTransaction mFragmentTransaction;
    private Intent mIntentPlayActivity;
    private Fragment mFragmentListSong, mFragmentRecent, mFramentArtist, mFragmentAlbum, mFragmentFolder;
    private int currentFragmentContentId;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem mPrevMenuBottomNavigation;
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private int[] mTabIcons = {
            R.drawable.ic_av_timer_black_24dp,
            R.drawable.ic_library_music_black_24dp,
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_album_black_24dp,
            R.drawable.ic_folder_black_24dp,
    };
    private ImageView imageViewBackgroundMain;


    public static DatabaseManager mDatabaseManager;

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
        mLayoutPlayingMinimizie.setOnClickListener(this);
        mCardViewPlayingMinimize.setOnClickListener(this);
        mMainActivity = MainActivity.this;

//        togglePlayingMinimize("MAIN");
        pagerAdapter = new PagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, null);
        mViewPager.setOffscreenPageLimit(1);
        setSupportActionBar(mToolBar);
        mTabLayout.setupWithViewPager(mViewPager);
        setupLayoutTransparent();
        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
        new intitSongFromDevice().execute();

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                SongModel.deleteAllSong(mDatabaseManager);
//                new intitSongFromDevice().execute();
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).run();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_main).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(MainActivity.this);
        mLayoutMainContent.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
//        mLayoutMainContent.setBackground(ImageHelper.getMainBackgroundDrawable());
    }

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
    }

    private void initDataBaseFromDevice() {

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
            playSongsFromFragmentListToMain(FragmentPlaylist.SENDER, songModelFromArtist, listSongFromArtist);
            Log.d(TAG, "onActivityResult: PLAY FROM ARTIST: " + songModelFromArtist.getTitle() + "_" + listSongFromArtist.size());
        }
    }

    //    private boolean requestPermission(){
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//            return true;
//        }
//    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static final String TEST_MESSAGE = "PlayModel";

//    private void loadFragment(Fragment fragment, String tag) {
//        //load fragment
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
////        fragmentListSong = FragmentListSong.newInstance();
//        transaction.add(R.id.frame_container, fragment, tag);
//        transaction.addToBackStack(tag);
//        transaction.commit();
//
//    }

    //    @RequiresApi(api = Build.VERSION_CODES.O)


    @Override
    protected void onResume() {
        super.onResume();
        togglePlayingMinimize("MAIN");
    }

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

    private void handleShowPlayActivity(ArrayList<SongModel> songs) {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        }
//        Intent intent=new Intent(MainActivity.this,PlayActivity.class);
        mIntentPlayActivity.putExtra(PlayActivity.EXTRA_PLAYING_LIST, songs);
        startActivity(mIntentPlayActivity);

//        TaskStackBuilder.create(this)
//                .addParentStack(MainActivity.class)
//                .addNextIntent(new Intent(this, MainActivity.class)
//                        )
//                .startActivities();
//
//        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//
//        int sizeStack =  am.getRunningTasks(2).size();
//
//        for(int i = 0;i < sizeStack;i++){
//
//            ComponentName cn = am.getRunningTasks(2).get(i).topActivity;
//            Log.d("MAIN_ACTIVITY", cn.getClassName());
//        }
    }


    @Override
    public void TestMessageFromFragmentToActivity(String sender) {

    }

    @Override
    public void playSongFromFragmentListToMain(String sender, SongModel songModel) {
        Toast.makeText(MainActivity.this, "Playsong form Main SONG ID: " + songModel.getSongId(), Toast.LENGTH_SHORT).show();
        ArrayList<SongModel> songModelArrayList = new ArrayList<SongModel>();
        songModelArrayList.add(songModel);
        handleShowPlayActivity(songModelArrayList);
    }

    @Override
    public void playSongsFromFragmentListToMain(String Sender, SongModel songPlay, ArrayList<SongModel> songList) {
        Log.d(TAG, "playSongsFromFragmentListToMain: " + "SONG " + songPlay.getTitle() + " LIST " + songList.size());
        handleShowPlayActivityWithSongList(songPlay, songList, PlayActivity.TYPE_SHOW_NEW);
    }

    @Override
    public void playSongsIdFromFragmentListToMain(String Sender, SongModel songPlay, ArrayList<Integer> songsId) {
        handleShowPlayActivityWithSongIdList(songPlay, songsId);
    }

    @Override
    public void togglePlayingMinimize(String sender) {

        if (PlayService.getCurrentSongPlaying() != null) {
            Log.d(TAG, "togglePlayingMinimize: " + sender + " SONG PLAYING " + PlayService.getCurrentSongPlaying().getTitle());
            SongModel songPlaying = PlayService.getCurrentSongPlaying();
            mTextViewTitleSongMinimize.setText(songPlaying.getTitle());
            mTextViewArtistMinimize.setText(songPlaying.getArtist());

            Bitmap bitmap = ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_file_128);
//            Bitmap blurBitmap = ImageHelper.blurBitmap(bitmap, 2.0f, 80);
//            BitmapDrawable background = new BitmapDrawable(blurBitmap);
            mImageViewSongMinimize.setImageBitmap(bitmap);
//            mLayoutPlayingMinimizie.setBackground(background);


//            mLayoutPlayingMinimizie.setBackground(ImageHelper.getMimimizeBackgroundDrawable());
//

            mLayoutPlayingMinimizie.post(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setPadding(0, 0, 0, mLayoutPlayingMinimizie.getMeasuredHeight());

                    mLayoutPlayingMinimizie.setVisibility(View.VISIBLE);
                    Log.d(TAG, "togglePlayingMinimize: HEIGHT" + mLayoutPlayingMinimizie.getMeasuredHeight());
                }
            });
        } else {
            mLayoutPlayingMinimizie.post(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setPadding(0, 0, 0, 0);
                    mLayoutPlayingMinimizie.setVisibility(View.GONE);
                    Log.d(TAG, "togglePlayingMinimize: HEIGHT" + mLayoutPlayingMinimizie.getMeasuredHeight());
                }
            });
        }


    }

    private void handleShowPlayActivityWithSongList(SongModel songPlay, ArrayList<SongModel> songList, int typeShow) {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
//        Intent intent=new Intent(MainActivity.this,PlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PLAY_LIST", songList);
        bundle.putSerializable("PLAY_SONG", songPlay);
        bundle.putInt("TYPE_SHOW", typeShow);

        mIntentPlayActivity.putExtras(bundle);
        startActivity(mIntentPlayActivity);
    }

    private void handleShowPlayActivityWithSongIdList(SongModel songPlay, ArrayList<Integer> songIdList) {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
//        Intent intent=new Intent(MainActivity.this,PlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PLAY_LIST", songIdList);
        bundle.putSerializable("PLAY_SONG", songPlay);
        mIntentPlayActivity.putExtras(bundle);
        startActivity(mIntentPlayActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomSheetPlay:
            case R.id.cardViewPlayingMinimize:
                if (PlayService.getCurrentSongPlaying() == null) {
                    break;
                }
                handleShowPlayActivityWithSongList(null, null, PlayActivity.TYPE_SHOW_RESUME);
                break;

            default:
                break;
        }
    }


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
            for (SongModel song : tempAudioList) {
                long id = SongModel.insertSong(mDatabaseManager, song);
                Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
            }
            return tempAudioList;
        }

    }
}