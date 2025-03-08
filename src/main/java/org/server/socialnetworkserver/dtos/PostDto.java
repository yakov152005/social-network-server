package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostDto {
    private long id;
    private String username;
    private String profilePicture;
    private String content;
    private String imageUrl;
    private Date date;
    boolean isLikedByUser;
    private int likesCount;
    private int commentCount;
    private List<LikeDto> likes;

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

    public PostDto(long id,String username, String profilePicture,String content, String imageUrl, Date date, boolean isLikedByUser, int likesCount,int commentCount) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.isLikedByUser = isLikedByUser;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
    }

    public PostDto(long id, String username, String profilePicture, String content, String imageUrl, Date date, boolean isLikedByUser, int likesCount, int commentCount, List<LikeDto> likes) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.isLikedByUser = isLikedByUser;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
        this.likes = likes;
    }

}



