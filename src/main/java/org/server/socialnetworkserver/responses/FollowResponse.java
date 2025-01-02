package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.FollowDto;

import java.util.List;

public class FollowResponse extends BasicResponse{
    private List<FollowDto> getAllFollowers;
    private List<FollowDto> getAllFollowing;

    public FollowResponse(boolean success, String error, List<FollowDto> getAllFollowers,List<FollowDto> getAllFollowing) {
        super(success, error);
        this.getAllFollowers = getAllFollowers;
        this.getAllFollowing = getAllFollowing;
    }

    public FollowResponse(List<FollowDto> getAllFollowers) {
        this.getAllFollowers = getAllFollowers;
    }

    public List<FollowDto> getGetAllFollowers() {
        return getAllFollowers;
    }

    public void setGetAllFollowers(List<FollowDto> getAllFollowers) {
        this.getAllFollowers = getAllFollowers;
    }

    public List<FollowDto> getGetAllFollowing() {
        return getAllFollowing;
    }

    public void setGetAllFollowing(List<FollowDto> getAllFollowing) {
        this.getAllFollowing = getAllFollowing;
    }

    @Override
    public String toString() {
        return "FollowResponse{" +
                "getAllFollowers=" + getAllFollowers +
                ", getAllFollowing=" + getAllFollowing +
                '}';
    }
}
