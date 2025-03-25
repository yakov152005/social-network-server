package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsFollowDto {
    private boolean isFollowing;

    public IsFollowDto(Boolean isFollowing){
        this.isFollowing = isFollowing != null && isFollowing;
    }

    public IsFollowDto(){

    }
}
