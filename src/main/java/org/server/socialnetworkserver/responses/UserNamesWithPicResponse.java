package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dto.UsernameWithPicDTO;

import java.util.List;

public class UserNamesWithPicResponse extends BasicResponse{
    private List<UsernameWithPicDTO> usernameWithPicDTOS;

    public UserNamesWithPicResponse(boolean success,String error,List<UsernameWithPicDTO> usernameWithPicDTOS){
        super(success,error);
        this.usernameWithPicDTOS = usernameWithPicDTOS;
    }

    public List<UsernameWithPicDTO> getUsernameWithPicDTOS() {
        return usernameWithPicDTOS;
    }

    public void setUsernameWithPicDTOS(List<UsernameWithPicDTO> usernameWithPicDTOS) {
        this.usernameWithPicDTOS = usernameWithPicDTOS;
    }
}
