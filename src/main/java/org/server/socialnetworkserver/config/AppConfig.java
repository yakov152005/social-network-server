package org.server.socialnetworkserver.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppConfig {

    @Value("${SENDER_EMAIL}")
    private String senderEmail;

    @Value("${SENDER_PASSWORD}")
    private String senderPassword;

    @Value("${SMS_TOKEN}")
    private String smsToken;

    @Value("${URL_TEST}")
    private String urlTest;

    @Value("${ID_GPT}")
    private String idGpt;

    @Value("${PATH_SEND_MESSAGE}")
    private String sendMessage;

    @PostConstruct
    public void printConfigValues() {
        System.out.println("=============== AppConfig Loaded ===============");
        System.out.println("SENDER_EMAIL: " + senderEmail);
        System.out.println("SENDER_PASSWORD: " + maskSensitiveData(senderPassword));
        System.out.println("SMS_TOKEN: " + maskSensitiveData(smsToken));
        System.out.println("URL_TEST: " + urlTest);
        System.out.println("ID_GPT: " + idGpt);
        System.out.println("PATH_SEND_MESSAGE: " + sendMessage);
        System.out.println("===============================================");
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.length() < 4) {
            return "****";
        }
        return data.substring(0, 2) + "****" + data.substring(data.length() - 2);
    }
}
