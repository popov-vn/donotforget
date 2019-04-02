package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.Notification;

import java.util.Date;
import java.util.UUID;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class NotificationBuilder
{
    private int notifyId;
    private String text;
    private String id;
    private Date date;

    public void setNotifyId(int id) {
        notifyId = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNotification(Notification notification) {
        this.date = notification.getDate();
        this.id = notification.getId();
        this.notifyId = notification.getNotifyId();
        this.text = notification.getText();
    }

    public Notification build()
    {
        if(id == null)
            id = UUID.randomUUID().toString();

        if(date == null)
            date = new Date();

        return new Notification(id, notifyId, date, text);
    }
}
