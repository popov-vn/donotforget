package com.elsoftware.notificationreminder;

import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.core.NotificationsImpl;
import com.elsoftware.notificationreminder.model.HistoryRepository;
import com.elsoftware.notificationreminder.model.IdProvider;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.model.NotificationsObserver;
import com.elsoftware.notificationreminder.model.NotificationsRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

@RunWith(MockitoJUnitRunner.class)
public class NotificationsUnitTest {

    @Mock
    NotificationsRepository notificationsRepository;

    @Mock
    NotificationManager notificationManager;

    @Mock
    HistoryRepository historyRepository;

    @Mock
    IdProvider idProvider;

    static int id = 1;
    private int getId() {
        return id++;
    }

    private Notification createNotification() {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setNotifyId(id++);

        return builder.build();
    }

    private Notification createNotificationEmpty() {
        return new NotificationBuilder().build();
    }

    @Test
    public void should_add_history_and_manager_and_active() throws Exception {
        // Arrange
        Notification notification = createNotification();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);

        // Act
        impl.add(notification);

        // Assert
        verify(notificationsRepository, times(1)).add(Mockito.any(Notification.class));
        verify(notificationManager, times(1)).add(Mockito.any(Notification.class));
        verify(historyRepository, times(1)).add(Mockito.any(Notification.class));
    }

    @Test
    public void should_edit_history_and_manager_and_active() throws Exception {
        // Arrange
        Notification notification = createNotification();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);

        // Act
        impl.edit(notification);

        // Assert
        verify(notificationsRepository, times(1)).update(Mockito.any(Notification.class));
        verify(notificationManager, times(1)).add(Mockito.any(Notification.class));
        verify(historyRepository, times(1)).add(Mockito.any(Notification.class));
    }

    @Test
    public void should_done_manager_and_active() throws Exception {
        // Arrange
        Notification notification = createNotification();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);

        // Act
        impl.done(Mockito.anyString());

        // Assert
        verify(notificationsRepository, times(1)).remove(Mockito.anyString());
        verify(notificationManager, times(1)).done(Mockito.anyInt());
    }

    @Test
    public void should_done_all_manager_and_active() throws Exception {
        // Arrange
        Notification notification = createNotification();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);

        // Act
        impl.doneAll();

        // Assert
        verify(notificationsRepository, times(1)).removeAll();
        verify(notificationManager, times(1)).doneAll();
    }

    @Test
    public void should_add_and_fill_notify_id() throws Exception {
        // Arrange
        Notification notification = createNotificationEmpty();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);
        id = 1;

        final Notification [] updated = new Notification[1];
        when(idProvider.getNext()).thenReturn(getId());
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Notification notification = invocation.getArgumentAt(0, Notification.class);
                updated[0] = notification;
                return null;
            }}).when(notificationsRepository).add(Mockito.any(Notification.class));

        // Act
        impl.add(notification);

        // Assert
        verify(notificationsRepository, times(1)).add(Mockito.any(Notification.class));
        assertEquals(1, updated[0].getNotifyId());
    }

    @Test
    public void should_notify_changes() throws Exception {
        // Arrange
        Notification notification = createNotificationEmpty();
        NotificationsImpl impl = new NotificationsImpl(notificationsRepository, historyRepository, notificationManager, idProvider);
        NotificationsObserver observer = Mockito.mock(NotificationsObserver.class);
        impl.addObserver(observer);

        final Notification [] updated = new Notification[1];
        when(idProvider.getNext()).thenReturn(getId());

        // Act
        impl.add(notification);
        impl.edit(notification);
        impl.done(notification.getId());

        // Assert
        verify(observer, times(1)).onCreate();
        verify(observer, times(1)).onDone();
        verify(observer, times(1)).onEdit();

        impl.doneAll();
        verify(observer, times(2)).onDone();
    }
}
