package org.server.socialnetworkserver.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController()
@RequestMapping("/sse")
public class StreamController {
    private List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();

    @GetMapping("/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterList.add(emitter);

        emitter.onCompletion(() -> {
            emitterList.remove(emitter);
        });

        emitter.onError((throwable -> {
            emitterList.remove(emitter);
        }));

        emitter.onTimeout(() -> {
            emitterList.remove(emitter);
        });

        return emitter;
    }

    private void sentTestMessage() {
        Date date = new Date();
        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data("Current server time: " + date));
            } catch (IOException e) {
                emitterList.remove(emitter);
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
            sentTestMessage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("זיהה ריענון שרת/יציאה מהשרת");
            }
        }
    }
}
