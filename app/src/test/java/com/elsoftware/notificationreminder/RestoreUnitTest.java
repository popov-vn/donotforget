package com.elsoftware.notificationreminder;

import com.elsoftware.notificationreminder.core.LoggerEmpty;
import com.elsoftware.notificationreminder.core.NotificationBuilder;
import com.elsoftware.notificationreminder.core.RestoreImpl;
import com.elsoftware.notificationreminder.model.IdProvider;
import com.elsoftware.notificationreminder.model.Notification;
import com.elsoftware.notificationreminder.model.NotificationManager;
import com.elsoftware.notificationreminder.model.NotificationsRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by popovich on 15.12.2017.
 * eL Software Company, 2017
 */

@RunWith(MockitoJUnitRunner.class)
public class RestoreUnitTest {

    @Mock
    NotificationsRepository notificationsRepository;

    @Mock
    NotificationManager notificationManager;

    @Mock
    IdProvider idProvider;

    static int id = 0;

    private Notification createNotification() {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setNotifyId(id++);

        return builder.build();
    }

    private int getId() {
        return id++;
    }

    @Test
    public void should_clear_all_if_no_notifications() throws Exception {
        // Arrange
        List<Notification> emptyList = new ArrayList<>();
        when(notificationsRepository.getAll()).thenReturn(emptyList);
        RestoreImpl impl = new RestoreImpl(new LoggerEmpty(), notificationsRepository, notificationManager, idProvider);

        // Act
        impl.restore();

        // Assert
        verify(notificationsRepository, times(0)).updateMany(Mockito.any(List.class));
        verify(notificationManager, times(1)).doneAll();
    }

    @Test
    public void should_clear_all_if_all_notifications_gone() throws Exception {
        // Arrange
        List<Notification> activeList = new ArrayList<>();
        activeList.add(createNotification());
        activeList.add(createNotification());

        final List<Notification> updatedNotifications = new ArrayList<>();

        when(notificationsRepository.getAll()).thenReturn(activeList);
        when(idProvider.getNext()).thenReturn(getId());

        RestoreImpl impl = new RestoreImpl(new LoggerEmpty(), notificationsRepository, notificationManager, idProvider);

        // Act
        impl.restore();

        // Assert
        verify(notificationManager, times(1)).doneAll();
    }

    @Test
    public void should_add_all_if_all_notifications_gone() throws Exception {
        // Arrange
        List<Notification> activeList = new ArrayList<>();
        activeList.add(createNotification());
        activeList.add(createNotification());

        final List<Notification> updatedNotifications = new ArrayList<>();

        when(notificationsRepository.getAll()).thenReturn(activeList);
        when(idProvider.getNext()).thenReturn(getId());
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                List<Notification> notifications = invocation.getArgumentAt(0, List.class);
                updatedNotifications.addAll(notifications);
                return null;
            }}).when(notificationsRepository).updateMany(anyList());

        RestoreImpl impl = new RestoreImpl(new LoggerEmpty(), notificationsRepository, notificationManager, idProvider);

        // Act
        impl.restore();

        // Assert
        verify(notificationManager, times(2)).add(Mockito.any(Notification.class));
        assertEquals(2, updatedNotifications.size());
    }

    @Test
    public void should_nothing_if_all_notifications_exists() throws Exception {
        // Arrange
        List<Notification> activeList = new ArrayList<>();

        activeList.add(createNotification());
        activeList.add(createNotification());

        when(notificationManager.getAll()).thenReturn(activeList);
        when(notificationsRepository.getAll()).thenReturn(activeList);
        when(idProvider.getNext()).thenReturn(getId());

        RestoreImpl impl = new RestoreImpl(new LoggerEmpty(), notificationsRepository, notificationManager, idProvider);

        // Act
        impl.restore();

        // Assert
        verify(notificationManager, times(0)).add(Mockito.any(Notification.class));
        verify(notificationManager, times(0)).doneAll();
        verify(notificationsRepository, times(0)).updateMany(Mockito.any(List.class));
    }

    @Test
    public void should_add_only_gone_notifications() throws Exception {
        // Arrange
        List<Notification> activeList = new ArrayList<>();
        List<Notification> notificationManagerList = new ArrayList<>();

        Notification existNotification = createNotification();
        Notification notExist = createNotification();

        activeList.add(existNotification);
        activeList.add(notExist);

        notificationManagerList.add(existNotification);

        final List<Notification> updatedNotifications = new ArrayList<>();

        when(notificationManager.getAll()).thenReturn(notificationManagerList);
        when(notificationsRepository.getAll()).thenReturn(activeList);
        when(idProvider.getNext()).thenReturn(getId());
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                List<Notification> notifications = invocation.getArgumentAt(0, List.class);
                updatedNotifications.addAll(notifications);
                return null;
            }}).when(notificationsRepository).updateMany(anyList());

        RestoreImpl impl = new RestoreImpl(new LoggerEmpty(), notificationsRepository, notificationManager, idProvider);

        // Act
        impl.restore();

        // Assert
        verify(notificationManager, times(1)).add(Mockito.any(Notification.class));
        assertEquals(1, updatedNotifications.size());
        assertEquals(notExist .getId(), updatedNotifications.get(0).getId());
    }
}
