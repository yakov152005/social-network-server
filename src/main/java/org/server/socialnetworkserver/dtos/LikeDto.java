package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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


}
