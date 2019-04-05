package com.example.musicforlife;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.play.FragmentPlayAdapter;
import com.example.musicforlife.play.ZoomOutPageTransformer;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

import static android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;


public class MainActivity extends AppCompatActivity implements MainCallbacks {

    FragmentListSong fragmentListSong;
    FrameLayout frameLayoutContainer = null;
    Handler _mainHandle = new Handler();
    //    FragmentThread fragmentThread = new FragmentThread();
    //    @BindView(R.id.btn_bottom_sheet)
    Button btnBottomSheet;

    //    @BindView(R.id.bottom_sheet)
    BottomSheetBehavior sheetBehavior;


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


    public static DatabaseHelper mDatabaseHelper;


    private static final String TAG = "MainActivity";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.activity_main);

        final Bitmap bitmapBackgroundMain=BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.background_1);
                //BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.background);
                //getBitmap(R.drawable.background_gradient);
//        imageViewBackgroundMain=findViewById(R.id.imageViewBackgroundMain);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        getSupportActionBar().hide();
//
//        imageViewBackgroundMain.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//        imageViewBackgroundMain.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//        imageViewBackgroundMain.setAdjustViewBounds(false);
//        imageViewBackgroundMain.setScaleType(ImageView.ScaleType.FIT_XY);
//        final CoordinatorLayout mainLayout= findViewById(R.id.mainContent);
//        mainLayout.post(new Runnable() {
//            @Override
//            public void run() {
////                Blurry.with(MainActivity.this)
////                        .radius(10)
////                        .sampling(10)
////                        .from(bitmapBackgroundMain)
//////                        .color(Color.argb(66, 255, 255, 0))
//////                        .async()
////                        .into(imageViewBackgroundMain);
//                Blurry.with(MainActivity.this)
//                        .radius(10)
//                        .sampling(8)
//                        .color(Color.argb(1, 47, 47, 47))
//                        .async()
//                        .animate(500)
//                        .onto();
//            }
//        });

//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        mDatabaseHelper.onUpgrade(db,2,3);

//        frameLayoutContainer = findViewById(R.id.frame_container);

