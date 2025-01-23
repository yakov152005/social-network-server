package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse extends BasicResponse{
    private boolean isValid;
    private String username;

    public TokenResponse(boolean success, String error, boolean isValid,String username) {
        super(success, error);
        this.isValid = isValid;
        this.username = username;
    }

}