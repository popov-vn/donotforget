package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.IdProvider;

/**
 * Created by popovich on 12.12.2017.
 * eL Software Company, 2017
 */

public class IdProviderImpl implements IdProvider {

    private int id = 0;

    @Override
    synchronized public int getNext() {
        id++;
        return id;
    }
}
