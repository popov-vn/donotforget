package com.elsoftware.notificationreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;

import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.service.JobService;

/**
 * Created by popovich on 17.08.2016.
 * eL Software Company, 2017
 */

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction() != null && intent.getAction().contains(Constant.BOOT_COMPLETED)) {
            JobIntentService.enqueueWork(context, JobService.class, Constant.JOB_ID, new Intent(Constant.RESTORE_JOB));
        }
    }
}

