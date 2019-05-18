package com.example.musicforlife.play;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.TimerSongService;
import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.playlist.PlaylistDialogAdapter;
import com.example.musicforlife.playlist.PlaylistModel;
import com.example.musicforlife.playlist.PlaylistSongModel;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class FragmentDialogTimerSong extends DialogFragment {
    private PlayActivity mPlayActivity;
    private SeekBar mSbTimer;
    private Switch mSwtTimer;
    private TextView mTxtTimeStart;
    private TimerSongService mTimerSongService;
    private final int MAX_TIMER = 120;
    private final int MIN_TIMER = 0;
    private final int STEP_TIMER = 1;
    private static final String TAG = "FragmentDialogTimerSong";

    @SuppressLint("ValidFragment")
    public FragmentDialogTimerSong(PlayActivity playActivity) {
        mPlayActivity = playActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_timer_song, null);
        mSbTimer = view.findViewById(R.id.sbTimer);
        mSwtTimer = view.findViewById(R.id.swtTimer);
        mTxtTimeStart = view.findViewById(R.id.txtStartTimer);
        mSbTimer.setMin(MIN_TIMER);
        mSbTimer.setProgress(0);
        mSbTimer.setMax(MAX_TIMER);
        mTimerSongService = new TimerSongService();
//        mSbTimer.incrementProgressBy(STEP_TIMER);

        mSbTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mTxtTimeStart.setText(getTimesFromProgress(progress) + " phút");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setView(view);
        return builder.create();
    }

    private int getTimesFromProgress(int progress) {
        progress = progress / STEP_TIMER;
        progress = progress * STEP_TIMER;
        return progress;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mTimerSongService.isRuning()) {
            mTxtTimeStart.setText((mTimerSongService.getmCurrentTime() / 60000) + " phút");
            mSbTimer.setProgress((int) (mTimerSongService.getTimes() / 60000));
            mSwtTimer.setChecked(true);
        }


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        boolean isStartTimer = mSwtTimer.isChecked();
        int times = getTimesFromProgress(mSbTimer.getProgress());
        Intent intentStart = new Intent(mPlayActivity, TimerSongService.class);
        intentStart.setAction(TimerSongService.ACTION_START_TIMER);
        Bundle bundle = new Bundle();
        bundle.putInt("Times", times);
        intentStart.putExtras(bundle);
        if (isStartTimer) {

            mPlayActivity.startTimerService(intentStart);
        } else {
            mPlayActivity.stopTimerService(intentStart);
        }

        Log.d(TAG, "onDetach: ");
    }
}
