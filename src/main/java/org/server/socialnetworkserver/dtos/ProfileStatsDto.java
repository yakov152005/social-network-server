package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStatsDto {
    private int followersCount;
    private int followingCount;
    private boolean isFollowing;

    public ProfileStatsDto(int followersCount, int followingCount, boolean isFollowing) {
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.isFollowing = isFollowing;
    }

    public ProfileStatsDto() {
        this.followersCount = 0;
        this.followingCount = 0;
        this.isFollowing = false;
    }
}
