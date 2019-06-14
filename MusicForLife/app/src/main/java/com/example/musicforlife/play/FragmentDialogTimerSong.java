package com.example.musicforlife.play;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.TimerReceiver;
import com.example.musicforlife.TimerSongService;
import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.playlist.PlaylistDialogAdapter;
import com.example.musicforlife.playlist.PlaylistModel;
import com.example.musicforlife.playlist.PlaylistSongModel;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class FragmentDialogTimerSong extends DialogFragment {
    private PlayActivity mPlayActivity;
    private SeekBar mSbTimer;
    private Switch mSwtTimer;
    private TextView mTxtTimeStart;
    private TextView mTxtTimes;
    private TimerSongService mTimerSongService;
    private final int MAX_TIMER = 120;
    private final int MIN_TIMER = 0;
    private final int STEP_TIMER = 10;
    private long mTimes;
    private long mCurrentTimes;
    private static final String TAG = "FragmentDialogTimerSong";
    public static final String ACTION_DIAGLOG_TIMER_RECEIVER = "ACTION_DIAGLOG_TIMER_RECEIVER";

    @SuppressLint("ValidFragment")
    public FragmentDialogTimerSong(PlayActivity playActivity, long times, long currentTimes) {
        mPlayActivity = playActivity;
        mTimes = times;
        mCurrentTimes = currentTimes;
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
        mTxtTimes = view.findViewById(R.id.txtTimes);
        mSbTimer.setMin(MIN_TIMER);
        mSbTimer.setProgress(0);
        mSbTimer.setMax(MAX_TIMER);
        mTxtTimes.setVisibility(View.GONE);
        mTimerSongService = TimerSongService.newIntance();


        Log.d(TAG, "onCreateDialog: IS RUNNING " + TimerReceiver.isRunning);
        if (mTimes > 0) {
            Log.d(TAG, "onCreateDialog: CURRENT =" + mCurrentTimes);
//            int leftTime=(int) (mTimes - mCurrentTimes) / 60000;
//            mSbTimer.setProgress(leftTime);
            mSwtTimer.setChecked(true);
//            mTxtTimeStart.setText(leftTime+" phút");
            String startLabelTimer = getColoredSpanned("Sau ", "#e0e0e0");
            String labelTimer = getColoredSpanned((mTimes / 60000) + " phút ", "#FFB533");
            String endLabelTimer = getColoredSpanned("ứng dụng sẽ tự động tắt nhạc", "#e0e0e0");
            mTxtTimes.setText(Html.fromHtml(startLabelTimer + labelTimer + endLabelTimer));
            mTxtTimes.setVisibility(View.VISIBLE);

        }

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

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
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
        Intent intentStart = new Intent(mPlayActivity.getApplicationContext(), TimerSongService.class);
        intentStart.setAction(TimerSongService.ACTION_START_TIMER);
        Bundle bundle = new Bundle();
        bundle.putInt("Times", times);
        intentStart.putExtras(bundle);
        if (isStartTimer) {
            if (mTimes == 0 || times > 0) {
                mPlayActivity.startTimerService(intentStart);
            }

        } else {
            mPlayActivity.stopTimerService(intentStart);
        }

//        Log.d(TAG, "onDetach: ");
    }

}
