package com.example.musicforlife.artist;
import android.app.Activity;
import android.app.FragmentTransaction;
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

public class ArtistSongsActivity extends Activity {

    ImageButton ImgBtnBack;
    RelativeLayout RLHeroImage;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVSongcount;
    ArtistModel artistModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);

        Intent intent = getIntent();
        artistModel = (ArtistModel)intent.getSerializableExtra("infoArtist");
        InitControl();
        BindData();

        //Init Fragment
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentArtistSong fragmentArtistSong = new FragmentArtistSong();
        Bundle bundle = new Bundle();
        bundle.putString("artistQuery",artistModel.getName());
        fragmentArtistSong.setArguments(bundle);
        fragmentTransaction.add(R.id.artistSongFragmentLayout,fragmentArtistSong);
        fragmentTransaction.commit();
        //Init Fragment

        //set button Back
        ImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void InitControl(){
        ImgBtnBack = (ImageButton)findViewById(R.id.artistSongBtnBack);
        RLHeroImage = (RelativeLayout)findViewById(R.id.artistSongHeroImage);
        ImgProfile = (ImageView)findViewById(R.id.artistSongImgProfile);
        TVNameArtist = (TextView)findViewById(R.id.artistSongNameArtist);
        TVSongcount = (TextView)findViewById(R.id.artistSongcount);
    }
    private void BindData(){
        if(artistModel != null){
            TVNameArtist.setText(artistModel.getName());
            TVSongcount.setText(artistModel.getSongCount() + " Bài hát");

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(artistModel.getPath());
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
