package org.server.socialnetworkserver.dtos;
import java.util.List;

public class ProfileDto{
    private String username;
    private String profilePicture;
    private int followers;
    private int following;
    private boolean isFollowing;
    private List<PostDto> posts;

    public ProfileDto(String username, String profilePicture, int followers, int following, boolean isFollowing, List<PostDto> posts) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.followers = followers;
        this.following = following;
        this.isFollowing = isFollowing;
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public boolean getIsFollowing() {return isFollowing;}

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
