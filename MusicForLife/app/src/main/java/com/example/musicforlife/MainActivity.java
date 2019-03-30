package com.example.musicforlife;


import android.app.ActivityManager;
import android.app.Fragment;
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

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;

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
    private Fragment mFragmentListSong;
    private Fragment mFragmentRecent;
    private BottomNavigationView mBottomNavigationView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        frameLayoutContainer = findViewById(R.id.frame_container);

        mBottomNavigationView = findViewById(R.id.navigation_main);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // NAV - Gần đây
                    case R.id.navigation_recent: {
                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_recent &&
                                getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) != null
                        ) {
                            break;
                        }
                        if (mFragmentRecent == null) {
                            mFragmentRecent = new FragmentRecent();
                        }
                        if (getFragmentManager().findFragmentByTag(FragmentRecent.SENDER) == null) {
                            loadFragment(mFragmentRecent, FragmentRecent.SENDER);
                        } else {
                            getFragmentManager().popBackStack(FragmentRecent.SENDER, 0);
                        }

                        break;
                    }
                    // NAV - Bài hát
                    case R.id.navigation_song: {
                        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_song
                                && getFragmentManager().findFragmentByTag(FragmentListSong.SENDER) != null
                        ) {
                            break;
                        }
                        if (mFragmentListSong == null) {
                            mFragmentListSong = FragmentListSong.newInstance();
                        }
                        if (getFragmentManager().findFragmentByTag(FragmentListSong.SENDER) == null) {
                            loadFragment(mFragmentListSong, FragmentListSong.SENDER);
                        } else {
                            getFragmentManager().popBackStack(FragmentListSong.SENDER, 0);
                        }

//                        Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
////                        fragmentThread.run();
                        break;
                    }
//                    case  R.id.navigation_song:{
//                        Toast.makeText(MainActivity.this,"SONG",Toast.LENGTH_SHORT).show();
//                        break;
//                    }
                    case R.id.navigation_album: {
//                        Toast.makeText(MainActivity.this, "ALBUM", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return true;
            }
        });
//        mBottomNavigationView.setSelectedItemId(R.id.navigation_recent);
//        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
//        View view=mBottomNavigationView.findViewById(R.id.navigation_recent);
//        view.performClick();
//        loadFragment(mFragmentRecent,FragmentRecent.SENDER);
    }


    public static final String TEST_MESSAGE = "Play";

    private void loadFragment(Fragment fragment, String tag) {
        //load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        fragmentListSong = FragmentListSong.newInstance();
        transaction.add(R.id.frame_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            mBottomNavigationView.setSelectedItemId(getFragmentManager().getFragments().get(0).getId());
        } else {
            super.onBackPressed();
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

}
