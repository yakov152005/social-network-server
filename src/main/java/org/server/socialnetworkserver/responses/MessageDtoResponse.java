package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.MessageDto;

import java.util.List;

public class MessageDtoResponse extends BasicResponse{
    private List<MessageDto> messageDtos;

    public MessageDtoResponse(boolean success, String error, List<MessageDto> messageDtos) {
        super(success, error);
        this.messageDtos = messageDtos;
    }

    public List<MessageDto> getMessageDtos() {
        return messageDtos;
    }

    public void setMessageDtos(List<MessageDto> messageDtos) {
        this.messageDtos = messageDtos;
    }
}
