package org.server.socialnetworkserver.utils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import static org.server.socialnetworkserver.utils.Constants.ChatGpt.*;


public class ApiChatGpt {

    private static final CloseableHttpClient client = HttpClients.createDefault();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws URISyntaxException, IOException {
        String res = getResponseFromServer("hey");
        System.out.println(res);
    }

    public static String getResponseFromServer(String conversation) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(SEND_MESSAGE)
                .setParameter("id", ID_GPT)
                .setParameter("text", conversation)
                .build();


        HttpGet get = new HttpGet(uri);

        try (var response = client.execute(get)) {
            String myResponse = EntityUtils.toString(response.getEntity());


            System.out.println("Response from server: " + myResponse);

            if (!myResponse.isEmpty()) {
                try {
                    Response responseObj = mapper.readValue(myResponse, Response.class);
                    if (responseObj.isSuccess()) {
                        return responseObj.getExtra();
                    } else {
                        errorCode(responseObj);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse JSON response: " + e.getMessage());
                }
            } else {
                System.err.println("The message was not sent.");
            }
        }
        return null;
    }


    private static void errorCode(Response responseObj) {
        Integer errorCode = responseObj.getErrorCode();
        ErrorOption e = ErrorOption.fromCode(errorCode);

        if (e != null) {
            StringBuilder sb = new StringBuilder();
            switch (e) {
                case E_3_0, E_3_1, E_3_2, E_3_3, E_3_5 ->
                        sb.append(e.getCode()).append(System.lineSeparator()).append(e.getMessage());
                default -> System.out.println("Unhandled error code: " + responseObj.getErrorCode());
            }
            System.out.println(sb);
        } else {
            System.out.println("Unhandled error code: " + responseObj.getErrorCode());
        }

    }


    @Getter
    static class Response {
        private boolean success;
        private String extra;
        private String errorMessage;
        private int errorCode;

    }

    @Getter
    enum ErrorOption {
        E_3_0(3000, "The id is not sent."),
        E_3_1(3001, "This id is not found!"),
        E_3_2(3002, "The quota of requests for this identity card is over.."),
        E_3_3(3003, "No text message will be sent."),
        E_3_5(3005, "General error.");

        private final int code;
        private final String message;

        ErrorOption(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static ErrorOption fromCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (ErrorOption error : values()) {
                if (error.code == code) {
                    return error;
                }
            }
            return null;
        }
    }
}
