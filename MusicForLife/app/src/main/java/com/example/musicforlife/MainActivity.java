package com.example.musicforlife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
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
    NestedScrollView layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    LinearLayout topContentPlayedExpanded;
    View topcontentPlayedcollapsed;

    private Animation animationShow, animationHide;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        frameLayoutContainer = findViewById(R.id.frame_container);
        btnBottomSheet = findViewById(R.id.btn_bottom_sheet);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        topContentPlayedExpanded = findViewById(R.id.top_content_played_expanded);
        topcontentPlayedcollapsed = findViewById(R.id.top_content_played_collapsed);
//        fragmentThread.run();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
//
//
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//


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
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
//        layoutBottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        sheetBehavior.setPeekHeight(bottomNavigationView.getLayoutParams().height * 2);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        btnBottomSheet.setText("Close Sheet");
//                        bottomNavigationView.setSystemUiVisibility(View.INVISIBLE);
//                        layoutBottomSheet.bringToFront();
//                        topContentPlayedExpanded.setVisibility(View.VISIBLE);
//
//                        topcontentPlayedcollapsed.setVisibility(View.GONE);
                        hideView(topcontentPlayedcollapsed);
                        showView(topContentPlayedExpanded);
                        Toast.makeText(MainActivity.this, "EXPANDED", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        showView(topcontentPlayedcollapsed);
                        hideView(topContentPlayedExpanded);
                        btnBottomSheet.setText("Expand Sheet");
//                        bottomNavigationView.setSystemUiVisibility(View.VISIBLE);
//                        topContentPlayedExpanded.setVisibility(View.GONE);
//                        topcontentPlayedcollapsed.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "COLLAPSE", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        Toast.makeText(MainActivity.this, "DRAGGING", Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Toast.makeText(MainActivity.this, "SETTLING", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Toast.makeText(MainActivity.this,"SLIDED" +slideOffset,Toast.LENGTH_SHORT).show();
//                btnBottomSheet.setText(String.valueOf(slideOffset));
            }
        });
        animationHide = AnimationUtils.loadAnimation(MainActivity.this, R.anim.view_hide);
        animationShow = AnimationUtils.loadAnimation(MainActivity.this, R.anim.view_show);
    }

    private void showView(final View view) {
        //left to right
//        TranslateAnimation animation= new TranslateAnimation(0,-view.getWidth(),0,0);
//        animation.setFillAfter(true);
//        view.startAnimation(animation);
//        view.setVisibility(View.VISIBLE);

        view.startAnimation(animationShow);
        view.setVisibility(View.VISIBLE);
//        view.animate().alpha(1.0f).translationY(view.getHeight()).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.VISIBLE);
//            }
//        });
//        view.animate().alpha(1.0f).translationY(0).setInterpolator(
//                new DecelerateInterpolator(1.4f)
//        ).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
    }

    private void hideView(final View view) {
//        TranslateAnimation animation= new TranslateAnimation(0,view.getWidth(),0,0);
//        animation.setFillAfter(true);
//        view.startAnimation(animation);
//        view.setVisibility(View.GONE);
//        view.animate().alpha(0.0f).translationY(view.getHeight()).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.GONE);
//            }
//        });

        view.startAnimation(animationHide);
        view.setVisibility(View.GONE);
//        view.animate().alpha(0.0f).translationY(layoutBottomSheet.getHeight()).setInterpolator(
//                new DecelerateInterpolator(1.4f)
//        ).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
////                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
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
