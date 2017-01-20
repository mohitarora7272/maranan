package com.maranan.utils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.maranan.gcm.Constant;


public class MarshMallowPermission implements Constant {

    Activity activity;


    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    /* Check Permission For Access Wifi State */
    public boolean checkPermissionForAccessWifiState() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For Internet */
    public boolean checkPermissionForInternet() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For Read External Storage */
    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For ACCESS_NETWORK_STATE */
    public boolean checkPermissionForACCESS_NETWORK_STATE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For Get Account */
    public boolean checkPermissionForGET_ACCOUNTS() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For WAKE_LOCK */
    public boolean checkPermissionForWAKE_LOCK() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For RECORD_AUDIO */
    public boolean checkPermissionForRECORD_AUDIO() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For WriteExternalStorage */
    public boolean checkPermissionForWriteExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For READ_PHONE_STATE */
    public boolean checkPermissionForREAD_PHONE_STATE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For CALL_PHONE */
    public boolean checkPermissionForCALL_PHONE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For WRITE_SETTINGS */
    public boolean checkPermissionForWRITE_SETTINGS() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For RECEIVE_BOOT_COMPLETED */
    public boolean checkPermissionForRECEIVE_BOOT_COMPLETED() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For CHANGE_WIFI_STATE */
    public boolean checkPermissionForCHANGE_WIFI_STATE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_WIFI_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For CHANGE_NETWORK_STATE */
    public boolean checkPermissionForCHANGE_NETWORK_STATE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_NETWORK_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /* Check Permission For VIBRATE */
    public boolean checkPermissionForVIBRATE() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    /* Check Common Permission For App */
    public void commonPermissionForApp() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_WIFI_STATE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_NETWORK_STATE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WAKE_LOCK)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.VIBRATE)

                ) {


            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.VIBRATE},
                    COMMON_PERMISSION_REQUEST_CODE);


        } else {
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.VIBRATE},
                    COMMON_PERMISSION_REQUEST_CODE);

        }
    }
}
