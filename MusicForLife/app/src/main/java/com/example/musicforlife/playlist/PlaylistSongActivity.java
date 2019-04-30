package com.example.musicforlife.playlist;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;
import java.util.Objects;

public class PlaylistSongActivity extends AppCompatActivity {

    RecyclerView mRecylerViewListSong;
    SongPlaylistAdapter mSongPlaylistAdapter;
    ArrayList<SongModel> mListSong;
    Toolbar mToolbar;
    int mCurrentPlaylistId;
    PlaylistModel mCurrentPlaylist;

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

    }

    private void initToolBarParalax() {
        mCurrentPlaylist = PlaylistModel.getPlaylistById(mCurrentPlaylistId);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            if (mCurrentPlaylist!=null){

                getSupportActionBar().setTitle(mCurrentPlaylist.getTitle());
            }else{
                getSupportActionBar().setTitle("Không tìm thấy Playlist");
            }
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    private void initRecyclerViewListSong() {

        mListSong = PlaylistSongModel.getAllSongFromPlaylistId(mCurrentPlaylistId);
        Log.d(TAG, "initRecyclerViewListSong: SIZE PLAYLIST SONG " + mListSong.size());
        mSongPlaylistAdapter = new SongPlaylistAdapter(this, mListSong);
        mRecylerViewListSong.setLayoutManager(new LinearLayoutManager(this));
        mRecylerViewListSong.setHasFixedSize(true);
        mRecylerViewListSong.setAdapter(mSongPlaylistAdapter);
    }
}
