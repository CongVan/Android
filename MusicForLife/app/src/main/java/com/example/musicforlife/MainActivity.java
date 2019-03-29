package com.example.musicforlife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.musicforlife.play.FragmentPlayAdapter;

//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    FragmentListSong fragmentListSong;
    FrameLayout frameLayoutContainer = null;
    Handler _mainHandle = new Handler();
    FragmentThread fragmentThread = new FragmentThread();
    //    @BindView(R.id.btn_bottom_sheet)
    Button btnBottomSheet;

    //    @BindView(R.id.bottom_sheet)
    BottomSheetBehavior sheetBehavior;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        frameLayoutContainer = findViewById(R.id.frame_container);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_song: {
//                        Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                        fragmentThread.run();
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

    }

    public static final  String TEST_MESSAGE="Play";
    private void loadFragment() {
        //load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentListSong = FragmentListSong.newInstance();
        transaction.replace(R.id.frame_container, fragmentListSong);
        transaction.commit();

    }
    public void showPlayActivity(View view){
        Intent intent=new Intent(MainActivity.this,PlayActivity.class);
        intent.putExtra(TEST_MESSAGE,"OKOKOK");
        startActivity(intent);
    }



    private class FragmentThread extends Thread {
        @Override
        public void run() {


            Handler threadHandle = new Handler(Looper.getMainLooper());
            threadHandle.post(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    fragmentListSong = FragmentListSong.newInstance();
                    transaction.replace(R.id.frame_container, fragmentListSong);
                    transaction.commit();
                }
            });
        }
    }

}
