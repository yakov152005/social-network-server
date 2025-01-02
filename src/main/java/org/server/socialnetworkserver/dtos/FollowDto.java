package org.server.socialnetworkserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FollowDto {
    private String username;
    private String profilePicture;
    //private boolean isFollowing;


    public FollowDto(String username, String profilePicture) {
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    /*
     public boolean isFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean following) {
       isFollowing = following;

       }*/

    @Override
    public String toString() {
        return "FollowDto{" +
                "username='" + username + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}

