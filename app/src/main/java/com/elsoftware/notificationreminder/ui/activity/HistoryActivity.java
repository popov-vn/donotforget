package com.elsoftware.notificationreminder.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.elsoftware.notificationreminder.R;
import com.elsoftware.notificationreminder.app.ReminderApplication;
import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.model.History;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.Notifications;
import com.elsoftware.notificationreminder.ui.WrappingLayoutManager;
import com.elsoftware.notificationreminder.ui.listview.HistoryViewAdapter;
import com.elsoftware.notificationreminder.ui.listview.ItemClick;
import com.elsoftware.notificationreminder.ui.listview.NotificationViewItem;
import com.elsoftware.notificationreminder.utils.DateString;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    final List<NotificationViewItem> data = new ArrayList<>();
    HistoryViewAdapter historyAdapter;
    History historyService;
    Notifications notificationsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View emptyContainer = findViewById(R.id.txtEmpty);
        emptyContainer.setVisibility(View.GONE);

        historyService = ((ReminderApplication)getApplicationContext()).createHistory();
        if (historyAdapter == null ) {
            historyAdapter = new HistoryViewAdapter(
                    new ItemClick() {
                        @Override
                        public void OnClick(int adapterPosition, int menuId) {
                            if(menuId == R.id.action_repeat)
                                repeatNotification(adapterPosition);
                            else if(menuId == R.id.action_remove)
                                removeNotification(adapterPosition);
                        }

                        @Override
                        public void OnMenuClick(int adapterPosition, int menuId) {

                        }
                    });
        }

        RecyclerView historyView = findViewById(R.id.historyItems);
        historyView.setLayoutManager(new WrappingLayoutManager(this));
        historyView.setAdapter(historyAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUi();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(notificationsService != null)
            notificationsService.close();
        if(historyService != null)
            historyService.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_all) {
            clearAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateHistory() {

        List<NotificationViewItem> items;

        synchronized (data)
        {
            items = new ArrayList<>(data);
        }

        View emptyContainer = findViewById(R.id.txtEmpty);
        if(items.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            emptyContainer.setVisibility(View.GONE);
        }

        historyAdapter.updateItems(items);
        historyAdapter.notifyDataSetChanged();
    }

    private void repeatNotification(int pos) {
        NotificationViewItem item = historyAdapter.getItem(pos);
        if(item != null)
        {
            NotificationBuilder builder = new NotificationBuilder();
            builder.setText(item.getText());

            Notifications notifications = getNotifications();
            notifications.add(builder.build());

            updateUi();
        }
    }

    private void removeNotification(int pos) {
        NotificationViewItem item = historyAdapter.getItem(pos);
        if(item != null)
        {
            historyService.remove(item.getId());
            updateUi();
        }
    }

    private void clearAll() {
        historyService.removeAll();
        updateUi();
    }

    private void updateUi() {
        Thread t = new Thread() {
            public void run() {
                List<Notification> notificationList = historyService.getAll();
                synchronized (data) {
                    data.clear();
                    for (Notification notification : notificationList) {
                        String date = new DateString(notification.getDate(), getApplicationContext()).toString();
                        data.add(new NotificationViewItem(notification.getId(), date, notification.getText()));
                    }
                }

                handler.post(new Runnable() {
                    public void run() {
                        updateHistory();
                    }
                });
            }
        };
        t.start();
    }

    private Notifications getNotifications() {
        if(notificationsService == null)
            notificationsService = ((ReminderApplication)getApplicationContext()).createNotifications();

        return notificationsService;
    }

}
