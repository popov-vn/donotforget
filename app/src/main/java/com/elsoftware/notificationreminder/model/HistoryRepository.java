package com.elsoftware.notificationreminder.model;

import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public interface HistoryRepository extends Repository {
    void remove(String id);

    void add(Notification notification);

    void removeAll();

    List<Notification> getAll();
}
