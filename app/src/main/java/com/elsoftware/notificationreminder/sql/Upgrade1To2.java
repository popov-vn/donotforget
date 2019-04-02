package com.elsoftware.notificationreminder.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

public class Upgrade1To2 {
    void upgrade(SQLiteDatabase db){
        Log.d("db", "Upgrade to latest version");

        try {
            // move old data to new location
            final List<Notification> oldNotifications = getOldNotifications(db);
            Log.d("db", "Old notifications: " + oldNotifications.size());

            db.beginTransaction();
            db.execSQL(Constant.create_notification);

            try
            {
                for(Notification notification : oldNotifications) {

                    ContentValues values = new ContentValues();

                    values.put("id", notification.getId());
                    values.put("data", notification.getText());
                    values.put("date", notification.getDate().getTime());
                    values.put("id_notification", notification.getNotifyId());

                    Log.d("db", "Add old: " + notification.getId() + ", " + notification.getText() + ", " + notification.getNotifyId() + ", " + notification.getDate());
                    db.insert(Constant.notificationTable, null, values);
                }

                // remove from old tables
                Log.d("db", "Remove old");
                db.execSQL("delete from " + Constant.historyTable + " where flag = 0");

                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
        }
        catch (Exception ex) {
            Log.e("Db", "Fault move data", ex.getCause());
        }
    }

    private List<Notification> getOldNotifications(SQLiteDatabase db) {
        List<Notification> items = new ArrayList<>();
        Cursor cursor = db.query(Constant.historyTable, null, " flag = 0", null, null, null, null);

        try {
            if (cursor.moveToFirst()) {

                int id = 0;
                int column_data = cursor.getColumnIndex("data");
                int column_date = cursor.getColumnIndex("date");

                do
                {
                    NotificationBuilder builder = new NotificationBuilder();
                    builder.setDate(SelectNotification.getDate(cursor, column_date));
                    builder.setText(cursor.getString(column_data));
                    builder.setNotifyId(id++);

                    items.add(builder.build());
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return items;
    }

}
