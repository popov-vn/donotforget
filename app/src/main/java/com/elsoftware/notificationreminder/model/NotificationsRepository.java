package com.elsoftware.notificationreminder.model;

import java.util.List;

public interface NotificationsRepository extends Repository {
    int remove(String id);

    void add(Notification notification);

    void update(Notification notification);

    void updateMany(List<Notification> notifications);

    void removeAll();

    List<Notification> getAll();

    Notification get(String id);
}
