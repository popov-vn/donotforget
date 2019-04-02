package com.elsoftware.notificationreminder.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elsoftware.notificationreminder.R;
import com.elsoftware.notificationreminder.app.Constant;
import com.elsoftware.notificationreminder.app.ReminderApplication;
import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.Notifications;
import com.elsoftware.notificationreminder.service.JobService;
import com.elsoftware.notificationreminder.ui.WrappingLayoutManager;
import com.elsoftware.notificationreminder.ui.listview.ItemClick;
import com.elsoftware.notificationreminder.ui.listview.NotificationViewAdapter;
import com.elsoftware.notificationreminder.ui.listview.NotificationViewItem;
import com.elsoftware.notificationreminder.utils.DateString;
import com.elsoftware.notificationreminder.utils.PlayStore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotificationViewAdapter adapter;

    final Handler handler = new Handler();
    final Runnable loadData = new Runnable() {
        public void run() {
            updateData();
        }
    };

    final List<NotificationViewItem> data = new ArrayList<>();
    Notifications notificationsService;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction() != null && intent.getAction().compareTo(Constant.ON_DONE) == 0) {
                updateUi();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton addReminderBtn = findViewById(R.id.btnAddReminder);
        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNotification();
            }
        });

        Button clearActive = findViewById(R.id.btnClearActive);
        clearActive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                allDone();
            }
        });

        View emptyContainer = findViewById(R.id.txtEmpty);
        emptyContainer.setVisibility(View.GONE);

        registerReceiver(broadcastReceiver, new IntentFilter(Constant.ON_DONE));

        if(adapter == null)
        {
            adapter = new NotificationViewAdapter(
                    new ItemClick() {
                        @Override
                        public void OnClick(int adapterPosition, int menuId) {
                            if(menuId == R.id.action_done) {
                                completeNotification(adapterPosition);
                            } else if (menuId == R.id.action_click_item) {
                                editNotification(adapterPosition);
                            }
                        }

                        @Override
                        public void OnMenuClick(int adapterPosition, int menuId) {
                        }
                    }
            );
        }

        RecyclerView activeView = findViewById(R.id.activeItems);
        activeView.setLayoutManager(new WrappingLayoutManager(this));
        activeView.setAdapter(adapter);

        restoreNotifications();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if(notificationsService != null)
            notificationsService.close();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            startHistory();
            return true;
        }
        else if (id == R.id.action_about) {
            startAbout();
            return true;
        }
        else if(id == R.id.action_rate_app) {
            startRateApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Activities
    private void startHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void startAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void startRateApp() {
        PlayStore.startCurrent(this);
    }

    // Actions
    private void completeNotification(int pos)
    {
        NotificationViewItem item = adapter.getItem(pos);
        if(item != null)
        {
            Notifications notifications = getNotifications();
            notifications.done(item.getId());

            updateUi();
        }
    }

    private void editNotification(int pos)
    {
        NotificationViewItem item = adapter.getItem(pos);
        if(item != null)
        {
            startActivity(EditActivity.createStart(this, item.getId()));
        }
    }


    private void allDone()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getApplicationContext().getString(R.string.are_you_sure_all_done))
                .setPositiveButton(getApplicationContext().getString(R.string.all_done), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Notifications notifications = getNotifications();
                        notifications.doneAll();
                        updateUi();
                    }
                })
                .setNegativeButton(getApplicationContext().getString(R.string.cancel), null)
                .show();
    }

    private void addNotification()
    {
        String text = "";

        EditText editText = findViewById(R.id.newNotification);
        if (editText != null) {
            text = editText.getText().toString();
            editText.setText("");
        }

        if(text.isEmpty())
        {
            showToast(getApplicationContext().getString(R.string.enter_some_text));
            return;
        }

        NotificationBuilder builder = new NotificationBuilder();
        builder.setText(text);

        Notifications notifications = getNotifications();
        notifications.add(builder.build());

        updateUi();
    }

    void restoreNotifications() {
        Intent work = new Intent();
        work.setAction(Constant.RESTORE_JOB);

        JobIntentService.enqueueWork(this, JobService.class, Constant.JOB_ID, work);
    }


    // Services
    private Notifications getNotifications() {
        if(notificationsService == null)
        {
            ReminderApplication app = (ReminderApplication)getApplicationContext();
            notificationsService = app.createNotifications();
        }

        return notificationsService;
    }

    // Ui
    private void updateUi()
    {
        Thread t = new Thread() {
            public void run() {
                loadActive();
                handler.post(loadData);
            }
        };
        t.start();
    }

    private void loadActive()
    {
        Notifications notifications = getNotifications();
        List<Notification> items = notifications.getAll();

        synchronized (data) {
            data.clear();
            for (Notification item : items) {
                NotificationViewItem viewItem = new NotificationViewItem(item.getId(), new DateString(item.getDate(), getApplicationContext()).toString(), item.getText());
                data.add(viewItem);
            }
        }
    }

    private void updateData() {
        List<NotificationViewItem> items;

        synchronized (data)
        {
            items = new ArrayList<>(data);
        }

        showHideData(items, adapter);
    }

    private void showHideData(List<NotificationViewItem> items, NotificationViewAdapter adapter)
    {
        if(items != null) {
            View container = findViewById(R.id.activeContainer);
            View emptyContainer = findViewById(R.id.txtEmpty);

            if(items.size() > 1)
                container.setVisibility(View.VISIBLE);
            else
                container.setVisibility(View.GONE);

            if(items.isEmpty())
                emptyContainer.setVisibility(View.VISIBLE);
            else
                emptyContainer.setVisibility(View.GONE);

            adapter.updateItems(items);
            adapter.notifyDataSetChanged();
        }
    }

    private void showToast(String text)
    {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
