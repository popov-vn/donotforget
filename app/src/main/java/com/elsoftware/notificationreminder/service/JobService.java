package com.elsoftware.notificationreminder.service;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.app.ReminderApplication;
import com.elsoftware.notificationreminder.model.Notifications;
import com.elsoftware.notificationreminder.model.Restore;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class JobService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try
        {
            if(intent.getAction() != null) {
                if(intent.getAction().contains(Constant.RESTORE_JOB)) {
                    processRestore();
                }
                else if(intent.getAction().contains(Constant.DONE_JOB)) {
                    processComplete(intent);
                }
            }
        }
        catch (Exception ex) {

        }
    }

    protected void processComplete(@NonNull Intent intent)
    {
        String id = intent.getStringExtra("id");

        ReminderApplication application = (ReminderApplication)getApplicationContext();
        Notifications notifications = application.createNotifications();

        notifications.done(id);
    }

    protected void processRestore()
    {
        ReminderApplication application = (ReminderApplication)getApplicationContext();
        Restore restore = application.createRestore();
        restore.restore();
    }
}
