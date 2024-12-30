package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.MessageDto;

public class MessageResponse extends BasicResponse{
    private MessageDto messageDto;

    public MessageResponse(boolean success, String error, MessageDto messageDto) {
        super(success, error);
        this.messageDto = messageDto;
    }

    public MessageResponse(MessageDto messageDto) {
        this.messageDto = messageDto;
    }

    public MessageDto getMessageDto() {
        return messageDto;
    }

    public void setMessageDto(MessageDto messageDto) {
        this.messageDto = messageDto;
    }
}
