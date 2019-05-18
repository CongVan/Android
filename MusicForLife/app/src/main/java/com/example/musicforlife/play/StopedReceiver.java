package com.example.musicforlife.play;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.musicforlife.listsong.SongModel;

public class StopedReceiver extends BroadcastReceiver {
    private static final String TAG = "StopedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SongModel songPlay = (SongModel) bundle.getSerializable(SongModel.class.toString());
            Log.d(TAG, "onReceive: SONG PLAY RECEIVER " + songPlay);
            Intent playIntent = new Intent(context, PlayService.class);
            try {
                if (PlayService.isPlaying()) {
                    playIntent.setAction(String.valueOf(PlayService.ACTION_RESUME));
                } else {
                    playIntent.setAction(String.valueOf(PlayService.ACTION_PAUSE));
                }

//            Bundle bundlePlay=new Bundle();
//            bundlePlay.putSerializable(SongModel.class.toString(),songPlay);
                playIntent.putExtras(bundle);
                context.startService(playIntent);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        }

    }
}
