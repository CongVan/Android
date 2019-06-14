package com.example.musicforlife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicforlife.play.FragmentDialogTimerSong;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;

public class TimerReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerReceiver";
    public static long times;
    public static boolean isRunning;
    public static long currentTimes;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Timer Song " + intent.getAction());
        if (intent.getAction() == TimerSongService.ACTION_START_TIMER) {
            isRunning = true;
        }
        if (intent.getAction() == TimerSongService.ACTION_FINISH_TIMER) {
            isRunning = false;
            if (PlayService.isPlaying()) {
                PlayService.newInstance().pause();
                if (MainActivity.getMainActivity() != null) {
                    MainActivity.getMainActivity().refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                }
                if (PlayActivity.getActivity() != null) {
                    PlayActivity.getActivity().updateButtonPlay(TAG);
                }

            }

        }
        if (intent.getAction() == TimerSongService.ACTION_TICK_TIMER) {
            long times = intent.getLongExtra("times", 0);
            long currentTimes = intent.getLongExtra("currentTimes", 0);
            times = times;
            currentTimes = currentTimes;
            Log.d(TAG, "onReceive: TICK times=" + times + ", current=" + currentTimes);
            Intent i = new Intent(FragmentDialogTimerSong.ACTION_DIAGLOG_TIMER_RECEIVER);
            i.putExtra("times", times);
            i.putExtra("currentTimes", currentTimes);
            context.sendBroadcast(i);
        }
    }
}