package com.example.musicforlife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.musicforlife.play.PlayService;

public class BecomingNoisyReceiver extends BroadcastReceiver {
    private static final String TAG = "BecomingNoisyReceiver";
    TelephonyManager telManager;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(TAG, "onReceive: " + intent.getAction());
        telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, "onReceive: " + intent.getAction());
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            // Pause the playback
            PlayService.newInstance().pause();
            Log.d(TAG, "onReceive: ");
        }
    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        //PAUSE
                        PlayService.newInstance().pause();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {

                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        //PLAY
                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception ex) {

            }
        }
    };
}
