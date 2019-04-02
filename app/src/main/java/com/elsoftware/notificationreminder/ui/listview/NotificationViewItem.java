package com.elsoftware.notificationreminder.ui.listview;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public class NotificationViewItem {
        protected String id;
        protected String text;
        protected String dateText;

        public NotificationViewItem(String id, String dateText, String text)
        {
            this.id = id;
            this.text = text;
            this.dateText = dateText;
        }

        public String getText()
        {
            return text;
        }

        public String getDate()
        {
            return dateText;
        }

        public String getId()
        {
            return id;
        }
}
