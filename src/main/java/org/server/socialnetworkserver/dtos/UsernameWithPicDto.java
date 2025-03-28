package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameWithPicDto {
    private String username;
    private String profilePicture;
    private String bio;

    public UsernameWithPicDto(String username, String profilePicture,String bio) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }

}

