package org.server.socialnetworkserver.dtos;

import lombok.Data;


@Data
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
    }
}