//        mBottomNavigationView = findViewById(R.id.navigation_main);
//        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                currentFragmentContentId = menuItem.getItemId();
////                menuItem.setCheckable(true);
////                for (int i = 0; i < mBottomNavigationView.getMenu().size(); i++) {
////                    MenuItem item = mBottomNavigationView.getMenu().getItem(i);
////                    boolean isChecked = item.getItemId() == menuItem.getItemId();
////                    menuItem.setChecked(isChecked);
////                }
//                switch (menuItem.getItemId()) {
//                    // NAV - Gần đây
//                    case R.id.navigation_recent: {
////                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_recent &&
////                                getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) != null
////                        ) {
////                            break;
////                        }
////                        if (mFragmentRecent == null) {
////                            mFragmentRecent = new FragmentRecent();
////                        }
////                        if (getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) == null) {
////                            loadFragment(mFragmentRecent, FragmentRecent.SENDER);
////                        } else {
////                            getFragmentManager().popBackStack(FragmentRecent.SENDER, 0);
////                        }
//                        mViewPager.setCurrentItem(0);
//                        break;
//                    }
//                    // NAV - Bài hát
//                    case R.id.navigation_song: {
////                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_song
////                                && getFragmentManager().findFragmentByTag(FragmentListSong.SENDER) != null
////                        ) {
////                            break;
////                        }
////                        if (mFragmentListSong == null) {
////                            mFragmentListSong = FragmentListSong.newInstance();
////                        }
////                        if (getFragmentManager().findFragmentByTag(FragmentListSong.SENDER) == null) {
////                            loadFragment(mFragmentListSong, FragmentListSong.SENDER);
////                        } else {
////                            getFragmentManager().popBackStack(FragmentListSong.SENDER, 0);
////                        }
//
////                        Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
//////                        fragmentThread.run();
//                        mViewPager.setCurrentItem(1);
//                        break;
//                    }
//                    // NAV - Ca sĩ
//                    case R.id.navigation_artist: {
////                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_artist &&
////                                getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) != null
////                        ) {
////                            break;
////                        }
////                        if (mFramentArtist == null) {
////                            mFramentArtist = new FragmentArtist();
////                        }
////                        if (getFragmentManager().findFragmentByTag(FragmentArtist.SENDER) == null) {
////                            loadFragment(mFramentArtist, FragmentArtist.SENDER);
////                        } else {
////                            getFragmentManager().popBackStack(FragmentArtist.SENDER, 0);
////                        }
//                        mViewPager.setCurrentItem(2);
//                        break;
//                    }
//                    // NAV - album
//                    case R.id.navigation_album: {
////                        Toast.makeText(MainActivity.this, "ALBUM", Toast.LENGTH_SHORT).show();
////                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_album &&
////                                getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) != null
////                        ) {
////                            break;
////                        }
////                        if (mFragmentAlbum == null) {
////                            mFragmentAlbum = new FragmentAlbum();
////                        }
////                        if (getFragmentManager().findFragmentByTag(FragmentAlbum.SENDER) == null) {
////                            loadFragment(mFragmentAlbum, FragmentAlbum.SENDER);
////                        } else {
////                            getFragmentManager().popBackStack(FragmentAlbum.SENDER, 0);
////                        }
//                        mViewPager.setCurrentItem(4);
//                        break;
//                    }
//                    // NAV - Thư mục
//                    case R.id.navigation_folder: {
////                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_folder &&
////                                getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) != null
////                        ) {
////                            break;
////                        }
////                        if (mFragmentFolder == null) {
////                            mFragmentFolder = new FragmentFolder();
////                        }
////                        if (getFragmentManager().findFragmentByTag(FragmentFolder.SENDER) == null) {
////                            loadFragment(mFragmentFolder, FragmentFolder.SENDER);
////                        } else {
////                            getFragmentManager().popBackStack(FragmentFolder.SENDER, 0);
////                        }
//                        mViewPager.setCurrentItem(5);
//                        break;
//                    }
//                }
//
//                return true;
//            }
//        });
//        mBottomNavigationView.setSelectedItemId(R.id.navigation_recent);
//        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
//        View view=mBottomNavigationView.findViewById(R.id.navigation_recent);
//        view.performClick();
//        loadFragment(mFragmentRecent,FragmentRecent.SENDER);

        mViewPager = (ViewPager) findViewById(R.id.pagerMainContent);
        pagerAdapter = new PagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, null);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                if (mPrevMenuBottomNavigation != null)
//                    mPrevMenuBottomNavigation.setChecked(false);
//                else
//                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
//
//                mBottomNavigationView.getMenu().getItem(i).setChecked(true);
//                mPrevMenuBottomNavigation = mBottomNavigationView.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mDatabaseHelper = DatabaseHelper.newInstance(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!getApplicationContext().getDatabasePath(DatabaseHelper.DATABASE_NAME).exists()) {
                    Log.d(TAG, "run: NOT EXIST DATABASE: ");

                    new intitSongFromDevice().execute();
                }
//                Log.d(TAG, "run:  EXIST DATABASE: ");
            }
        }).run();
        mToolBar = findViewById(R.id.tool_bar_main);
        setSupportActionBar(mToolBar);
        mTabLayout = findViewById(R.id.tablayout_main);
        mTabLayout.setupWithViewPager(mViewPager);
//        for (int i = 0; i < mTabIcons.length; i++) {
//            mTabLayout.getTabAt(i).setIcon(mTabIcons[i]);
//        }
        //Set status bar color
        Window window=MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
//        window.setBackgroundDrawable(MainActivity.this.getDrawable(R.drawable));
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static final String TEST_MESSAGE = "Play";

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

    public void showPlayActivity(View view) {
        handleShowPlayActivity();
    }

    private void handleShowPlayActivity() {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            mIntentPlayActivity.putExtra(TEST_MESSAGE, "OKOKOK");
        }
//        Intent intent=new Intent(MainActivity.this,PlayActivity.class);

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
//        Toast.makeText(this,sender,Toast.LENGTH_SHORT).show();
        handleShowPlayActivity();
    }

    @Override
    public void playSongFromFragmentListToMain(String sender,SongModel songModel) {
        handleShowPlayActivity();
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
            super.onPostExecute(songModels);
            for (SongModel song : songModels) {
                long id = SongModel.insertSong(mDatabaseHelper, song);
                Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {
            ArrayList<SongModel> tempAudioList = SongModel.getAllAudioFromDevice(getApplicationContext());
            return tempAudioList;
        }

    }
}