package com.example.musicforlife;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;

public class NotifyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NotifyBroadcastReceiver";
    public static final String ACTION_PLAY_NOTIFY = "Play_Button_Notification";
    public static final String ACTION_NEXT_NOTIFY = "Next_Button_Notification";
    public static final String ACTION_PREV_NOTIFY = "Prev_Button_Notification";
    private final PlayService mPlayService = PlayService.newInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MainActivity mainActivity = MainActivity.getMainActivity();
        PlayActivity playActivity = PlayActivity.getActivity();
        switch (action) {
            case ACTION_PLAY_NOTIFY:
                if (PlayService.isPlaying()) {
                    mPlayService.pause();
                    if (mainActivity != null) {
                        mainActivity.refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                    }

                } else if (PlayService.isPause()) {
                    mPlayService.resurme();
                    if (mainActivity != null) {
                        mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                    }

                } else {
                    mPlayService.play(PlayService.getCurrentSongPlaying());
                    if (mainActivity != null) {
                        mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                    }
                }
                break;

            case ACTION_NEXT_NOTIFY:
                mPlayService.next(PlayService.ACTION_FROM_SYSTEM);
                if (mainActivity != null) {
                    mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                }

                break;
            case ACTION_PREV_NOTIFY:
                mPlayService.prev(PlayService.ACTION_FROM_SYSTEM);
                if (mainActivity != null) {
                    mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                }

                break;
            default:
                break;
        }
        if (playActivity != null) {
            playActivity.refreshListPlaying();
            playActivity.updateButtonPlay("Notify");
        }
    }
}
