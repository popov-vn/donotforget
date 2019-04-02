package com.elsoftware.notificationreminder.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.elsoftware.notificationreminder.R;
import com.elsoftware.notificationreminder.ui.WrappingLayoutManager;
import com.elsoftware.notificationreminder.ui.listview.AboutAppsViewAdapter;
import com.elsoftware.notificationreminder.ui.listview.ItemClick;
import com.elsoftware.notificationreminder.utils.PlayStore;

public class AboutActivity extends AppCompatActivity {

    AboutAppsViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

        if (adapter == null ) {
            adapter = new AboutAppsViewAdapter(
                    new ItemClick() {
                        @Override
                        public void OnClick(int adapterPosition, int menuId) {
                            if(menuId == R.id.action_click_item)
                                openAppLink(adapterPosition);
                        }

                        @Override
                        public void OnMenuClick(int adapterPosition, int menuId) {

                        }
                    }, this);
        }

        RecyclerView historyView = findViewById(R.id.aboutItems);
        historyView.setLayoutManager(new WrappingLayoutManager(this));
        historyView.setAdapter(adapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private void openAppLink(int position){
        AboutAppsViewAdapter.AboutAppItem item = adapter.getItem(position);
        if(item != null)
        {
            PlayStore.startPackage(item.packageId, this);
        }
    }
}
