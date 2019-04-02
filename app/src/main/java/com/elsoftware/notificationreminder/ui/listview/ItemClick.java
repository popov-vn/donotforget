package com.elsoftware.notificationreminder.ui.listview;

/**
 * Created by popovich on 16.08.2016.
 * eL Software Company, 2017
 */

public interface ItemClick
{
     void OnClick(int adapterPosition, int menuId);

     void OnMenuClick(int adapterPosition, int menuId);
}
