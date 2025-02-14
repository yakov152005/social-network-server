package org.server.socialnetworkserver.dtos;


import lombok.Setter;


@Setter
public class ProfileStatsDto {
    private Long followersCount;
    private Long followingCount;
    private Boolean isFollowing;

    public ProfileStatsDto(Long followersCount, Long followingCount, Boolean isFollowing) {
        this.followersCount = (followersCount != null) ? followersCount : 0;
        this.followingCount = (followingCount != null) ? followingCount : 0;
        this.isFollowing = (isFollowing != null) ? isFollowing : false;
    }

    public ProfileStatsDto() {}

    public Long getFollowersCount() {
        return followersCount;
    }

    public Long getFollowingCount() {
        return followingCount;
    }

    public Boolean isFollowing() {
        return isFollowing;
    }

}
