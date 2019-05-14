package com.example.musicforlife.play;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentPlaying extends Fragment implements FragmentPlayInterface, View.OnClickListener {

    private TableRow mTableLayoutControlPlaying;
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
    private ImageView mImagePlaying;
    private ImageView mImageDvd;
    private ImageView mImageBgPlaying;
    private CardView mCvImagePlaying;
    private Animation mAnimationPlay;
    private PlayService mPlayService;

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
//        Bundle bundle = getArguments();
//        mSongPlaying = (SongModel) bundle.getSerializable("PLAY_SONG");
        mSongPlaying = PlayService.getCurrentSongPlaying();
        mPlayService = PlayService.newInstance();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mViewGroupMain = (ViewGroup) inflater.inflate(R.layout.fragment_playing, container, false);
        mPlayActivity.updateToolbarTitle();
        return mViewGroupMain;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTableLayoutControlPlaying = mViewGroupMain.findViewById(R.id.layoutControlPlaying);
        mImageButtonPlaySong = mViewGroupMain.findViewById(R.id.btnPlaySong);
        mImageButtonPrevSong = mViewGroupMain.findViewById(R.id.btnPrevSong);
        mImageButtonNextSong = mViewGroupMain.findViewById(R.id.btnNextSong);
        mImageButtonLoopType = mViewGroupMain.findViewById(R.id.btnLoopType);
        mImagePlaying = mViewGroupMain.findViewById(R.id.imgPlaying);
//        mImageDvd=mViewGroupMain.findViewById(R.id.imgDvd);
        mCvImagePlaying=mViewGroupMain.findViewById(R.id.cvImagePlaying);
        mImageBgPlaying = mViewGroupMain.findViewById(R.id.imgBgPlaying);

        mTxtTitleSongPlaying = mViewGroupMain.findViewById(R.id.txtTitleSongPlaying);
        mTxtArtistSongPlaying = mViewGroupMain.findViewById(R.id.txtArtistSongPlaying);
        mTxtDurationSongPlaying = mViewGroupMain.findViewById(R.id.txtDurationSongPlaying);
        mSebDurationSongPlaying = mViewGroupMain.findViewById(R.id.sebDurationSongPlaying);
        mTxtCurrentDuration = mViewGroupMain.findViewById(R.id.txtCurrentDuration);

        mAnimationPlay=AnimationUtils.loadAnimation(mContext,R.anim.playing_image);
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
                        setButtonPause();
                        //mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));

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
        setResourceImagePlaying();
        updateControlPlaying(mSongPlaying);
    }

    private void setResourceImagePlaying() {
        if (mSongPlaying != null) {
            Bitmap bitmapPlaying = ImageHelper.getBitmapFromPath(mSongPlaying.getPath());
//        Bitmap bitmapBgPlaying = ImageHelper.getBitmapFromPath(mSongPlaying.getPath(), R.drawable.background_1);
            if (bitmapPlaying == null) {
                bitmapPlaying = ImageHelper.drawableToBitmap(R.mipmap.dvd);
                mImagePlaying.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                mImagePlaying.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            mImagePlaying.setImageBitmap(bitmapPlaying);
            if (PlayService.isPlaying()){
                mCvImagePlaying.startAnimation(mAnimationPlay);
            }else{
                mCvImagePlaying.clearAnimation();
            }

//            mImageDvd.startAnimation(animation);
        }


//        Bitmap bitmapBlurBgPlaying = ImageHelper.blurBitmap(bitmapBgPlaying, 1.0f, 100);
//        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgPlaying.getWidth(), bitmapBgPlaying.getHeight(), Color.argb(100, 0, 0, 0));
//        mImageBgPlaying.setImageBitmap(bitmapBlurBgPlaying);
//        if (ImageHelper.isDarkBitmap(bitmapBgPlaying)) {
//            mTableLayoutControlPlaying.setBackgroundColor(Color.argb(40, 255, 255, 255));
//        } else {
//            mTableLayoutControlPlaying.setBackgroundColor(Color.TRANSPARENT);
//        }
//        Log.d(TAG, "setResourceImagePlaying: ISDARK" + ImageHelper.isDarkBitmap(bitmapPlaying));
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
//        if (mSongPlaying != null && PlayService.getCurrentSongPlaying() != null) {
//            if (mSongPlaying.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
//                updateControlPlaying(mSongPlaying);
//            }
//        }
        updateControlPlaying(mSongPlaying);
        updateButtonPlay();
    }

    @Override
    public void updateListPlaying() {

    }

    @Override
    public void refreshListPlaying() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {
        Log.d(TAG, "updateControlPlaying: " + songModel.getDuration());
        mSongPlaying = songModel;
        mTxtTitleSongPlaying.setText(mSongPlaying.getTitle());
        mTxtArtistSongPlaying.setText(mSongPlaying.getArtist());
        mTxtDurationSongPlaying.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        mSebDurationSongPlaying.setMax(mSongPlaying.getDuration().intValue() / 1000);
        setResourceImagePlaying();
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
            setButtonPause();
//            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));
        } else {
            setButtonPlay();
//            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
        }
    }

    private void setButtonPlay() {
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_circle_outline_black_64dp));

    }

    private void setButtonPause() {
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaySong:
                Toast.makeText(mContext, "PLAY CLICK", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: DURATION PLAY" + PlayService.getCurrentDuration());
                if (PlayService.isPlaying()) {// song is playing then stop
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);
                    mCvImagePlaying.clearAnimation();
                    setButtonPlay();
//                    mPlayService.pause();
                } else if (PlayService.isPause()) { //resume
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                    mCvImagePlaying.startAnimation(mAnimationPlay);
                    setButtonPause();
//                    mPlayService.resurme();
//                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));
                } else {
                    mPlayActivity.controlSong(SENDER, PlayService.getCurrentSongPlaying(), PlayService.ACTION_PLAY);
                    mCvImagePlaying.startAnimation(mAnimationPlay);
                    setButtonPause();
//                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));
                }
                break;
            case R.id.btnPrevSong:
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PREV);
//                mPlayService.prev(PlayService.ACTION_FROM_USER);
                break;
            case R.id.btnNextSong:
//                mPlayService.next(PlayService.ACTION_FROM_USER);
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
