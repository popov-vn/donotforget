package com.elsoftware.notificationreminder.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public interface Transaction {
    void run(SQLiteDatabase db);
}
