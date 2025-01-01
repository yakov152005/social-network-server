package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.entitys.Comment;


public class CommentResponse {
    private Long postId;
    private String username;
    private String content;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

