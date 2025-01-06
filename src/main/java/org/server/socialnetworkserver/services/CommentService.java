package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.controllers.NotificationController;
import org.server.socialnetworkserver.dtos.CommentDto;
import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.entitys.Comment;
import org.server.socialnetworkserver.entitys.Notification;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.CommentRepository;
import org.server.socialnetworkserver.repositoris.NotificationRepository;
import org.server.socialnetworkserver.repositoris.PostRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.AllCommentsResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.CommentResponse;
import org.server.socialnetworkserver.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CommentService {


    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationController notificationController;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository, PostRepository postRepository,
                          NotificationRepository notificationRepository, NotificationController notificationController){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
        this.notificationController = notificationController;
    }

    public BasicResponse addComments(@RequestBody CommentResponse commentResponse ){
        User user = userRepository.findByUsername(commentResponse.getUsername());
        Post post = postRepository.findById(commentResponse.getPostId()).orElse(null);

        if (post == null || user == null){
            return new BasicResponse(false,"No find user or post.");
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentResponse.getContent());
        commentRepository.save(comment);

        Notification notification = new Notification(post.getId(),post.getImageUrl(),post.getUser(),user,user.getProfilePicture(), Constants.Notification.COMMENT);
        notificationRepository.save(notification);

        NotificationDto notificationDto = new NotificationDto(
                notification.getId(),
                post.getId(),
                post.getImageUrl(),
                post.getUser().getUsername(),
                user.getUsername(),
                user.getProfilePicture(),
                Constants.Notification.COMMENT,
                notification.getDate(),
                notification.isRead()
        );

        notificationController.sendNotification(post.getUser().getUsername(),notificationDto);

        return new BasicResponse(true,"Add comment success.");
    }


    public AllCommentsResponse getAllCommentPost(@PathVariable Long postId){
        List<CommentDto> comments = commentRepository.findAllCommentByPostId(postId);

        if (comments.isEmpty()){
            return new AllCommentsResponse(false,"Comment not exist.",null);
        }

        return new AllCommentsResponse(true,"Success to get all comments.",comments);
    }
}
