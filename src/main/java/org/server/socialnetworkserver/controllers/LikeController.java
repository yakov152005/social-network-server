package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.responses.AllLikesResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/like-post/{postId}&{username}")
    public BasicResponse likePost(@PathVariable Long postId, @PathVariable String username ){
        return likeService.likePost(postId, username);
    }

    @DeleteMapping("/unlike-post/{postId}&{username}")
    public BasicResponse unlikePost(@PathVariable Long postId, @PathVariable String username){
        return likeService.unlikePost(postId,username);
    }

    @GetMapping("/likes-count/{postId}")
    public int countLikes(@PathVariable Long postId){
        return likeService.countLikes(postId);
    }

    @GetMapping("/get-all-likes-post/{postId}")
    public AllLikesResponse getAllLikesPost(@PathVariable Long postId){
        return likeService.getAllLikesPost(postId);
    }
}
