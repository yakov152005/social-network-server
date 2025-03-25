package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuggestedFriendsDto {
    private long id;
    private String name;
    private String profilePicture;
    private boolean followedBy;

    public SuggestedFriendsDto(long id, String name, String profilePicture, boolean followedBy) {
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
        this.followedBy = followedBy;
    }

    public SuggestedFriendsDto(long id, String name, String profilePicture) {
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public SuggestedFriendsDto(){

    }
}
