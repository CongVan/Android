package com.example.musicforlife;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.musicforlife.listsong.SongModel;
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
        Intent intentPlay = new Intent(context, PlayService.class);
        Bundle bundlePlay = new Bundle();
        switch (action) {
            case ACTION_PLAY_NOTIFY:
                if (PlayService.isPlaying()) {
//                    intentPlay.setAction(String.valueOf(PlayService.ACTION_PAUSE));
                    mPlayService.pause();
                    if (mainActivity != null) {
                        mainActivity.refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                    }

                } else if (PlayService.isPause()) {
//                    intentPlay.setAction(String.valueOf(PlayService.ACTION_RESUME));
                    mPlayService.resurme();

                    if (mainActivity != null) {
                        mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                    }

                } else {
                    if (PlayService.getCurrentSongPlaying() != null) {
//                        intentPlay.setAction(String.valueOf(PlayService.ACTION_PLAY));

//                        bundlePlay.putSerializable(SongModel.class.toString(), PlayService.getCurrentSongPlaying());

                        mPlayService.play(PlayService.getCurrentSongPlaying());
                        if (mainActivity != null) {
                            mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                        }
                    }

                }
                break;

            case ACTION_NEXT_NOTIFY:
//                bundlePlay.putInt("actionFrom", PlayService.ACTION_FROM_USER);
//                intentPlay.setAction(String.valueOf(PlayService.ACTION_NEXT));
                Log.d(TAG, "onReceive: ");
                mPlayService.next(PlayService.ACTION_FROM_USER);
                if (mainActivity != null) {
                    mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                }

                break;
            case ACTION_PREV_NOTIFY:
//                bundlePlay.putInt("actionFrom", PlayService.ACTION_FROM_USER);

                mPlayService.prev(PlayService.ACTION_FROM_USER);
//                intentPlay.setAction(String.valueOf(PlayService.ACTION_PREV));
                if (mainActivity != null) {
                    mainActivity.refreshNotificationPlaying(PlayService.ACTION_PLAY);
                }

                break;
            default:
                intentPlay.setAction(String.valueOf(PlayService.ACTION_PLAY));
                break;
        }
//        intentPlay.putExtras(bundlePlay);
//        context.startService(intentPlay);
        if (playActivity != null) {
            playActivity.refreshListPlaying();
            playActivity.updateButtonPlay("Notify");
        }
    }
}
