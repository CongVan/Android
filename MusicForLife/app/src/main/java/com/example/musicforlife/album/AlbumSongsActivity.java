package com.example.musicforlife.album;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.utilitys.Utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AlbumSongsActivity extends AppCompatActivity {
    ImageButton ImgBtnBack;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVNameAlbum;
    TextView TVSongcount;
    AlbumModel albumModel;
    CoordinatorLayout layoutContentAlbumtSong;
    Toolbar mToolbarAlbumSong;
    RecyclerView RVAlbumsong;
    PlayService mPlayService;
    AppBarLayout mAppbarLayoutalbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent = getIntent();
        albumModel = (AlbumModel) intent.getSerializableExtra("infoAlbum");
        InitControl();
        BindData();
        setupLayoutTransparent();
    }

    private void InitControl() {
        ImgProfile = (ImageView) findViewById(R.id.albumSongImgProfile);
        TVNameArtist = (TextView) findViewById(R.id.albumSongArtistName);
        TVNameAlbum = (TextView) findViewById(R.id.albumSongName);
        TVSongcount = (TextView) findViewById(R.id.albumSongcount);
        layoutContentAlbumtSong = (CoordinatorLayout) findViewById(R.id.layoutContentAlbumSong);
        mToolbarAlbumSong = findViewById(R.id.albumhtab_toolbar);
        RVAlbumsong = (RecyclerView)findViewById(R.id.rcvalbumSong);
        mAppbarLayoutalbum = (AppBarLayout)findViewById(R.id.albumhtab_appbar);
        mPlayService = PlayService.newInstance();
    }

    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(AlbumSongsActivity.this);
        Bitmap bitmapBg = ImageHelper.getBitmapFromPath(albumModel.getPath(), R.drawable.highcompress_background_test);
        Bitmap bitmapBgBlur = ImageHelper.blurBitmap(bitmapBg, 1.0f, 30);
        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgBlur.getWidth(), bitmapBgBlur.getHeight(), getResources().getColor(R.color.colorBgPrimaryOverlay));
        Bitmap bitmapBgOverlay = ImageHelper.overlayBitmapToCenter(bitmapBgBlur, bitmapOverlay);
        layoutContentAlbumtSong.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
        layoutContentAlbumtSong.setBackground(ImageHelper.getMainBackgroundDrawableFromBitmap(bitmapBgOverlay));

        setSupportActionBar(mToolbarAlbumSong);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbarAlbumSong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void BindData() {
        if (albumModel != null) {
            TVNameAlbum.setText(albumModel.getTitle());
            TVNameArtist.setText(albumModel.getArtist());
            TVSongcount.setText(albumModel.getNumberOfSongs() + " Bài hát");
            ImgProfile.setImageBitmap(ImageHelper.getBitmapFromPath(albumModel.getPath(), R.mipmap.microphone_128));
            final ArrayList<SongModel> albumSongsList = AlbumProvider.getALbumSongs(this, albumModel.getTitle());
            AlbumSongAdapter albumSongAdapter = new AlbumSongAdapter(this, albumSongsList);
            RVAlbumsong.setLayoutManager(new LinearLayoutManager(this));
            RVAlbumsong.setHasFixedSize(true);
            RVAlbumsong.setAdapter(albumSongAdapter);
            RVAlbumsong.addOnItemTouchListener(new RecyclerItemClickListener(this, RVAlbumsong, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SongModel songModel = albumSongsList.get(position);
                    MainActivity _mainActivity = MainActivity.getMainActivity();
                    mPlayService.play(songModel);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mPlayService.initListPlaying(albumSongsList);
                        }
                    }).start();
                    _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            mAppbarLayoutalbum.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                        getSupportActionBar().setTitle(albumModel.getTitle());

                    } else {
                        getSupportActionBar().setTitle(" ");
                    }
                }
            });
        }
    }
}
