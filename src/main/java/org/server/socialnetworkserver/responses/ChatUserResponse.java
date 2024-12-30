package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.ChatUserDto;

import java.util.List;

public class ChatUserResponse extends BasicResponse{
    List<ChatUserDto> chatUserDtos;

    public ChatUserResponse(boolean success, String error, List<ChatUserDto> chatUserDtos) {
        super(success, error);
        this.chatUserDtos = chatUserDtos;
    }

    public ChatUserResponse(List<ChatUserDto> chatUserDtos) {
        this.chatUserDtos = chatUserDtos;
    }

    public List<ChatUserDto> getChatUserDtos() {
        return chatUserDtos;
    }

    public void setChatUserDtos(List<ChatUserDto> chatUserDtos) {
        this.chatUserDtos = chatUserDtos;
    }
}
