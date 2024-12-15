package org.server.socialnetworkserver.utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;


import static org.server.socialnetworkserver.utils.Constants.SmsConstants.*;
import static org.server.socialnetworkserver.utils.HelpMethods.*;


public class ApiUtils {
    public static void main(String[] args) {
        String verify = generatorCode();
        System.out.println( sendSms(verify,
                List.of("0526650754")));
    }

    public static boolean sendSms (String text, List<String> phones) {
        String token = SMS_TOKEN;
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
        settingsJson.put("Sender", "NETWORKAPI");
        jsonObject.put("Settings", settingsJson);
        System.out.println(jsonObject);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        httpHeaders.add("Authorization", "Basic " + token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(jsonObject.toString(), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(URL_SMS, HttpMethod.POST, httpEntity, String.class);
        return true;

    }
}