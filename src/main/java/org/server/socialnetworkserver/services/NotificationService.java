package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.NotificationRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.AllNotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class NotificationService {

    public final NotificationRepository notificationRepository;
    public final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,UserRepository userRepository){
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    public AllNotificationResponse getAllNotification(@PathVariable String username){
        User user = userRepository.findByUsername(username);

        if (user == null){
            return new AllNotificationResponse(false,"User is not found.",null);
        }

        List<NotificationDto> notificationDto= notificationRepository.findUnreadNotifications(username);
        return new AllNotificationResponse(true,"All notification send.",notificationDto);
    }
}
