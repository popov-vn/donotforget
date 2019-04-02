package com.elsoftware.notificationreminder.core;

import com.elsoftware.notificationreminder.model.NotificationsRepository;
import com.elsoftware.notificationreminder.model.HistoryRepository;
import com.elsoftware.notificationreminder.model.IdProvider;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.model.Notifications;
import com.elsoftware.notificationreminder.model.NotificationsObservable;
import com.elsoftware.notificationreminder.model.NotificationsObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popovich on 11.12.2017.
 * eL Software Company, 2017
 */

public class NotificationsImpl implements Notifications, NotificationsObservable {

    private NotificationsRepository notificationsRepository;
    private HistoryRepository historyRepository;
    private NotificationManager notificationManager;
    private IdProvider idProvider;
    private final List<NotificationsObserver> observers;

    public NotificationsImpl(NotificationsRepository notificationsRepository, HistoryRepository historyRepository, NotificationManager notificationManager, IdProvider idProvider)
    {
        this.notificationsRepository = notificationsRepository;
        this.historyRepository = historyRepository;
        this.notificationManager = notificationManager;
        this.idProvider = idProvider;
        this.observers = new ArrayList<>();
    }

    @Override
    public void close() {
        this.notificationsRepository.close();
        this.historyRepository.close();
    }

    @Override
    public void add(Notification notification) {
        Notification updatedNotification = notification;

        if(updatedNotification.getNotifyId() == 0)
        {
            NotificationBuilder builder = new NotificationBuilder();
            builder.setNotification(notification);
            builder.setNotifyId(idProvider.getNext());
            updatedNotification = builder.build();
        }

        notificationsRepository.add(updatedNotification);
        historyRepository.add(updatedNotification);
        notificationManager.add(updatedNotification);
        processObservers(new RunObserver() {
            @Override
            public void run(NotificationsObserver observer) {
                observer.onCreate();
            }
        });
    }

    @Override
    public void edit(Notification notification) {
        historyRepository.add(notification);
        notificationsRepository.update(notification);
        notificationManager.add(notification);
        processObservers(new RunObserver() {
            @Override
            public void run(NotificationsObserver observer) {
                observer.onEdit();
            }
        });
    }

    @Override
    public void done(String id) {
        int notifyId = this.notificationsRepository.remove(id);
        notificationManager.done(notifyId);
        processObservers(new RunObserver() {
            @Override
            public void run(NotificationsObserver observer) {
                observer.onDone();
            }
        });
    }

    @Override
    public void doneAll() {
        this.notificationsRepository.removeAll();
        notificationManager.doneAll();
        processObservers(new RunObserver() {
            @Override
            public void run(NotificationsObserver observer) {
                observer.onDone();
            }
        });
    }

    @Override
    public List<Notification> getAll() {
        return notificationsRepository.getAll();
    }

    @Override
    public Notification get(String id) {
        return notificationsRepository.get(id);
    }

    @Override
    synchronized public void addObserver(NotificationsObserver observer) {
        this.observers.add(observer);
    }

    private void processObservers(RunObserver runnable)
    {
        List<NotificationsObserver> observersCopy;
        synchronized (observers){
            observersCopy = new ArrayList<>(observers);
        }

        for(NotificationsObserver observer : observersCopy) {
            runnable.run(observer);
        }
    }

    private interface RunObserver
    {
        void run(NotificationsObserver observer);
    }

}
