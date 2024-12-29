package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.UsernameWithPicDto;

import java.util.List;

public class UserNamesWithPicResponse extends BasicResponse{
    private List<UsernameWithPicDto> usernameWithPicDtos;

    public UserNamesWithPicResponse(boolean success,String error,List<UsernameWithPicDto> usernameWithPicDtos){
        super(success,error);
        this.usernameWithPicDtos = usernameWithPicDtos;
    }

    public List<UsernameWithPicDto> getUsernameWithPicDTOS() {
        return usernameWithPicDtos;
    }

    public void setUsernameWithPicDTOS(List<UsernameWithPicDto> usernameWithPicDtos) {
        this.usernameWithPicDtos = usernameWithPicDtos;
    }
}
