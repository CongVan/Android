package com.example.musicforlife;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.utilitys.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {


    private String[] appPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private static final int PERMISSION_REQUEST_CODE = 1240;
    private static final String TAG = "SplashActivity";
    private DatabaseManager mDatabaseManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utility.setTransparentStatusBar(this);
        boolean canWrite = Settings.System.canWrite(getApplicationContext());
        Log.d(TAG, "onCreate: CAN WRITE " + canWrite);
        if (canWrite) {
            if (checkAndRequestPermission()) {
                initApp();
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

//        initApp();

    }

    public void initApp() {
        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "doInBackground: SIZE AUDIOS " + SongModel.getRowsSong(mDatabaseManager));
                ArrayList<SongModel> tempAudioList = SongModel.getAllAudioFromDevice(getApplicationContext());
                Log.d(TAG, "doInBackground: AUDIO " + tempAudioList.size());
                if (SongModel.getRowsSong(mDatabaseManager) != tempAudioList.size()) {
                    for (SongModel song : tempAudioList) {
                        long id = SongModel.insertSong(mDatabaseManager, song);
                        Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
                    }
                }
            }
        }).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }
        }, 100);

    }

    public boolean checkAndRequestPermission() {
        List<String> listPermissionNeded = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeded.add(perm);
            }
        }
        if (!listPermissionNeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeded.toArray(new String[listPermissionNeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                initApp();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String perName = entry.getKey();
                    Integer perResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, perName)) {
                        showDialog("", "Cấp quyền ", "Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkAndRequestPermission();
                            }
                        }, "Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }, false);
                    } else {
                        showDialog("", "Cấp quyền ", "Cài đặt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                checkAndRequestPermission();
                                Intent intent = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }, "Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }, false);
                        break;
                    }

                }
            }

        }
    }

    public AlertDialog showDialog(String title, String message, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                  String negativeLabel, DialogInterface.OnClickListener negativeOnclick, boolean isCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancel);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}
