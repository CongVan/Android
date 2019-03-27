package com.example.musicforlife;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        frameLayoutContainer = findViewById(R.id.frame_container);
//        btnBottomSheet = findViewById(R.id.btn_bottom_sheet);
//        layoutBottomSheet = findViewById(R.id.bottom_sheet);
//        fragmentThread.run();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
//
//
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//

//        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
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
/**
 * bottom sheet state change listener
 * we are changing button text when sheet changed state
 * */
//        layoutBottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
////                        bottomNavigationView.setSystemUiVisibility(View.INVISIBLE);
//                        layoutBottomSheet.bringToFront();
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
////                        bottomNavigationView.setSystemUiVisibility(View.VISIBLE);
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });

    }

    /**
     * manually opening / closing bottom sheet on button click
     */

    public void toggleBottomSheet(View view) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnBottomSheet.setText("Expand sheet");
        }
    }

    private void loadFragment() {
        //load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentListSong = FragmentListSong.newInstance();
        transaction.replace(R.id.frame_container, fragmentListSong);
        transaction.commit();

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
