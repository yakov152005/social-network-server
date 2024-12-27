package org.server.socialnetworkserver.responses;

import java.util.List;

public class UserNamesResponse extends BasicResponse{
    private List<String> usernames;

    public UserNamesResponse(boolean success,String error,List<String> usernames){
        super(success,error);
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
