package com.elsoftware.notificationreminder.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.elsoftware.notificationreminder.core.BroadcastObserver;
import com.elsoftware.notificationreminder.core.HistoryImpl;
import com.elsoftware.notificationreminder.core.IdProviderImpl;
import com.elsoftware.notificationreminder.core.LoggerEmpty;
import com.elsoftware.notificationreminder.core.LoggerImpl;
import com.elsoftware.notificationreminder.core.NotificationManagerImpl;
import com.elsoftware.notificationreminder.core.NotificationsImpl;
import com.elsoftware.notificationreminder.core.RestoreEmptyImpl;
import com.elsoftware.notificationreminder.core.RestoreImpl;
import com.elsoftware.notificationreminder.model.Logger;
import com.elsoftware.notificationreminder.model.NotificationsRepository;
import com.elsoftware.notificationreminder.model.History;
import com.elsoftware.notificationreminder.model.IdProvider;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.model.Notifications;
import com.elsoftware.notificationreminder.model.NotificationsObserver;
import com.elsoftware.notificationreminder.model.Restore;
import com.elsoftware.notificationreminder.sql.NotificationRepository;
import com.elsoftware.notificationreminder.sql.HistoryRepository;

import static com.elsoftware.notificationreminder.BuildConfig.DEBUG;

/**
 * Created by popovich on 17.08.2016.
 * eL Software Company, 2017
 */

public class ReminderApplication extends Application
{
    private IdProvider idProvider = new IdProviderImpl();
    private boolean mNotificationsRestored;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationsRestored = false;
    }

    @NonNull
    private NotificationsRepository createActiveRepository() {
        return new NotificationRepository(this);
    }

    @NonNull
    private com.elsoftware.notificationreminder.model.HistoryRepository createHistoryRepository() {
        return new HistoryRepository(this);
    }

    @NonNull
    private NotificationManager createNotificationManager() {
        return new NotificationManagerImpl(createeLogger(), this);
    }

    @NonNull
    private IdProvider createIdProvider() {
        return idProvider;
    }

    synchronized public Notifications createNotifications()
    {
        NotificationsObserver broadcastObserver = new BroadcastObserver(this);
        NotificationsImpl notifications = new NotificationsImpl(createActiveRepository(), createHistoryRepository(), createNotificationManager(), createIdProvider());
        notifications.addObserver(broadcastObserver);
        return notifications;
    }

    synchronized public History createHistory()
    {
        return new HistoryImpl(createHistoryRepository());
    }

    synchronized public Restore createRestore()
    {
        if(!mNotificationsRestored)
            return new RestoreImpl(createeLogger(), createActiveRepository(), createNotificationManager(), createIdProvider());

        return new RestoreEmptyImpl();
    }

    public Logger createeLogger() {
        if(DEBUG) return new LoggerImpl();

        return new LoggerEmpty();
    }

}
