package org.server.socialnetworkserver.dtos;

import java.util.Date;

public class PostDto {
    private long id;
    private String username;
    private String profilePicture;
    private String content;
    private String imageUrl;
    private Date date;
    boolean isLikedByUser;
    private int likesCount;

    public PostDto(long id,String username, String profilePicture,String content, String imageUrl, Date date, boolean isLikedByUser, int likesCount) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.isLikedByUser = isLikedByUser;
        this.likesCount = likesCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setData(Date date) {
        this.date = date;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", date=" + date +
                '}';
    }
}



