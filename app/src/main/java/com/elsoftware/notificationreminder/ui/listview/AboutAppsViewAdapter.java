package com.elsoftware.notificationreminder.ui.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elsoftware.notificationreminder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 14.12.2017.
 * eL Software Company, 2017
 */

public class AboutAppsViewAdapter extends RecyclerView.Adapter{
    private List<AboutAppItem> dataset = new ArrayList<>();
    ItemClick clickListener;

    public AboutAppsViewAdapter(ItemClick onClick, Context context)
    {
        this.clickListener = onClick;
        dataset.add(new AboutAppItem(
                context.getString(R.string.loancalculator_name),
                context.getString(R.string.loancalculator_description),
                context.getString(R.string.loancalculator_id),
                R.drawable.ic_loancalculatopr)
        );
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_about, parent, false), this.clickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder)
            ((ItemViewHolder) holder).populize(getItem(position));
    }


    @Override
    synchronized public int getItemCount()
    {
        return dataset.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    synchronized public AboutAppItem getItem(int position)
    {
        if(dataset.size() > position)
            return dataset.get(position);

        return null;
    }

    public class AboutAppItem {
        AboutAppItem(String packageName, String packageDescription, String packageId, int packageIconId){
            this.packageName = packageName;
            this.packageDescription = packageDescription;
            this.packageIconId = packageIconId;
            this.packageId = packageId;
        }

        public String packageName;
        public String packageDescription;
        public String packageId;
        public int packageIconId;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtDescription;
        private ImageView imgIcon;

        public ItemViewHolder(View v, final ItemClick click) {
            super(v);

            this.txtName = v.findViewById(R.id.txtName);
            this.txtDescription = v.findViewById(R.id.txtDescription);
            this.imgIcon = v.findViewById(R.id.imgIcon);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.OnClick(getAdapterPosition(), R.id.action_click_item);
                }
            });
        }

        public void populize(AboutAppItem item)
        {
            txtName.setText(item.packageName);
            txtDescription.setText(item.packageDescription);
            imgIcon.setImageResource(item.packageIconId);
        }
    }
}
