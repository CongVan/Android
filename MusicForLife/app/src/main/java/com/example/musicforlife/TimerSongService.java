package com.example.musicforlife;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.musicforlife.play.PlayService;

public class TimerSongService extends Service {

    private long mTimes;
    private long mCurrentTime;
    private boolean isRuning;
    private CountDownTimer mCoutTimer;
    public static final String ACTION_START_TIMER = "START_TIMER_SONG";
    public static final String ACTION_TICK_TIMER = "TICK_TIMER_SONG";
    public static final String ACTION_FINISH_TIMER = "FINISH_TIMER_SONG";

    private static TimerSongService mTimerSongService;
    private static final String TAG = "TimerSongService";

    public static TimerSongService newIntance(){

        if (mTimerSongService==null){
            mTimerSongService=new TimerSongService();
        }
        return mTimerSongService;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: TIMER ");
        String action = intent.getAction();
        if (!action.equals(ACTION_START_TIMER)) {
            return START_NOT_STICKY;
        }
        Bundle bundle = intent.getExtras();
        mTimes = bundle.getInt("Times") * 60000;
        Log.d(TAG, "onStartCommand: TIMES="+mTimes);
        mCurrentTime = 0;
        isRuning = true;


        mCoutTimer = new CountDownTimer(mTimes, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.d(TAG, "onTick: TIMES " + mTimes + ", CURRENT TIME " + mCurrentTime);
                if (isRuning) {
                    mCurrentTime += 1000;
                } else {
                    mCoutTimer.cancel();
                    stopSelf();
                }
                Intent intentTick = new Intent();
                intentTick.setAction(ACTION_TICK_TIMER);
                intentTick.putExtra("times",mTimes);
                intentTick.putExtra("currentTimes",mCurrentTime);
                sendBroadcast(intentTick);
            }

            @Override
            public void onFinish() {
                Intent intentFinish = new Intent();
                intentFinish.setAction(ACTION_FINISH_TIMER);
                sendBroadcast(intentFinish);
                stopSelf();
            }
        }.start();
        Intent intentStart = new Intent();
        intentStart.setAction(ACTION_START_TIMER);
        sendBroadcast(intentStart);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public long getmCurrentTime() {
        return mCurrentTime;
    }

    public void setmCurrentTime(long mCurrentTime) {
        this.mCurrentTime = mCurrentTime;
    }

    public long getTimes() {
        return mTimes;
    }

    public void setTimes(long times) {
        this.mTimes = times;
    }

    public void setRuning(boolean runing) {
        isRuning = runing;
    }

    public boolean isRuning() {
        return isRuning;
    }
}
