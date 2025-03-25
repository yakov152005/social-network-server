package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.ProfileDto;



public class ProfileResponse extends BasicResponse{
    private ProfileDto profileDto;


    public ProfileResponse(boolean success, String error, ProfileDto profileDto) {
        super(success, error);
        this.profileDto = profileDto;
    }

    public ProfileDto getProfileDto() {
        return profileDto;
    }

    public void setProfileDto(ProfileDto profileDto) {
        this.profileDto = profileDto;
    }
}
