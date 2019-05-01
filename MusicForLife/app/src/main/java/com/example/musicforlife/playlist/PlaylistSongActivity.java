package com.example.musicforlife.playlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class PlaylistSongActivity extends AppCompatActivity implements MultiClickAdapterListener {

    RecyclerView mRecylerViewListSong;
    SongPlaylistAdapter mSongPlaylistAdapter;
    ArrayList<SongModel> mListSong;
    CoordinatorLayout mLayoutSongPlaylist;
    Toolbar mToolbar;
    int mCurrentPlaylistId;
    PlaylistModel mCurrentPlaylist;
    TextView mTxtTitlePlaylist;
    TextView mTxtNumberOfSongPlaylist;
    AppBarLayout mAppbarLayoutPlaylist;
    ImageView mImageCoverPlaylist;
    private static PlayService mPlayService;
    private static final String TAG = "PlaylistSongActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_song);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCurrentPlaylistId = bundle.getInt("playlistId");
        }

        initFindViewId();
        initRecyclerViewListSong();
        initToolBarParalax();
    }

    private void initFindViewId() {
        mRecylerViewListSong = findViewById(R.id.rcvListSongPlaylist);
        mToolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        mLayoutSongPlaylist = findViewById(R.id.layoutSongPlaylist);
        mTxtTitlePlaylist = findViewById(R.id.txtTitlePlaylist);
        mTxtNumberOfSongPlaylist = findViewById(R.id.txtNumberOfSongPlaylist);
        mAppbarLayoutPlaylist = findViewById(R.id.htab_appbar);
        mImageCoverPlaylist = findViewById(R.id.imageCoverPlaylist);

        mAppbarLayoutPlaylist.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                    Log.d(TAG, "onOffsetChanged: COLLPASED");
                    //Collapsed

                    getSupportActionBar().setTitle(mCurrentPlaylist.getTitle());

                } else {
                    //Expanded
//                    Log.d(TAG, "onOffsetChanged: EXPANDED");
                    getSupportActionBar().setTitle(" ");

                }
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something here, such as start an Intent to the parent activity.
                finish();
            }
        });
        mPlayService = PlayService.newInstance();

    }

    private void initToolBarParalax() {
        mCurrentPlaylist = PlaylistModel.getPlaylistById(mCurrentPlaylistId);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Utility.setTranslucentStatusBar(this);
//        mAppbarLayoutPlaylist.setPadding(0,Utility.getStatusbarHeight(this),0,0);
//        mToolbar.setPadding(0, Utility.getStatusbarHeight(this), 0, Utility.getStatusbarHeight(this));
//        mLayoutSongPlaylist.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
        mTxtTitlePlaylist.setText(mCurrentPlaylist.getTitle());
        mTxtNumberOfSongPlaylist.setText(String.valueOf(mCurrentPlaylist.getNumberOfSongs()) + " bài hát");

        mImageCoverPlaylist.post(new Runnable() {
            @Override
            public void run() {
                mImageCoverPlaylist.setImageBitmap(ImageHelper.getBitmapFromPath(mCurrentPlaylist.getPathImage(), R.mipmap.playlist_128));
            }
        });
    }

    private void initRecyclerViewListSong() {

        mListSong = PlaylistSongModel.getAllSongFromPlaylistId(mCurrentPlaylistId);
        Log.d(TAG, "initRecyclerViewListSong: SIZE PLAYLIST SONG " + mListSong.size());
        mSongPlaylistAdapter = new SongPlaylistAdapter(this, mListSong, this);
        mRecylerViewListSong.setLayoutManager(new LinearLayoutManager(this));
        mRecylerViewListSong.setHasFixedSize(true);
        mRecylerViewListSong.setAdapter(mSongPlaylistAdapter);

    }

    private void showBottomSheetOptionSong(SongModel song) {
        BottomSheetOptionSong bottomSheetDialogFragment = new BottomSheetOptionSong(song);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    private void playSong(SongModel songPlay) {
        mPlayService.play(songPlay);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayService.initListPlaying(mListSong);
            }
        }).start();

        MainActivity.getMainActivity().playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        playSong(songChose);
    }

    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        showBottomSheetOptionSong(songChose);
    }
}
