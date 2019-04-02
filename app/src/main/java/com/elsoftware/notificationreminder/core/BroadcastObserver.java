package com.elsoftware.notificationreminder.core;

import android.content.Context;
import android.content.Intent;

import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.model.NotificationsObserver;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public class BroadcastObserver implements NotificationsObserver {
    final private Context context;

    public BroadcastObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Intent updateIntent = new Intent(Constant.ON_CREATE);
        context.sendBroadcast(updateIntent);
    }

    @Override
    public void onDone() {
        Intent doneIntent = new Intent(Constant.ON_DONE);
        context.sendBroadcast(doneIntent);
    }

    @Override
    public void onEdit() {
        Intent editIntent = new Intent(Constant.ON_EDIT);
        context.sendBroadcast(editIntent);
    }
}
