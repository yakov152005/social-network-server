package org.server.socialnetworkserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.server.socialnetworkserver.entitys.Like;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDto {
    private long id;
    private Long postId;
    private String username;
    private String content;
    private List<Like> likes = new ArrayList<>();
    private Date date;
    private String profilePicture;


    public CommentDto(long id,Long postId, String username, String content, Date date,String profilePicture) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.date = date;
        this.profilePicture = profilePicture;
    }

    public CommentDto(Long postId, String username, String content) {
        this.postId = postId;
        this.username = username;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
