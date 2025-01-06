package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.NotificationDto;

import java.util.List;

public class AllNotificationResponse extends BasicResponse{
    private List<NotificationDto> notificationDtos;

    public AllNotificationResponse(boolean success, String error, List<NotificationDto> notificationDtos) {
        super(success, error);
        this.notificationDtos = notificationDtos;
    }

    public AllNotificationResponse(List<NotificationDto> notificationDtos) {
        this.notificationDtos = notificationDtos;
    }

    public List<NotificationDto> getNotificationDtos() {
        return notificationDtos;
    }

    public void setNotificationDtos(List<NotificationDto> notificationDtos) {
        this.notificationDtos = notificationDtos;
    }
}
