package org.server.socialnetworkserver.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class TestController {

    @GetMapping("/slow-endpoint")
    public CompletableFuture<ResponseEntity<String>> slowEndpoint() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return ResponseEntity.ok("Response after delay");
            } catch (InterruptedException e) {
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Timeout");
            }
        });
    }
}





