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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class FragmentPlaying extends Fragment implements FragmentPlayInterface, View.OnClickListener {

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
    private ImageButton mImageButtonLoopType;

    private SongModel mSongPlaying;

    public static final String SENDER = "FRAGMENT_PLAYING";
    private static final String TAG = "FragmentPlaying";
    private static final ArrayList<Integer> arrLoopTypeValue = new ArrayList<>(Arrays.asList(
            PlayService.NONE_LOOP,
            PlayService.ALL_LOOP,
            PlayService.ONE_LOOP));
    private static final ArrayList<Integer> arrLoopTypeImage = new ArrayList<>(Arrays.asList(
            R.drawable.ic_repeat_none_black_32dp,
            R.drawable.ic_repeat_black_32dp,
            R.drawable.ic_repeat_one_black_32dp));


    public FragmentPlaying() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mContext == null) {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();

        }
        Bundle bundle = getArguments();
        mSongPlaying = (SongModel) bundle.getSerializable("PLAY_SONG");
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
        mImageButtonPrevSong = mViewGroupMain.findViewById(R.id.btnPrevSong);
        mImageButtonNextSong = mViewGroupMain.findViewById(R.id.btnNextSong);
        mImageButtonLoopType = mViewGroupMain.findViewById(R.id.btnLoopType);

        mTxtTitleSongPlaying = mViewGroupMain.findViewById(R.id.txtTitleSongPlaying);
        mTxtArtistSongPlaying = mViewGroupMain.findViewById(R.id.txtArtistSongPlaying);
        mTxtDurationSongPlaying = mViewGroupMain.findViewById(R.id.txtDurationSongPlaying);
        mSebDurationSongPlaying = mViewGroupMain.findViewById(R.id.sebDurationSongPlaying);
        mTxtCurrentDuration = mViewGroupMain.findViewById(R.id.txtCurrentDuration);

        mImageButtonPlaySong.setOnClickListener(this);
        mImageButtonPrevSong.setOnClickListener(this);
        mImageButtonNextSong.setOnClickListener(this);
        mImageButtonLoopType.setOnClickListener(this);
        mImageButtonLoopType.setImageDrawable(
                mPlayActivity.getDrawable(
                        arrLoopTypeImage.get(
                                arrLoopTypeValue.indexOf(PlayService.getLoopType()))));


        mSebDurationSongPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayActivity.updateDuration(SENDER, progress);
                    if (!PlayService.isPlaying()) {
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (PlayService.isPlaying() && mSongPlaying.getSongId() != PlayService.getCurrentSongPlaying().getSongId()) {
            Log.d(TAG, "onResume: SERVICE " + PlayService.getCurrentSongPlaying().getTitle() + " PLAY " + mSongPlaying.getTitle());
//            mSongPlaying=PlayService.getCurrentSongPlaying();
            mPlayActivity.controlSong(SENDER, mSongPlaying, PlayService.ACTION_PLAY);
            updateControlPlaying(mSongPlaying);
//            mPlayActivity.updateControlPlaying(SENDER, mSongPlaying);
        }

        updateSeekbar(PlayService.getCurrentDuration());
        Log.d(TAG, "onResume: " + PlayService.getCurrentDuration());
//        mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
        if (mSongPlaying != null && PlayService.getCurrentSongPlaying() != null) {
            if (mSongPlaying.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
                updateControlPlaying(mSongPlaying);

            }
        }

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {
        mSongPlaying = songModel;
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

    @Override
    public void updateButtonPlay() {
        Log.d(TAG, "updateButtonPlay: " + PlayService.isPlaying());
        if (PlayService.isPlaying()) {
            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));
        } else {
            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaySong:
                Toast.makeText(mContext, "PLAY CLICK", Toast.LENGTH_SHORT).show();
                if (PlayService.isPlaying()) {// song is playing then stop
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);
                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
                } else { //resume
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));
                }
                break;
            case R.id.btnPrevSong:
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PREV);
                break;
            case R.id.btnNextSong:
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_NEXT);
                break;
            case R.id.btnLoopType:
                int currentLoopType = PlayService.getLoopType();
                int indexLoopType = arrLoopTypeValue.indexOf(currentLoopType);
//                Log.d(TAG, "onClick: TYPE LOOP " + currentLoopType + "__" + indexLoopType);
                indexLoopType = indexLoopType >= arrLoopTypeValue.size() - 1 ? 0 : indexLoopType + 1;
                currentLoopType = arrLoopTypeValue.get(indexLoopType);
                PlayService.setLoopType(currentLoopType);
//                Log.d(TAG, "onClick: TYPE LOOP " + currentLoopType + "__" + indexLoopType);
                mImageButtonLoopType.setImageDrawable(
                        mPlayActivity.getDrawable(
                                arrLoopTypeImage.get(indexLoopType)));

                break;
            default:
                break;
        }
    }
}
