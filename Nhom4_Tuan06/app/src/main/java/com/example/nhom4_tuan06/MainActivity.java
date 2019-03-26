package com.example.nhom4_tuan06;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtSize;
    TextView txtPercent;
    ProgressBar progressBarStatus;
    Long maxSize;
    Long piecePercent;
    Long currentPercent =Long.valueOf(0);
    Handler mainHandle;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSize = findViewById(R.id.txtSize);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarStatus = findViewById(R.id.prgStatus);
        progressBarStatus.setMax(100);
        mainHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handleMessage: " + piecePercent);
                Integer t = (Integer) msg.obj;

//                    currentPercent=currentPercent+1;
                txtPercent.setText(currentPercent.toString() + "%");
                progressBarStatus.setProgress(Integer.valueOf(currentPercent.toString()));

//                currentPercent=currentPercent+piece;
//                txtPercent.setText(currentPercent.toString() + "%");
//                progressBarStatus.setProgress(currentPercent);
            }
        };

    }


    public void submimtStatus(View view) {
        progressBarStatus.setProgress(0);
        currentPercent = Long.valueOf(0);
        piecePercent = Long.valueOf(0);
        txtPercent.setText("0%");
        maxSize = Long.parseLong(txtSize.getText().toString());
        piecePercent = maxSize / 100;
        StatusThread statusThread = new StatusThread();
        statusThread.start();
    }

    private class StatusThread extends Thread {
        @Override
        public void run() {

            for (int i = 0; i <= piecePercent && currentPercent < 100;) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == piecePercent) {
                    i = 0;
                    currentPercent++;
                    Message msg = mainHandle.obtainMessage(1, i);
                    mainHandle.sendMessage(msg);
                    Log.i(TAG, "runTheard: " + i +"- "+currentPercent);
                }else{
                    i++;
                }

            }
        }
    }
}
