package com.elsoftware.notificationreminder.model;

import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public interface Notifications {
    void add(Notification notification);

    void edit(Notification notification);

    void done(String id);

    void doneAll();

    List<Notification> getAll();

    Notification get(String id);

    void close();
}
