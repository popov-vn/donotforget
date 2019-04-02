package com.elsoftware.notificationreminder.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class BaseRepository extends SQLiteOpenHelper {

    Context context;
    public BaseRepository(Context context)
    {
        super(context, "reminderDb", null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("db", "Create database");

        db.execSQL(Constant.create_history);
        db.execSQL(Constant.create_notification);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("db", "Upgrade from " + oldVersion + " to " + newVersion);

        try {
            if(oldVersion == 1 && newVersion == 2) {
                new Upgrade1To2().upgrade(db);
            }
        } catch (Exception ex) {
            Log.e("Db", "Fault upgrade", ex.getCause());
        }
    }

    public void transactional(Transaction transaction)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try
            {
                transaction.run(db);
                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
        }
        catch (Exception ex)
        {
            Log.d("SqlBase", ex.toString());
        }
    }

    public <T> List<T> select(Select<T> select)
    {
        List<T> items = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = select.run(db);

            try {
                if (c.moveToFirst()) {
                    select.init(c);
                    do items.add(select.toItem(c));
                    while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        catch (Exception ex)
        {
            Log.d("db", ex.toString());
        }

        return items;
    }



}
