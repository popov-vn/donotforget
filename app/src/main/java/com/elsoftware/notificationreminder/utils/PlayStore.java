package com.elsoftware.notificationreminder.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by popovich on 14.12.2017.
 */

public class PlayStore {
    public static void startCurrent(Context context) {
        startPackage(context.getPackageName(), context);
    }

    public static void startPackage(String packageId, Context context)
    {
        Uri uri = Uri.parse("market://details?id=" + packageId);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        goToMarket.addFlags(flags);

        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageId)));
        }
    }
}
