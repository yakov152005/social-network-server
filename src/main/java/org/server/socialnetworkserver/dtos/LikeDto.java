package org.server.socialnetworkserver.dtos;

import org.server.socialnetworkserver.entitys.Like;

import java.util.ArrayList;
import java.util.List;

public class LikeDto {
    private long id;
    private Long postId;
    private String username;
    private String profilePicture;


    public LikeDto(long id, Long postId, String username, String profilePicture) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

}
