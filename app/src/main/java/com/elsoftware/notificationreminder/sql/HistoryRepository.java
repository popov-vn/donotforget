package com.elsoftware.notificationreminder.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elsoftware.notificationreminder.model.Notification;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */
public class HistoryRepository extends BaseRepository implements com.elsoftware.notificationreminder.model.HistoryRepository {
    public HistoryRepository(Context context) {
        super(context);
    }

    @Override
    public void remove(final String id) {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                db.delete(Constant.historyTable, "id = ?", new String[] {id});
            }
        });
    }

    @Override
    public void add(final Notification notification) {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                ContentValues values = new ContentValues();

                String internalId = UUID.randomUUID().toString();

                values.put("id", internalId);
                values.put("data", notification.getText());
                values.put("date", (new Date()).getTime());

                db.insert(Constant.historyTable, null, values);
            }
        });
    }

    @Override
    public void removeAll() {
        transactional(new Transaction() {
            @Override
            public void run(SQLiteDatabase db) {
                db.delete(Constant.historyTable, null, null);
            }
        });
    }

    @Override
    public List<Notification> getAll() {
        return select(new Select<Notification>() {
            int column_id = 0;
            int column_data = 0;
            int column_date = 0;

            @Override
            public Cursor run(SQLiteDatabase db) {
                return db.query(Constant.historyTable, null, null, null, null, null, "date DESC");
            }

            @Override
            public void init(Cursor cursor) {
                column_id = cursor.getColumnIndex("id");
                column_data = cursor.getColumnIndex("data");
                column_date = cursor.getColumnIndex("date");
            }

            @Override
            public Notification toItem(Cursor cursor) {
                return new Notification(
                        cursor.getString(column_id),
                        0,
                        SelectNotification.getDate(cursor, column_date),
                        cursor.getString(column_data));
            }
        });
    }
}
