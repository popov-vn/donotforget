package com.elsoftware.notificationreminder.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class NotificationRepository extends BaseRepository implements NotificationsRepository {

    public NotificationRepository(Context context) {
        super(context);
    }

    @Override
    public int remove(final String id) {

        Notification notification = get(id);

        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                db.delete(Constant.notificationTable, "id = ?", new String[] {id});
            }
        });

        if(notification != null)
            return notification.getNotifyId();

        return 0;
    }

    @Override
    public void add(final Notification notification) {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                ContentValues values = new ContentValues();

                values.put("id", notification.getId());
                values.put("data", notification.getText());
                values.put("date", notification.getDate().getTime());
                values.put("id_notification", notification.getNotifyId());

                db.insert(Constant.notificationTable, null, values);
            }
        });
    }

    @Override
    public void update(final Notification notification) {
        List<Notification> list = new ArrayList<>();
        list.add(notification);
        updateMany(list);
    }

    @Override
    public void updateMany(final List<Notification> notifications) {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                for(Notification notification : notifications) {
                    ContentValues values = new ContentValues();

                    values.put("data", notification.getText());
                    values.put("date", notification.getDate().getTime());
                    values.put("id_notification", notification.getNotifyId());

                    db.update(Constant.notificationTable, values, "id = ?", new String[]{notification.getId()});
                }
            }
        });
    }

    @Override
    public void removeAll() {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                db.delete(Constant.notificationTable, null, null);
            }
        });
    }

    @Override
    public List<Notification> getAll() {
        return select(new SelectNotification(Constant.notificationTable));
    }

    @Override
    public Notification get(final String id) {
        SelectNotification helper = new SelectNotification(Constant.notificationTable);
        helper.setWhereArgs(new String[] {id});
        helper.setWhereStatement();

        List<Notification> notifications = select(helper);

        if(notifications.size() > 0)
            return notifications.get(0);

        return null;
    }
}
