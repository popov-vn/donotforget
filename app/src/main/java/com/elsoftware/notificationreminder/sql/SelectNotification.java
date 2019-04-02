package com.elsoftware.notificationreminder.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elsoftware.notificationreminder.model.Notification;

import java.util.Date;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

public class SelectNotification implements Select<Notification> {
    int column_id = 0;
    int column_data = 0;
    int column_date = 0;
    int column_notifyId = 0;
    String whereStatement;
    String[] whereArgs;
    String table;

    public SelectNotification(String table){
        this.table = table;
    }

    public void setWhereArgs(String []whereArgs) {
        this.whereArgs = whereArgs;
    }

    public void setWhereStatement() {
        this.whereStatement = "id = ?";
    }

    @Override
    public Cursor run(SQLiteDatabase db) {
        return db.query(table, null, whereStatement, whereArgs, null, null, "date DESC");
    }

    @Override
    public void init(Cursor cursor) {
        column_id = cursor.getColumnIndex("id");
        column_data = cursor.getColumnIndex("data");
        column_date = cursor.getColumnIndex("date");
        column_notifyId = cursor.getColumnIndex("id_notification");
    }

    @Override
    public Notification toItem(Cursor cursor) {
        return new Notification(
                cursor.getString(column_id),
                cursor.getInt(column_notifyId),
                getDate(cursor, column_date),
                cursor.getString(column_data));
    }

    public static Date getDate(Cursor c, int field)
    {
        Date date = new Date();
        try {
            long time = c.getLong(field);
            date.setTime(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
