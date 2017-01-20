package com.maranan;



import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.maranan.utils.DataContainer;


public class ApplicationSingleton extends Application {
    public MediaPlayer player;
    public MediaPlayer player2;
    public DataContainer dataContainer;
    private static ApplicationSingleton mContext;

    // ApplicationSingleton Instance
    public static ApplicationSingleton getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = ApplicationSingleton.this;
        player = new MediaPlayer();
        player2 = new MediaPlayer();
        dataContainer = new DataContainer();
        Log.e("ApplicationSingleton", "Maranan");
        // Setup handler for uncaught exceptions.
//        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//                this));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
}
