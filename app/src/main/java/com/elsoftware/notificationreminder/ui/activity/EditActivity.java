package com.elsoftware.notificationreminder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.elsoftware.notificationreminder.R;
import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.app.ReminderApplication;
import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.Notifications;

public class EditActivity extends AppCompatActivity {
    Notifications notificationsService;
    Notification notification;

    static String idField = "id";

    public static Intent createStart(Context context, String id) {
        Intent edit = new Intent(context, EditActivity.class);
        edit.setAction(Constant.EDIT_ACTION);
        edit.putExtra(idField, id);
        return edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String id = getBundleId();
        if(id != null)
        {
            Notifications notifications = getNotificationsService();
            notification = notifications.get(id);
        }

        if(notification != null) {
            TextView txt = findViewById(R.id.txtNotification);
            txt.setText(notification.getText());
        }
        else {
            finish();
        }
    }

    @Nullable
    private String getBundleId() {
        Intent intent = getIntent();
        if(intent != null && intent.getAction() != null && intent.getAction().contains(Constant.EDIT_ACTION)) {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null && bundle.containsKey(idField)) {
                return bundle.getString(idField);
            }
        }

        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(notificationsService != null)
            notificationsService.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            edit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Actions
    private void edit() {
        TextView txt = findViewById(R.id.txtNotification);

        NotificationBuilder builder = new NotificationBuilder();
        builder.setNotification(notification);
        builder.setText(txt.getText().toString());

        Notifications notifications = getNotificationsService();
        notifications.edit(builder.build());

        this.finish();
    }

    // Services
    private Notifications getNotificationsService() {
        if(notificationsService == null)
        {
            ReminderApplication app = (ReminderApplication)getApplicationContext();
            notificationsService = app.createNotifications();
        }

        return notificationsService;
    }
}
