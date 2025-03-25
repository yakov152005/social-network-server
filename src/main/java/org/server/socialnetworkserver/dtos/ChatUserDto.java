package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatUserDto {
    private String receiver;
    private String profilePicture;
    private String lastMessage;
    private Date lastMessageTime;

    public ChatUserDto(String receiver, String profilePicture, String lastMessage, Date lastMessageTime) {
        this.receiver = receiver;
        this.profilePicture = profilePicture;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}
