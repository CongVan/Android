package com.example.musicforlife.artist;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.minimizeSong.MinimizeSongFragment;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.playlist.PlaylistSongActivity;
import com.example.musicforlife.recent.RecentModel;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.utilitys.Utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ArtistSongsActivity extends AppCompatActivity implements MinimizeSongFragment.OnFragmentInteractionListener {
    RecyclerView RVListArtist;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVSongcount;
    ArtistModel artistModel;
    Toolbar mToolbarArtistSong;
    CoordinatorLayout layoutContentArtistSong;
    PlayService mPlayService;
    AppBarLayout mAppbarLayoutArtist;
    private MinimizeSongFragment mMinimizeSongFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);

        Intent intent = getIntent();
        artistModel = (ArtistModel) intent.getSerializableExtra("infoArtist");
        InitControl();
        BindData();
        setupLayoutTransparent();
        initMimimizeSong();
    }

    private void InitControl() {
        RVListArtist = (RecyclerView) findViewById(R.id.rcvartistSong);
        ImgProfile = (ImageView) findViewById(R.id.artistSongImgProfile);
        TVNameArtist = (TextView) findViewById(R.id.artistSongNameArtist);
        TVSongcount = (TextView) findViewById(R.id.artistSongcount);
        layoutContentArtistSong = (CoordinatorLayout) findViewById(R.id.layoutContentArtistSong);
        mToolbarArtistSong = (Toolbar) findViewById(R.id.artisthtab_toolbar);
        mAppbarLayoutArtist = (AppBarLayout) findViewById(R.id.artisthtab_appbar);
        mPlayService = PlayService.newInstance();
    }

    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(ArtistSongsActivity.this);
        Bitmap bitmapBg = ImageHelper.getBitmapFromPath(artistModel.getPath(), R.drawable.gradient_bg);
        Bitmap bitmapBgBlur = ImageHelper.blurBitmap(bitmapBg, 1.0f, 30);
        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgBlur.getWidth(), bitmapBgBlur.getHeight(), getResources().getColor(R.color.colorBgPrimary));
        Bitmap bitmapBgOverlay = ImageHelper.overlayBitmapToCenter(bitmapBgBlur, bitmapOverlay);
        layoutContentArtistSong.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
        layoutContentArtistSong.setBackground(ImageHelper.getMainBackgroundDrawableFromBitmap(bitmapBgOverlay));

        setSupportActionBar(mToolbarArtistSong);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbarArtistSong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void BindData() {
        if (artistModel != null) {
            TVNameArtist.setText(artistModel.getName());
            TVSongcount.setText(artistModel.getSongCount() + " Bài hát");
            ImgProfile.setImageBitmap(ImageHelper.getBitmapFromPath(artistModel.getPath(), R.mipmap.microphone_128));
            final ArrayList<SongModel> artistSongsList = ArtistProvider.getArtistSongs(this, artistModel.getName());
            ArtistSongsAdapter artistSongsAdapter = new ArtistSongsAdapter(this, artistSongsList);
            RVListArtist.setLayoutManager(new LinearLayoutManager(this));
            RVListArtist.setHasFixedSize(true);
            RVListArtist.setAdapter(artistSongsAdapter);
            RVListArtist.addOnItemTouchListener(new RecyclerItemClickListener(this, RVListArtist, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SongModel songModel = artistSongsList.get(position);
                    MainActivity _mainActivity = MainActivity.getMainActivity();
                    mPlayService.play(songModel);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mPlayService.initListPlaying(artistSongsList);
                        }
                    }).start();
                    _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RecentModel.addToRecent(artistModel.getName(), RecentModel.TYPE_ARTIST);
                        }
                    }).start();
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            mAppbarLayoutArtist.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                        getSupportActionBar().setTitle(artistModel.getName());

                    } else {
                        getSupportActionBar().setTitle(" ");

                    }
                }
            });
        }
    }

    private void initMimimizeSong() {
        mMinimizeSongFragment = MinimizeSongFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.frgMinimizeSong, mMinimizeSongFragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentRefreshNotification(int action) {
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().refreshNotificationPlaying(action);
        }
    }

    @Override
    public void onFragmentShowPlayActivity() {
        Intent mIntentPlayActivity = new Intent(ArtistSongsActivity.this, PlayActivity.class);
        mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(mIntentPlayActivity);
    }

    @Override
    public void onFragmentLoaded(int heightLayout) {

    }
}
