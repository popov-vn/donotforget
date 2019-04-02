package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.Logger;
import com.elsoftware.notificationreminder.model.NotificationsRepository;
import com.elsoftware.notificationreminder.model.IdProvider;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.model.Restore;

import java.util.ArrayList;
import java.util.List;

import static com.elsoftware.notificationreminder.BuildConfig.DEBUG;

/**
 * Created by popovich on 12.12.2017.
 * eL Software Company, 2017
 */

public class RestoreImpl implements Restore {
    private Logger logger;
    private NotificationsRepository notificationsRepository;
    private NotificationManager notificationManager;
    private IdProvider idProvider;

    public RestoreImpl(Logger logger, NotificationsRepository notificationsRepository, NotificationManager notificationManager, IdProvider idProvider) {
        this.logger = logger;
        this.notificationsRepository = notificationsRepository;
        this.notificationManager = notificationManager;
        this.idProvider = idProvider;
    }

    @Override
    public void restore() {
        List<Notification> notificationList = this.notificationsRepository.getAll();

        if(notificationList.isEmpty())
        {
            logger.Log("Restore", "All notifications done. Clean garbage");
            this.notificationManager.doneAll();
        }
        else
        {
            logger.Log("Restore", "Restore notifications done");
            List<Notification> updatedNotificationList = new ArrayList<>();

            List<Notification> notificationsInManager = this.notificationManager.getAll();
            for(Notification notification : notificationList) {
                if(!isPresentById(notification.getNotifyId(), notificationsInManager)) {
                    NotificationBuilder build = new NotificationBuilder();
                    build.setNotification(notification);
                    build.setNotifyId(idProvider.getNext());
                    Notification updatedNotification = build.build();

                    updatedNotificationList.add(updatedNotification);
                }
            }

            logger.Log("Restore", "Update: " + updatedNotificationList.size() + ", current: " + notificationList.size());
            if(updatedNotificationList.size() == notificationList.size()) {
                logger.Log("Restore", "Full update");
                this.notificationManager.doneAll();
            }

            if(!updatedNotificationList.isEmpty()) {
                for (Notification updatedNotification : updatedNotificationList) {
                    this.notificationManager.add(updatedNotification);
                }
                this.notificationsRepository.updateMany(updatedNotificationList);
            }
        }
    }

    private boolean isPresentById(int id, List<Notification> notifications) {
        for(Notification notification : notifications) {
            if(notification.getNotifyId() == id)
                return true;
        }

        return false;
    }

}
