package com.elsoftware.notificationreminder.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by popovich on 11.12.2017.
 */

public class DateString {

    Date time;
    Context context;
    public DateString(Date time, Context context)
    {
        this.time = time;
        this.context =context;
    }

    @Override
    public String toString()
    {
        Calendar clDate = new GregorianCalendar();
        clDate.setTime(time);

        Calendar clCurrent = new GregorianCalendar();
        String dateTimeStr;
        if(clCurrent.get(Calendar.DAY_OF_MONTH) == clDate.get(Calendar.DAY_OF_MONTH) &&
                clCurrent.get(Calendar.MONTH) == clDate.get(Calendar.MONTH) &&
                clCurrent.get(Calendar.YEAR) == clDate.get(Calendar.YEAR)) {
            java.text.DateFormat df = DateFormat.getTimeFormat(context);
            dateTimeStr = df.format(time);
        }
        else {
            java.text.DateFormat df = DateFormat.getDateFormat(context);
            dateTimeStr = df.format(time);
        }

        return  dateTimeStr;
    }
}
