package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendStatus extends OnlineFriendsDto {
    private boolean online;

    public FriendStatus(long id, String username, String profilePicture, boolean online) {
        super(id, username, profilePicture);
        this.online = online;
    }

}
