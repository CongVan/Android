package com.example.musicforlife;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.musicforlife.play.PlayService;

public class NotificationIntentService extends IntentService {
    private static final String TAG = "NotificationIntentServie";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Log.d(TAG, "onHandleIntent: "+intent.getAction());
        switch (Integer.valueOf(intent.getAction())){
            case PlayService.ACTION_PLAY:{
                Toast.makeText(getBaseContext(),"PLAY FROM NOTIFICATION",Toast.LENGTH_LONG).show();
            }
        }
    }
}
