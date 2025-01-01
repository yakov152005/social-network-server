package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.responses.AllCommentsResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.CommentResponse;
import org.server.socialnetworkserver.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/add-comment")
    public BasicResponse addComment(@RequestBody CommentResponse commentResponse ) {
        return commentService.addComments(commentResponse);
    }


    @GetMapping("/get-all-comment-post/{postId}")
    public AllCommentsResponse getAllCommentPost(@PathVariable Long postId){
        return commentService.getAllCommentPost(postId);
    }
}
