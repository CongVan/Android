package com.example.musicforlife.play;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicforlife.PlayActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

public class FragmentPlaying extends Fragment implements FragmentPlayInterface {

    LinearLayout mLayoutFragmentPlaying;
    ViewGroup mViewGroupMain;
    ImageButton mImageButtonPlaySong;
    Context mContext;
    PlayActivity mPlayActivity;

    public static final String SENDER = "FRAGMENT_PLAYING";

    public FragmentPlaying() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mContext == null) {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mViewGroupMain = (ViewGroup) inflater.inflate(R.layout.fragment_playing, container, false);
        return mViewGroupMain;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageButtonPlaySong = mViewGroupMain.findViewById(R.id.btnPlaySong);
        mImageButtonPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "PLAY CLICK", Toast.LENGTH_SHORT).show();

                if (PlayCenter.isPlaying()) {// song is playing then stop
                    mPlayActivity.controlSong(SENDER, null, PlayCenter.ACTION_PAUSE);
                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));

                } else { //resume
                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
                    mPlayActivity.controlSong(SENDER, null, PlayCenter.ACTION_RESUME);
                }

            }
        });
    }


    @Override
    public void updateControlPlaying() {
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
    }
}
