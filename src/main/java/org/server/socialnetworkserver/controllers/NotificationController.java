package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.repositoris.NotificationRepository;
import org.server.socialnetworkserver.responses.AllNotificationResponse;
import org.server.socialnetworkserver.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class NotificationController {

    private final NotificationService notificationService;
    private final Map<String, SseEmitter> emitters;

    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
        this.emitters =  new ConcurrentHashMap<>();;
    }


    @GetMapping("/notifications/connect/{username}")
    public SseEmitter connect(@PathVariable String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(username, emitter);

        emitter.onCompletion(() -> emitters.remove(username));
        emitter.onError((throwable) -> emitters.remove(username));
        emitter.onTimeout(() -> emitters.remove(username));

        return emitter;
    }

    public void sendNotification(String username, NotificationDto notificationDto) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notificationDto, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitters.remove(username);
            }
        }
    }


    @GetMapping("/get-all-notification/{username}")
    public AllNotificationResponse getAllNotification(@PathVariable String username){
        return notificationService.getAllNotification(username);
    }
}
