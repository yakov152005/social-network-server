package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;
import org.server.socialnetworkserver.dtos.OnlineFriendsDto;

import java.util.List;

@Getter
@Setter
public class OnlineFriendsResponse extends BasicResponse{
    private List<OnlineFriendsDto> onlineFriendsDtos;


    public OnlineFriendsResponse(boolean success, String error, List<OnlineFriendsDto> onlineFriendsDtos) {
        super(success, error);
        this.onlineFriendsDtos = onlineFriendsDtos;
    }

    public OnlineFriendsResponse(boolean success, String error) {
        super(success, error);
    }


    public OnlineFriendsResponse(List<OnlineFriendsDto> onlineFriendsDtos) {
        this.onlineFriendsDtos = onlineFriendsDtos;
    }
}
