package com.elsoftware.notificationreminder.core;

import android.util.Log;

import com.elsoftware.notificationreminder.model.Logger;

import static com.elsoftware.notificationreminder.BuildConfig.DEBUG;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

public class LoggerImpl implements Logger{
    @Override
    public void Log(String tag, String message) {
        if(DEBUG) Log.d(tag, message);
    }
}
