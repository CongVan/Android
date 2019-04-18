package com.example.nhom4_tuan9;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class ServiceGPS extends Service {

    private static final String TAG = "ServiceGPS";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Thread serviceThread;
    LocationManager lm;
    GPSListener myLocationListener;
    Boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            lm.removeUpdates(myLocationListener);
            isRunning = false;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("GPS", "I am alive-GPS!");
        isRunning = true;
        serviceThread = new Thread(new Runnable() {
            public void run() {

                getGPSFix_Version2(); // uses GPS chip provider
            }// run
        });
        serviceThread.start();
    }// onStart

    public void getGPSFix_Version2() {
        try {
            Looper.prepare();
// try to get your GPS location using the
// LOCATION.SERVIVE provider
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
// This listener will catch and disseminate location updates
//            myLocationListener = new GPSListener();
// define update frequency for GPS readings
            long minTime = 1000; // 2 seconds
            float minDistance = 0; // 5 meter
// request GPS updates(
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
// assemble data bundle to be broadcasted
                    Intent myFilteredResponse = new Intent(MainActivity.filterGPS);
                    myFilteredResponse.putExtra("latitude", latitude);
                    myFilteredResponse.putExtra("longitude", longitude);
                    myFilteredResponse.putExtra("provider", location.getProvider());
                    Log.e(">>GPS_Service<<", "Lat:" + latitude + " lon:" + longitude);
// send the location data out
                    Log.d(TAG, "onLocationChanged: GPS " + latitude);
                    sendBroadcast(myFilteredResponse);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//            Log.d(TAG, "onLocationChanged: GPS OK ");
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: ONCHANGE GPS");
// capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
// assemble data bundle to be broadcasted
            Intent myFilteredResponse = new Intent(MainActivity.filterGPS);
            myFilteredResponse.putExtra("latitude", latitude);
            myFilteredResponse.putExtra("longitude", longitude);
            myFilteredResponse.putExtra("provider", location.getProvider());
            Log.e(">>GPS_Service<<", "Lat:" + latitude + " lon:" + longitude);
// send the location data out
            Log.d(TAG, "onLocationChanged: GPS " + latitude);
            sendBroadcast(myFilteredResponse);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    // GPSListener class
}
