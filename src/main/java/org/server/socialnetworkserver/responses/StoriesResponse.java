package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;
import org.server.socialnetworkserver.dtos.StoriesDto;

import java.util.List;

@Getter
@Setter
public class StoriesResponse extends BasicResponse{
    public List<StoriesDto> storiesDtos;

    public StoriesResponse(boolean success, String error, List<StoriesDto> storiesDtos) {
        super(success, error);
        this.storiesDtos = storiesDtos;
    }

    public StoriesResponse(List<StoriesDto> storiesDtos) {
        this.storiesDtos = storiesDtos;
    }


}
