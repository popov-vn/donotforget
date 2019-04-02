package com.elsoftware.notificationreminder.ui.listview;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public abstract class BaseNotificationViewAdapter extends RecyclerView.Adapter{
    private final List<NotificationViewItem> dataset = new ArrayList<>();
    ItemClick clickListener;

    BaseNotificationViewAdapter(ItemClick onClick)
    {
        this.clickListener = onClick;
    }

    synchronized public void updateItems(List<NotificationViewItem> items)
    {
        dataset.clear();
        dataset.addAll(items);
    }

    @Override
    public int getItemCount()
    {
        synchronized (dataset) {
            return dataset.size();
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    synchronized public NotificationViewItem getItem(int position)
    {
        if(dataset.size() > position && position >= 0)
            return dataset.get(position);

        return null;
    }
}
