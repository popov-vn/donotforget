package com.elsoftware.notificationreminder.model;

import java.util.Date;

/**
 * Created by popovich on 17.08.2016.
 * eL Software Company, 2017
 */

public class Notification
{
    private String id;
    private int notifyId;
    private Date date;
    private String text;

    public Notification(String id, int notifyId, Date date, String text)
    {
        this.id = id;
        this.date = date;
        this.text = text;
        this.notifyId = notifyId;
    }

    public String getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public String getText()
    {
        return text;
    }

    public int getNotifyId() { return notifyId; }
}
