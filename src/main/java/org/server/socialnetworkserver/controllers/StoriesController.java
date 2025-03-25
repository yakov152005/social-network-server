package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.StoriesResponse;
import org.server.socialnetworkserver.services.StoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class StoriesController {
    private final StoriesService storiesService;

    @Autowired
    public StoriesController(StoriesService storiesService) {
        this.storiesService = storiesService;
    }

    @PostMapping(value = "/add-stories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BasicResponse addStories(
            @RequestParam("username") String username,
            @RequestParam(value = "postImageFile", required = false) MultipartFile postImageFile,
            @RequestParam(value = "postImageUrl", required = false) String postImageUrl
    ) {
        return storiesService.addStories(username, postImageFile, postImageUrl);
    }

    @GetMapping("/get-all-stories/{username}")
    public StoriesResponse getStoriesForUser(@PathVariable String username){
        return storiesService.getStoriesForUser(username);
    }
}
