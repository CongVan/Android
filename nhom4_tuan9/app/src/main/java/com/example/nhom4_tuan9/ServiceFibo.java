package com.example.nhom4_tuan9;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class ServiceFibo extends Service {
    boolean isRunning = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        new ComputeFibonacciRecursivelyTask().execute(20, 50);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e ("S FIBO", "I am dead-5-Async");
        isRunning = false;
    }

    public Integer fibonacci(Integer n){
        if ( n==0 || n==1 )
            return 1;
        else
            return fibonacci(n-1) + fibonacci(n-2);
    }
    public class ComputeFibonacciRecursivelyTask extends AsyncTask<Integer, Integer, Integer > {
        @Override
        protected Integer doInBackground(Integer... params) {
            for (int i=params[0]; i<params[1] && isRunning ; i++){
                Integer fibn = fibonacci(i);
                publishProgress(i, fibn);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Intent intentFilter5 = new Intent(MainActivity.filterFibo);
            String data = "dataItem-5-fibonacci-AsyncTask"
                    + values[0] + ": " + values[1];
            intentFilter5.putExtra("MyService5DataItem", data);
            sendBroadcast(intentFilter5);
        }
    }// ComputeFibonacciRecursivelyTask
}
