package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineFriendsDto {
    private long id;
    private String username;
    private String profilePicture;


    public OnlineFriendsDto(long id, String username, String profilePicture) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public OnlineFriendsDto(){

    }
}
