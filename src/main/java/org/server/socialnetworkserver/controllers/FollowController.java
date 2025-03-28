package org.server.socialnetworkserver.controllers;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class FollowController {


    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService){
        this.followService = followService;
    }

    @GetMapping("/get-num-followers-following/{username}")
    public AllFollowResponse getNumOfFollowersAndFollowing(@PathVariable String username){
        return followService.getNumOfFollowersAndFollowing(username);
    }

    @GetMapping("/get-all-details-profile-search/{currentUsername}&{username}")
    public ProfileResponse getAllDetailsOfProfileSearch(@PathVariable String currentUsername, @PathVariable String username){
        return followService.getAllDetailsOfProfileSearch(currentUsername,username);
    }

    @PostMapping("/follow/{username}&{currentUsername}")
    public BasicResponse followUser(@PathVariable String username, @PathVariable String currentUsername){
        return followService.followUser(username,currentUsername);
    }

    @DeleteMapping("/unfollow/{username}&{currentUsername}")
    public BasicResponse unfollowUser(@PathVariable String username, @PathVariable String currentUsername) {
        return followService.unfollowUser(username, currentUsername);
    }

    @GetMapping("/get-all-followers-following/{username}")
    public FollowResponse getAllFollowersAndFollowing(@PathVariable String username){
        return followService.getFollower(username);
    }

    @GetMapping("/suggested/{username}")
    public SuggestedFriendsResponse getSuggestedFriends(@PathVariable String username){
        return followService.getSuggestedFriends(username);
    }



}
