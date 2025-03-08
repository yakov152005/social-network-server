package org.server.socialnetworkserver.utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.server.socialnetworkserver.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.server.socialnetworkserver.utils.Constants.SmsConstants.*;

@Service
public class ApiSmsSender {

    private final AppConfig appConfig;


    @Autowired
    public ApiSmsSender(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void sendSms(String text, List<String> phones) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message", text);
        JSONArray recipients = new JSONArray();

        for (String phone : phones) {
            JSONObject recipient = new JSONObject();
            recipient.put("Phone", phone);
            recipients.put(recipient);
        }

        jsonObject.put("Recipients", recipients);
        JSONObject settingsJson = new JSONObject();
        settingsJson.put("Sender", SMS_SENDER);
        jsonObject.put("Settings", settingsJson);

        System.out.println(jsonObject);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Basic " + appConfig.getSmsToken());
        System.out.println(appConfig.getSmsToken());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(jsonObject.toString(), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(URL_SMS, HttpMethod.POST, httpEntity, String.class);
        System.out.println(response);
    }


}