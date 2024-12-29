package org.server.socialnetworkserver.responses;

public class AllFollowResponse extends BasicResponse{
    private int followers;
    private int following;

    public AllFollowResponse(boolean success, String error, int followers, int following) {
        super(success, error);
        this.followers = followers;
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
