package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.History;
import com.elsoftware.notificationreminder.model.HistoryRepository;
import com.elsoftware.notificationreminder.model.Notification;

import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class HistoryImpl implements History {

    private HistoryRepository repository;

    public HistoryImpl(HistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(Notification notification) {
        this.repository.add(notification);
    }

    @Override
    public void remove(String id) {
        this.repository.remove(id);
    }

    @Override
    public void removeAll() {
        this.repository.removeAll();
    }

    @Override
    public List<Notification> getAll() {
        return this.repository.getAll();
    }

    @Override
    public void close() {
        repository.close();
    }
}
