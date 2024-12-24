package org.server.socialnetworkserver.responses;

import java.util.List;

public class PostResponse extends BasicResponse{
    private List<PostDto> posts;

    public PostResponse(boolean success, String error,List<PostDto> posts){
        super(success,error);
        this.posts = posts;
    }

    public PostResponse(){

    }

    public List<PostDto> getPostList() {
        return posts;
    }

    public void setPostList(List<PostDto> posts) {
        this.posts = posts;
    }

}
