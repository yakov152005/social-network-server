package org.server.socialnetworkserver.dtos;

public class ChatUserDto {
    private String receiver;
    private String profilePicture;

    public ChatUserDto(String receiver, String profilePicture) {
        this.receiver = receiver;
        this.profilePicture = profilePicture;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
