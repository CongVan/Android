package com.example.musicforlife;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.musicforlife.db.DatabaseHelper;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.FragmentListPlaying;
import com.example.musicforlife.play.FragmentPlayAdapter;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.play.PlayInterface;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements PlayInterface {

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private DatabaseHelper mDatabaseHelper;
    private PagerAdapter pagerAdapter;
    private PlayService mPlayService;
    private ImageView imageViewBackgroundMain;


    private static final String TAG = "PlayActivity";
    public static final String EXTRA_PLAYING_LIST = "EXTRA_PLAYING_LIST";
    public static final String SENDER = "PLAY_ACTIVITY";

    private SongModel mSongPlaying = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window window = PlayActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(PlayActivity.this, R.color.colorPrimary));
//        final Bitmap bitmapBackgroundMain= BitmapFactory.decodeResource(PlayActivity.this.getResources(), R.drawable.background_1);
//        //BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.background);
//        //getBitmap(R.drawable.background_gradient);
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
//                Blurry.with(MainActivity.this)
//                        .radius(10)
//                        .sampling(10)
//                        .from(bitmapBackgroundMain)
////                        .color(Color.argb(66, 255, 255, 0))
////                        .async()
//                        .into(imageViewBackgroundMain );
//            }
//        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<SongModel> playList = null;

        if (bundle.getSerializable("PLAY_LIST") != null) {
            playList = (ArrayList<SongModel>) bundle.getSerializable("PLAY_LIST");
        } else {
            playList = (ArrayList<SongModel>) intent.getSerializableExtra(PlayActivity.EXTRA_PLAYING_LIST);
        }
        Log.d(TAG, "onCreate: "+"PLAY LIST "+playList.size());
        if (bundle.getSerializable("PLAY_SONG") != null) {
            mSongPlaying = (SongModel) bundle.getSerializable("PLAY_SONG");
        } else {
            if (playList.size() > 0) {
                mSongPlaying = playList.get(0);

            }
        }

        PlayService.addSongsToPlayingList(playList);
        Log.d(TAG, "onCreate: "+"PLAY SONG "+mSongPlaying.getTitle());

//        TextView textView=findViewById(R.id.txtTest);
//        Log.d(TAG, "onCreate: " + songs.size());
//        for (SongModel song : songs
//        ) {
//            Log.d(TAG, "onCreate: " + song.getSongId());
//        }
//        Toast.makeText(PlayActivity.this, songs.size()+"", Toast.LENGTH_SHORT).show();
//        mPlayService = PlayService.newInstance(PlayActivity.this.getApplicationContext(), this);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new FragmentPlayAdapter(getSupportFragmentManager(), mSongPlaying);
        mPager.setAdapter(pagerAdapter);
        mDatabaseHelper = DatabaseHelper.newInstance(getApplicationContext());
        mPlayService = PlayService.newInstance(PlayActivity.this.getApplicationContext(), this, mDatabaseHelper);

        //set animation for slide page
//        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        super.onPause();
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
    }

    public void hidePlayActivity(View view) {
//        Intent i=new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
        onBackPressed();
//        finish();
    }


    @Override
    public void controlSong(String sender, SongModel songModel, int action) {
        switch (action) {
            case PlayService.ACTION_PLAY:
//                if (sender.equals(FragmentListPlaying.SENDER)) {
//
//                }
                Log.d(TAG, "controlSong: PLAY " +sender +" "+songModel.getTitle());
                mPager.setCurrentItem(1);
                mPlayService.play(songModel);
                break;
            case PlayService.ACTION_PAUSE:
                mPlayService.pause();
                break;
            case PlayService.ACTION_RESUME:
                mPlayService.resurme();
                break;
            case PlayService.ACTION_PREV:
                mPlayService.prev();
                break;
            case PlayService.ACTION_NEXT:
                mPlayService.next();
                break;
            default:
                break;
        }
    }

    @Override
    public void updateControlPlaying(String sender, SongModel songModel) {
        ((FragmentPlayAdapter) pagerAdapter).getFragmentPlaying().updateControlPlaying(songModel);
    }

    @Override
    public void updateDuration(String sender, int progress) {
        mPlayService.updateDuration(progress);
    }

    @Override
    public void updateSeekbar(String sender, int duration) {
        ((FragmentPlayAdapter) pagerAdapter).getFragmentPlaying().updateSeekbar(duration);
    }
}
