package com.example.nhom4_tuan9;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btnStartMusic;
    Button btnStartFib;
    Button btnStartGPS;

    Button btnStopMusic;
    Button btnStopFib;
    Button btnStopGPS;

    TextView txtContent;
    Intent intentServiceMusic;
    Intent intentServiceFib;
    Intent intentServiceGPS;
    BroadcastReceiver receiver;

    public static final String filterFibo="FILTER_FIBO";
    public static final String filterGPS="FILTER_GPS";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartMusic = findViewById(R.id.btnStartMusic);
        btnStartFib = findViewById(R.id.btnStartFib);
        btnStartGPS = findViewById(R.id.btnStartGPS);

        btnStopMusic = findViewById(R.id.btnStopMusic);
        btnStopFib = findViewById(R.id.btnStopFib);
        btnStopGPS = findViewById(R.id.btnStopGPS);
        txtContent=findViewById(R.id.txtContent);

        btnStartMusic.setOnClickListener(this);
        btnStartFib.setOnClickListener(this);
        btnStartGPS.setOnClickListener(this);
        btnStopMusic.setOnClickListener(this);
        btnStopFib.setOnClickListener(this);
        btnStopGPS.setOnClickListener(this);


        intentServiceMusic= new Intent(this,MusicService.class);
        intentServiceFib=new Intent(this,ServiceFibo.class);
        intentServiceGPS=new Intent(this,ServiceGPS.class);

        IntentFilter filter5 = new IntentFilter(filterFibo);
        IntentFilter filter6 = new IntentFilter(filterGPS);
        receiver = new MyEmbeddedBroadcastReceiver();
        registerReceiver(receiver, filter5);
        registerReceiver(receiver, filter6);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: "+v.getId());
        switch (v.getId()) {
            case R.id.btnStartMusic:
                Log.d(TAG, "onClick: START MUSIC");
                startService(intentServiceMusic);
                break;
            case R.id.btnStartFib:
                startService(intentServiceFib);
                break;
            case R.id.btnStartGPS:
                startService(intentServiceGPS);
                break;
            case R.id.btnStopMusic:
                stopService(intentServiceMusic);
                break;
            case R.id.btnStopFib:
                stopService(intentServiceFib);
                break;
            case R.id.btnStopGPS:
                stopService(intentServiceGPS);
                break;
            default:
                break;
        }
    }

    public class MyEmbeddedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MAIN>>>", "ACTION: " + intent.getAction());
            if (intent.getAction().equals(filterFibo)) {
                String service5Data = intent.getStringExtra("MyService5DataItem");
                Log.e("MAIN>>>", "Data received from Service5: " + service5Data);
                txtContent.append("\nService5Data: > " + service5Data);
            } else if (intent.getAction().equals(filterGPS)) {
                double latitude = intent.getDoubleExtra("latitude", -1);
                double longitude = intent.getDoubleExtra("longitude", -1);
                String provider = intent.getStringExtra("provider");
                String service6Data = provider
                        + " lat: " + Double.toString(latitude)
                        + " lon: " + Double.toString(longitude);
                Log.e("MAIN>>>", "Data received from Service6: " + service6Data);
                txtContent.append("\nService6Data: > " + service6Data);
            }
        }//onReceive
    }// MyEmbeddedBroadcastReceiver
}
