package com.elsoftware.notificationreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;

import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.service.JobService;

/**
* Created by popovich on 07.08.2016.
*/

public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().contains(Constant.DONE_ACTION))
        {
            Intent work = new Intent(Constant.DONE_JOB);
            work.putExtra("id", intent.getStringExtra("id"));

            JobIntentService.enqueueWork(context, JobService.class, Constant.JOB_ID, work);
        }
    }
}
