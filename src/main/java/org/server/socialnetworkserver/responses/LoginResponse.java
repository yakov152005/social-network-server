package org.server.socialnetworkserver.responses;

public class LoginResponse extends BasicResponse {
    private String username;

    public LoginResponse(boolean success, String error, String username) {
        super(success, error);
        this.username = username;
    }

    public LoginResponse() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
