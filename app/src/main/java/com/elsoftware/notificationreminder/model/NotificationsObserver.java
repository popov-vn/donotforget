package com.elsoftware.notificationreminder.model;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public interface NotificationsObserver {
    void onCreate();
    void onDone();
    void onEdit();
}
