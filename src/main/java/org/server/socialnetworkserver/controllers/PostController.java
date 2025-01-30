package org.server.socialnetworkserver.controllers;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.PostResponse;
import org.server.socialnetworkserver.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class PostController {


    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/add-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BasicResponse addPost(
            @RequestParam("username") String username,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "postImageFile", required = false) MultipartFile postImageFile,
            @RequestParam(value = "postImageUrl", required = false) String postImageUrl
    ) {
        return postService.addPost(username, content, postImageFile, postImageUrl);
    }

    @GetMapping("/get-post-by-username/{username}")
    public PostResponse getPostByUserName(@PathVariable String username) {
        return postService.getPostByUserName(username);
    }

    @GetMapping("/home-feed-post/{username}")
    public PostResponse getHomeFeedPost
            (@PathVariable String username,
             @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return postService.getHomeFeedPost(username,page,size);
    }

}
