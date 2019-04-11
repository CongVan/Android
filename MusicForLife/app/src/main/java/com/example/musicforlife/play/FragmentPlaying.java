package com.example.musicforlife.play;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.PlayActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

public class FragmentPlaying extends Fragment implements FragmentPlayInterface {

    private LinearLayout mLayoutFragmentPlaying;
    private ViewGroup mViewGroupMain;
    private ImageButton mImageButtonPlaySong;
    private Context mContext;
    private PlayActivity mPlayActivity;
    private TextView mTxtTitleSongPlaying;
    private TextView mTxtArtistSongPlaying;
    private TextView mTxtDurationSongPlaying;
    private SeekBar mSebDurationSongPlaying;
    private TextView mTxtCurrentDuration;
    private ImageButton mImageButtonPrevSong;
    private ImageButton mImageButtonNextSong;

    private SongModel mSongPlaying;

    public static final String SENDER = "FRAGMENT_PLAYING";
    private static final String TAG = "FragmentPlaying";
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
        mImageButtonPrevSong=mViewGroupMain.findViewById(R.id.btnPrevSong);
        mImageButtonNextSong=mViewGroupMain.findViewById(R.id.btnNextSong);
        mTxtTitleSongPlaying = mViewGroupMain.findViewById(R.id.txtTitleSongPlaying);
        mTxtArtistSongPlaying = mViewGroupMain.findViewById(R.id.txtArtistSongPlaying);
        mTxtDurationSongPlaying = mViewGroupMain.findViewById(R.id.txtDurationSongPlaying);
        mSebDurationSongPlaying = mViewGroupMain.findViewById(R.id.sebDurationSongPlaying);
        mTxtCurrentDuration = mViewGroupMain.findViewById(R.id.txtCurrentDuration);


        mImageButtonPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "PLAY CLICK", Toast.LENGTH_SHORT).show();

                if (PlayService.isPlaying()) {// song is playing then stop
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);

                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
                } else { //resume
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));


                }

            }
        });

        mImageButtonPrevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayActivity.controlSong(SENDER,null, PlayService.ACTION_PREV);
            }
        });

        mImageButtonNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayActivity.controlSong(SENDER,null, PlayService.ACTION_NEXT);
            }
        });

        mSebDurationSongPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayActivity.updateDuration(SENDER, progress);
                    if(!PlayService.isPlaying()){
                        mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));

                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekbar(PlayService.getCurrentDuration());
        Log.d(TAG, "onResume: "+ PlayService.getCurrentDuration());
//        mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {
        mSongPlaying = songModel;
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));
        mTxtTitleSongPlaying.setText(mSongPlaying.getTitle());
        mTxtArtistSongPlaying.setText(mSongPlaying.getArtist());
        mTxtDurationSongPlaying.setText(mSongPlaying.formateMilliSeccond(songModel.getDuration()));
        mSebDurationSongPlaying.setMax(mSongPlaying.getDuration().intValue() / 1000);
        //

    }

    @Override
    public void updateSeekbar(int currentDuration) {
        mSebDurationSongPlaying.setProgress(currentDuration / 1000);
        mTxtCurrentDuration.setText(SongModel.formateMilliSeccond(currentDuration));
    }
}
