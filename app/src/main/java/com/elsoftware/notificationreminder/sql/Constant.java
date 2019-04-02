package com.elsoftware.notificationreminder.sql;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

public class Constant {

    public static String historyTable = "history";
    public static String notificationTable = "notification";

    public static String create_history = "create table history (id TEXT, data TEXT, date INTEGER, flag INTEGER);";
    public static String create_notification = "create table notification (id TEXT, data TEXT, date INTEGER, id_notification INTEGER);";
}
