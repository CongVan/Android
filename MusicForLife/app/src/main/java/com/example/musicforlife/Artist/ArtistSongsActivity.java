package com.example.musicforlife.Artist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicforlife.R;

import org.w3c.dom.Text;

public class ArtistSongsActivity extends AppCompatActivity {

    ImageButton ImgBtnBack;
    RelativeLayout RLHeroImage;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVSongcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);

        InitControl();

    }

    private void InitControl(){
        ImgBtnBack = (ImageButton)findViewById(R.id.artistSongBtnBack);
        RLHeroImage = (RelativeLayout)findViewById(R.id.artistSongHeroImage);
        ImgProfile = (ImageView)findViewById(R.id.artistSongImgProfile);
        TVNameArtist = (TextView)findViewById(R.id.artistSongNameArtist);
        TVSongcount = (TextView)findViewById(R.id.artistSongcount);
    }
}