package org.server.socialnetworkserver.controllers;

import jakarta.annotation.PostConstruct;
import org.server.socialnetworkserver.dtos.MessageDto;
import org.server.socialnetworkserver.repositoris.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class StreamController {

    private final List<SseEmitter> dateEmitters;
    private final Map<String, List<SseEmitter>> userEmitters;
    private final MessageRepository messageRepository;

    @Autowired
    public StreamController(MessageRepository messageRepository) {
        this.userEmitters = new ConcurrentHashMap<>();
        this.dateEmitters = new CopyOnWriteArrayList<>();
        this.messageRepository = messageRepository;
    }

    @GetMapping("/stream/date")
    public SseEmitter streamDate() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        dateEmitters.add(emitter);

        emitter.onCompletion(() -> dateEmitters.remove(emitter));
        emitter.onError((throwable -> dateEmitters.remove(emitter)));
        emitter.onTimeout(() -> dateEmitters.remove(emitter));

        return emitter;
    }

    @GetMapping("/stream/user/{username}")
    public SseEmitter streamUser(@PathVariable String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.computeIfAbsent(username, key -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> userEmitters.get(username).remove(emitter));
        emitter.onError((throwable -> userEmitters.get(username).remove(emitter)));
        emitter.onTimeout(() -> userEmitters.get(username).remove(emitter));

        return emitter;
    }


    private void sentDate() {
        Date date = new Date();
        for (SseEmitter emitter : dateEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data("Current server time: " + date));
            } catch (IOException e) {
                dateEmitters.remove(emitter);
                System.out.println("זיהה ריענון שרת/יציאה מהשרת");
            }
        }
    }


    @PostConstruct
    public void initSse() {
        new Thread(this::run).start();
    }

    private void run() {
        while (true) {
            sentDate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("זיהה ריענון שרת/יציאה מהשרת");
            }
        }
    }

    public void notifyUser(String receiverUsername, MessageDto messageDto) {
        List<SseEmitter> emitters = userEmitters.get(receiverUsername);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {

                try {
                    emitter.send(SseEmitter.event()
                            .name("newMessage")
                            .data(messageDto));
                } catch (IOException e) {
                    System.out.println("Failed to send message to user: " + receiverUsername + ". Removing emitter.");
                    emitters.remove(emitter);
                }

            }
        } else {
            System.out.println("No active connection for user: " + receiverUsername);
        }
    }

}
