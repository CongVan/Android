package com.example.musicforlife.album;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicforlife.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.artist.ArtistSongsActivity;
import com.example.musicforlife.artist.FragmentArtistSong;
import com.example.musicforlife.utilitys.Utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AlbumSongsActivity extends Activity {
    ImageButton ImgBtnBack;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVNameAlbum;
    TextView TVSongcount;
    AlbumModel albumModel;
    LinearLayout layoutContentAlbumtSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent = getIntent();
        albumModel = (AlbumModel)intent.getSerializableExtra("infoAlbum");
        InitControl();
        BindData();

        //Init Fragment
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentAlbumSong fragmentAlbumSong = new FragmentAlbumSong();
        Bundle bundle = new Bundle();
        bundle.putString("albumQuery", albumModel.getTitle());
        fragmentAlbumSong.setArguments(bundle);
        fragmentTransaction.add(R.id.albumSongFragmentLayout, fragmentAlbumSong);
        fragmentTransaction.commit();
        //Init Fragment

        ImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupLayoutTransparent();
    }

    private void InitControl(){
        ImgBtnBack = (ImageButton)findViewById(R.id.albumSongBtnBack);
        ImgProfile = (ImageView)findViewById(R.id.albumSongImgProfile);
        TVNameArtist = (TextView)findViewById(R.id.albumSongArtistName);
        TVNameAlbum = (TextView)findViewById(R.id.albumSongName);
        TVSongcount = (TextView)findViewById(R.id.albumSongcount);
        layoutContentAlbumtSong = (LinearLayout)findViewById(R.id.layoutContentAlbumSong);
    }

    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(AlbumSongsActivity.this);
        Bitmap bitmapBg = ImageHelper.getBitmapFromPath(albumModel.getPath(), R.drawable.highcompress_background_test);
        Bitmap bitmapBgBlur = ImageHelper.blurBitmap(bitmapBg, 1.0f, 140);
        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgBlur.getWidth(), bitmapBgBlur.getHeight(), Color.argb(80, 255, 255, 255));
        Bitmap bitmapBgOverlay = ImageHelper.overlayBitmapToCenter(bitmapBgBlur, bitmapOverlay);
        layoutContentAlbumtSong.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
        layoutContentAlbumtSong.setBackground(ImageHelper.getMainBackgroundDrawableFromBitmap(bitmapBgOverlay));
    }

    private void BindData(){
        if(albumModel != null){
            TVNameAlbum.setText(albumModel.getTitle());
            TVNameArtist.setText(albumModel.getArtist());
            TVSongcount.setText(albumModel.getNumberOfSongs() + " Bài hát");

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(albumModel.getPath());
            if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                InputStream inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                mediaMetadataRetriever.release();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImgProfile.setBackground(new BitmapDrawable(getResources(), bitmap));
                BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
            }
        }
    }
}
