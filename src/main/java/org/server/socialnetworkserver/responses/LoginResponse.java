package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LoginResponse extends BasicResponse {
    private String username;
    private Map<String, String> result;

    public LoginResponse(boolean success, String error, String username) {
        super(success, error);
        this.username = username;
    }

    public LoginResponse(boolean success, String error, String username, Map<String, String> result) {
        super(success, error);
        this.username = username;
        this.result = result;
    }


    public LoginResponse() {
        super();
    }


    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + isSuccess() +
                ", error='" + getError() + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
