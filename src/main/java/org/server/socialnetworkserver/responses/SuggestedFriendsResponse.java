package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;
import org.server.socialnetworkserver.dtos.SuggestedFriendsDto;

import java.util.List;

@Getter
@Setter
public class SuggestedFriendsResponse extends BasicResponse{
    private List<SuggestedFriendsDto> suggestedFriendsDtos;


    public SuggestedFriendsResponse(boolean success, String error, List<SuggestedFriendsDto> suggestedFriendsDtos) {
        super(success, error);
        this.suggestedFriendsDtos = suggestedFriendsDtos;
    }

    public SuggestedFriendsResponse(boolean success, String error) {
        super(success, error);
    }

    public SuggestedFriendsResponse() {
    }
}
