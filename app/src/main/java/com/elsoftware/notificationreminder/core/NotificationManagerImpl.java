package com.elsoftware.notificationreminder.core;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.R;
import com.elsoftware.notificationreminder.model.Logger;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.receiver.NotificationReceiver;
import com.elsoftware.notificationreminder.ui.activity.EditActivity;
import com.elsoftware.notificationreminder.ui.activity.MainActivity;
import com.elsoftware.notificationreminder.utils.NotificationText;

import java.util.ArrayList;
import java.util.List;

import static android.app.NotificationManager.IMPORTANCE_LOW;
import static com.elsoftware.notificationreminder.BuildConfig.DEBUG;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class NotificationManagerImpl implements NotificationManager {

    private Context context;
    Logger logger;
    private static String CHANNEL_ID = "default";

    public NotificationManagerImpl(Logger logger, Context context) {
        this.logger = logger;
        this.context = context;
    }

    @Override
    public void doneAll() {
        logger.Log("NotificationManagerImpl", "cancel all");

        android.app.NotificationManager manager = getInternalManager();
        manager.cancelAll();
    }

    @Override
    public void add(Notification notification) {
        logger.Log("NotificationManagerImpl", String.format("add notification: %d %s", notification.getNotifyId(), notification.getId()));

        android.app.NotificationManager manager = getInternalManager();
        configureChannel(manager);

        android.app.Notification androidNotification = buildNotification(notification);
        manager.notify(notification.getNotifyId(), androidNotification);
    }

    @Override
    public void done(int id) {
        logger.Log("NotificationManagerImpl", String.format("cancel notification: %d", id));

        android.app.NotificationManager manager = getInternalManager();
        manager.cancel(id);
    }

    @Override
    public List<Notification> getAll() {
        logger.Log("NotificationManagerImpl", "getAll");

        List<Notification> notifications = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.app.NotificationManager manager = getInternalManager();
            StatusBarNotification[] currentNotifications = manager.getActiveNotifications();

            for(StatusBarNotification notification : currentNotifications) {
                NotificationBuilder builder = new NotificationBuilder();
                builder.setNotifyId(notification.getId());

                notifications.add(builder.build());
            }
        }

        return notifications;
    }

    private android.app.NotificationManager getInternalManager() {
        return (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent createEditAction(String uuid, int id) {
        Intent edit = EditActivity.createStart(context, uuid);
        return PendingIntent.getActivity(context, id, edit, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createDoneAction(String uuid, int id) {
        Intent done = new Intent(context, NotificationReceiver.class);
        done.setAction(Constant.DONE_ACTION);
        done.putExtra("notify_id", id);
        done.putExtra("id", uuid);

        return PendingIntent.getBroadcast(context, id, done, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void configureChannel(android.app.NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = manager.getNotificationChannel(CHANNEL_ID);
            final String description = context.getResources().getString(R.string.default_name);
            final String name = context.getResources().getString(R.string.app_name);
            final boolean vibration = false;
            final boolean lights = false;
            final int importance = IMPORTANCE_LOW;
            boolean reCreate;

            if(null == notificationChannel) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                reCreate = true;
                Log.d("NotificationManagerImpl", "Create channel");
            }
            else {
                boolean parametersEqual = notificationChannel.getDescription().contains(description) &&
                        notificationChannel.shouldVibrate() == vibration &&
                        notificationChannel.shouldShowLights() == lights &&
                        notificationChannel.getImportance() == importance &&
                        notificationChannel.getName().toString().contains(name);

                reCreate = !parametersEqual;
            }

            if(reCreate) {
                Log.d("NotificationManagerImpl", "Reconfigure channel");

                notificationChannel.setName(name);
                notificationChannel.setDescription(description);
                notificationChannel.enableLights(lights);
                notificationChannel.enableVibration(vibration);
                notificationChannel.setImportance(importance);

                manager.createNotificationChannel(notificationChannel);
            }
        }
    }

    private android.app.Notification buildNotification(Notification notification) {
        NotificationText notificationText = new NotificationText(notification.getText());
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent doneIntent = createDoneAction(notification.getId(), notification.getNotifyId());
        PendingIntent editIntent = createEditAction(notification.getId(), notification.getNotifyId());

        android.support.v4.app.NotificationCompat.Builder nb = new NotificationCompat.Builder(context, CHANNEL_ID);
        nb.setSmallIcon(R.drawable.ic_remind_icon_white)
                .setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary))
                .setAutoCancel(false)
                .setTicker(notification.getText())
                .setContentIntent(PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(notification.getDate().getTime())
                .setContentTitle(notificationText.getTitle())
                .addAction(R.drawable.ic_done_black_24dp, context.getResources().getString(R.string.done), doneIntent)
                .addAction(R.drawable.ic_mode_edit_black_24dp, context.getResources().getString(R.string.edit), editIntent);

        if(notificationText.isDescriptionExist()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                nb.setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(notification.getText()));
            } else{
                nb.setContentText(notificationText.getDescription());
            }
        }

        android.app.Notification anNotification = nb.build();

        anNotification.flags |= android.app.Notification.FLAG_ONGOING_EVENT;
        anNotification.flags |= android.app.Notification.FLAG_NO_CLEAR;

        return anNotification;
    }
}
