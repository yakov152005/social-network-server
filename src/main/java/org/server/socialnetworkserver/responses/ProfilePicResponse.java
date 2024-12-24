package org.server.socialnetworkserver.responses;

public class ProfilePicResponse extends BasicResponse{
    private String profilePicture;

    public ProfilePicResponse(boolean success, String error,String profilePicture){
        super(success,error);
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}
