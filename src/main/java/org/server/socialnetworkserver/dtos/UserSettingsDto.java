package org.server.socialnetworkserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSettingsDto {
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    private String gender;
    private String relationship;
    private String fullName;

    public UserSettingsDto(String username, String email, String profilePicture, String bio, String gender, String relationship, String fullName) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.gender = gender;
        this.relationship = relationship;
        this.fullName = fullName;
    }
    public UserSettingsDto(){

    }
}
