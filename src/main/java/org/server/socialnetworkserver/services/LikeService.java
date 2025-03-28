package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.controllers.NotificationController;
import org.server.socialnetworkserver.dtos.LikeDto;
import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.entitys.Like;
import org.server.socialnetworkserver.entitys.Notification;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.LikeRepository;
import org.server.socialnetworkserver.repositoris.NotificationRepository;
import org.server.socialnetworkserver.repositoris.PostRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.AllLikesResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class LikeService {


    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationController notificationController;


    @Autowired
    public LikeService(LikeRepository likeRepository,UserRepository userRepository,PostRepository postRepository,NotificationRepository notificationRepository,NotificationController notificationController){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
        this.notificationController = notificationController;
    }

    // @CacheEvict(value = "postLikesCache", key = "'likes_' + #postId")
   public BasicResponse likePost(@PathVariable Long postId, @PathVariable String username){
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(username);

        if (post == null || user == null){
            return new BasicResponse(false,  "Post or user not found.");
        }

        boolean alreadyLiked = likeRepository.isLikedByUser(post.getId(), user.getId());
        if (alreadyLiked) {
            return new BasicResponse(false, "User already liked this post.");
        }

       if (!(post.getUser().getUsername().equals(user.getUsername()))){
           notificationRepository.deleteExistingNotification(
                   post.getUser().getId(), user.getId(), Constants.Notification.LIKE, postId
           );
       }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

      if (!(post.getUser().getUsername().equals(user.getUsername()))){

          Notification notification = new Notification(post.getId(),post.getImageUrl(),post.getUser(),user,user.getProfilePicture(), Constants.Notification.LIKE);
          notificationRepository.save(notification);

          NotificationDto notificationDto = new NotificationDto(
                  notification.getId(),
                  post.getId(),
                  post.getImageUrl(),
                  post.getUser().getUsername(),
                  user.getUsername(),
                  user.getProfilePicture(),
                  Constants.Notification.LIKE,
                  notification.getDate(),
                  notification.isRead()
          );

          notificationController.sendNotification(post.getUser().getUsername(),notificationDto);
      }
        System.out.println("🔄 Clearing cache: postLikesCache -> likes_" + postId);

        return new BasicResponse(true, "Post liked successfully.");
    }

    //  @CacheEvict(value = "postLikesCache", key = "'likes_' + #postId")
    public BasicResponse unlikePost(@PathVariable Long postId, @PathVariable String username){
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(username);

        if (post == null || user == null){
            return new BasicResponse(false,  "Post or user not found.");
        }

        likeRepository.deleteByPostAndUser(post,user);

        return new BasicResponse(true,"Unliked success.");
    }

    // @Cacheable(value = "postLikesCache", key = "#postId")
    public int countLikes(@PathVariable Long postId){
        System.out.println("🟢 Fetching count from database for post ID: " + postId);
        return likeRepository.countLikeByPost(postId);
    }

    //  @Cacheable(value = "postLikesCache", key = "#postId")
    public AllLikesResponse getAllLikesPost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            return new AllLikesResponse(false,"The post not exist.",null);
        }

        List<LikeDto> likeDtos = likeRepository.findAllLikesByPostId(postId);

        return new AllLikesResponse(true,"All likes send.",likeDtos);
    }


}
