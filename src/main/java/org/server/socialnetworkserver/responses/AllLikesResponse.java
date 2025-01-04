package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.LikeDto;

import java.util.List;

public class AllLikesResponse extends BasicResponse{
    private List<LikeDto> likeDtos;

    public AllLikesResponse(boolean success, String error, List<LikeDto> likeDtos) {
        super(success, error);
        this.likeDtos = likeDtos;
    }

    public AllLikesResponse(List<LikeDto> likeDtos) {
        this.likeDtos = likeDtos;
    }

    public List<LikeDto> getLikeDtos() {
        return likeDtos;
    }

    public void setLikeDtos(List<LikeDto> likeDtos) {
        this.likeDtos = likeDtos;
    }
}
