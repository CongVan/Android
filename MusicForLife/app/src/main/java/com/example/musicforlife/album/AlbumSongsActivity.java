package com.example.musicforlife.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicforlife.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AlbumSongsActivity extends Activity {
    ImageButton ImgBtnBack;
    RelativeLayout RLHeroImage;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVSongcount;
    AlbumModel albumModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent = getIntent();
        albumModel = (AlbumModel)intent.getSerializableExtra("infoAlbum");
        InitControl();
        BindData();

        ImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitControl(){
        ImgBtnBack = (ImageButton)findViewById(R.id.albumSongBtnBack);
        RLHeroImage = (RelativeLayout)findViewById(R.id.albumSongHeroImage);
        ImgProfile = (ImageView)findViewById(R.id.albumSongImgProfile);
        TVNameArtist = (TextView)findViewById(R.id.albumSongName);
        TVSongcount = (TextView)findViewById(R.id.albumSongcount);
    }

    private void BindData(){
        if(albumModel != null){
            TVNameArtist.setText(albumModel.getTitle());
            TVSongcount.setText(albumModel.getNumberOfSongs() + " Bài hát");

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(albumModel.getPath());
            if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                InputStream inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                mediaMetadataRetriever.release();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImgProfile.setBackground(new BitmapDrawable(getResources(), bitmap));
                BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                RLHeroImage.setBackground(d);
            }
        }
    }
}
