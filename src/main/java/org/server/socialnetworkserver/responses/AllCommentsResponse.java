package org.server.socialnetworkserver.responses;

import org.server.socialnetworkserver.dtos.CommentDto;
import org.server.socialnetworkserver.entitys.Comment;

import java.util.List;

public class AllCommentsResponse extends BasicResponse{
    List<CommentDto> comments;

    public AllCommentsResponse(boolean success, String error, List<CommentDto> comments) {
        super(success, error);
        this.comments = comments;

    }

    public AllCommentsResponse(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

}
