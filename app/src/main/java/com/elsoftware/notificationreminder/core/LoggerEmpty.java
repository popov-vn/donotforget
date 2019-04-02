package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.Logger;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

public class LoggerEmpty implements Logger {
    @Override
    public void Log(String tag, String message) { }
}
