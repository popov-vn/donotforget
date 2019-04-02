package com.elsoftware.notificationreminder.ui.listview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elsoftware.notificationreminder.R;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public class NotificationViewAdapter extends BaseNotificationViewAdapter {
    public NotificationViewAdapter(ItemClick onClick)
    {
        super(onClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false), this.clickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder)
            ((ItemViewHolder) holder).populize(getItem(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate;
        private TextView txtText;

        public ItemViewHolder(View v, final ItemClick click) {
            super(v);

            this.txtDate = v.findViewById(R.id.txtDate);
            this.txtText = v.findViewById(R.id.txtText);
            Button btnDone = v.findViewById(R.id.btnDone);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.OnClick(getAdapterPosition(), R.id.action_done);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.OnClick(getAdapterPosition(), R.id.action_click_item);
                }
            });
        }

        public void populize(NotificationViewItem notification)
        {
            txtDate.setText(notification.getDate());
            txtText.setText(notification.getText());
        }
    }
}
