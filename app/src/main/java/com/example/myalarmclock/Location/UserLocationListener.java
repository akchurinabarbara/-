package com.example.myalarmclock.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class UserLocationListener implements LocationListener {
    //Положение пользователя
    static Location userLocation;

    public static void SetUpLocationListener(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new UserLocationListener();

        int i = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int j =  ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10,
                locationListener
        );

        userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    public static Location getUserLocation() {
        return userLocation;
    }

    @Override
    public void onLocationChanged(Location loc){
        userLocation = loc;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
