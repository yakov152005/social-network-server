package org.server.socialnetworkserver.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static org.server.socialnetworkserver.utils.Constants.ForTest.URL_TEST;

public class LoadTest {
    public static void main(String[] args) throws Exception {
        AtomicInteger count503 = new AtomicInteger();
        AtomicInteger count200 = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    URL url = new URL(URL_TEST);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    System.out.println("Response Code: " + conn.getResponseCode());
                    System.out.println("Count code 200:" + count200);
                    System.out.println("Count code 503:" + count503);
                    if (conn.getResponseCode() == 200){
                        count200.addAndGet(1);
                    }else if (conn.getResponseCode() == 503){
                        count503.addAndGet(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("Count code 200:" + count200);
        System.out.println("Count code 503:" + count503);
    }
}
