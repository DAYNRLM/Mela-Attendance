package com.example.melaAttendance.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.example.melaAttendance.utils.PermissionUtils;

import java.util.ArrayList;

public class LocationHelper implements PermissionUtils.PermissionResultCallback {

    private Context context;
    private Activity current_activity;

    private boolean isPermissionGranted;

    private Location mLastLocation;

    // Google client to interact with Google API

   // private GoogleApiClient mGoogleApiClient;
    @Override
    public void PermissionGranted(int request_code) {

    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }
}
