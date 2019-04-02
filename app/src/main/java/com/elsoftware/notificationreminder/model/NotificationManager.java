package com.elsoftware.notificationreminder.model;

import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public interface NotificationManager {
    void doneAll();

    void add(Notification notification);

    void done(int notifyId);

    List<Notification> getAll();
}
