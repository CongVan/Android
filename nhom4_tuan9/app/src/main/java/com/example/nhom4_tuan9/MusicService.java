package com.example.nhom4_tuan9;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {
    public static boolean boolIsServiceCreated = false;

    MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
//        super.onCreate();
        Toast.makeText(getApplicationContext(), "MyService4 Created", Toast.LENGTH_LONG).show();
        Log.e("MyService4", "onCreate");

        player=MediaPlayer.create(getApplicationContext(),R.raw.hong_nhan);
        boolIsServiceCreated = true;

    }

    @Override
    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
        if (player.isPlaying())
            Toast.makeText(this, "MyService4 Already Started " + startId,
                    Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "MyService4 Started " + startId,
                    Toast.LENGTH_LONG).show();
        Log.e("MyService4", "onStart");
        player.start();
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        Toast.makeText(this, "MyService4 Stopped", Toast.LENGTH_LONG).show();
        Log.e("MyService4", "onDestroy");
        player.stop();
        player.release();
        player = null;
    }
}
