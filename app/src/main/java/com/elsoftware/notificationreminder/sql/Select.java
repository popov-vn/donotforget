package com.elsoftware.notificationreminder.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */
public interface Select<V>{
    Cursor run(SQLiteDatabase db);

    void init(Cursor cursor);
    V toItem(Cursor cursor);
}
