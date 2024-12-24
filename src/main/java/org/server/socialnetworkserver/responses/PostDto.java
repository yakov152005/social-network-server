package org.server.socialnetworkserver.responses;

import java.util.Date;

public class PostDto {
    private String username;
    private String content;
    private String imageUrl;
    private Date date;

    public PostDto(String username, String content, String imageUrl, Date date) {
        this.username = username;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
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



