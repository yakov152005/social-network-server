package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStatsDto {
    private int followersCount;
    private int followingCount;
    private boolean isFollowing;


    public ProfileStatsDto(Long followersCount, Long followingCount, Boolean isFollowing) {
        this.followersCount = followersCount != null ? followersCount.intValue() : 0;
        this.followingCount = followingCount != null ? followingCount.intValue() : 0;
        this.isFollowing = isFollowing != null && isFollowing;
    }

    public ProfileStatsDto() {}
}
