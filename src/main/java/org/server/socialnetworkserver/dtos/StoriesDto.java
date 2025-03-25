package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StoriesDto {
    private long id;
    private String username;
    private String profilePicture;
    private String imageStories;
    private Date date;

    public StoriesDto(long id, String username, String profilePicture, String imageStories, Date date) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.imageStories = imageStories;
        this.date = date;
    }

    public StoriesDto(){

    }
}
