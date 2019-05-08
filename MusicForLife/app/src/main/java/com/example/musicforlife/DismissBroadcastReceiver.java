package com.example.musicforlife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DismissBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "DismissBroadcastReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Toast.makeText(context,"CLOSE NOTIFICATION",Toast.LENGTH_LONG).show();
    }
}
