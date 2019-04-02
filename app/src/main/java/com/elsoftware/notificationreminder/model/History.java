package com.elsoftware.notificationreminder.model;

import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public interface History {
    void add(Notification notification);

    void remove(String id);

    void removeAll();

    List<Notification> getAll();

    void close();
}
