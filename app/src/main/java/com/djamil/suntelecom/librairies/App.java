package com.djamil.suntelecom.librairies;

import android.app.Application;
import android.util.Log;

import com.djamil.suntelecom.truetime.TrueTimeRx;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.reactivex.schedulers.Schedulers;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 23/12/2020
 */

public class App extends Application {
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());

        com.djamil.suntelecom.truetime.TrueTimeRx.build()
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .subscribe(date -> {
                    Log.v(TAG, "TrueTime was initialized and we have a time: " + date);
                }, Throwable::printStackTrace);
    }
}