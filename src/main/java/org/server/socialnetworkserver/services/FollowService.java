package org.server.socialnetworkserver.services;

import jakarta.transaction.Transactional;
import org.server.socialnetworkserver.controllers.NotificationController;
import org.server.socialnetworkserver.dtos.*;
import org.server.socialnetworkserver.entitys.Follow;
import org.server.socialnetworkserver.entitys.Notification;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.*;
import org.server.socialnetworkserver.responses.AllFollowResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.FollowResponse;
import org.server.socialnetworkserver.responses.ProfileResponse;
import org.server.socialnetworkserver.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationController notificationController;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository,
                         PostRepository postRepository, NotificationRepository notificationRepository,
                         NotificationController notificationController

    ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
        this.notificationController = notificationController;
    }


    public AllFollowResponse getNumOfFollowersAndFollowing(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            return new AllFollowResponse(false,"This user not exist.",0,0);
        }

        int followers = followRepository.countFollowers(username);
        int following = followRepository.countFollowing(username);

        return new AllFollowResponse(true,"All number of followers and following by username.",followers,following);
    }


    /*
    public ProfileResponse getAllDetailsOfProfileSearch(@PathVariable String currentUsername, @PathVariable String username){
        User currentUser = userRepository.findByUsername(currentUsername);
        User searchUser = userRepository.findByUsername(username);

        if (currentUser == null || searchUser == null){
            return new ProfileResponse(false,"No find user..",null);
        }

        int followers = followRepository.countFollowers(username);
        int following = followRepository.countFollowing(username);

        boolean isFollowing = followRepository.isFollowing(currentUsername,username);

        List<Post> allPostsByUserSearch = postRepository.findPostsByUsername(username);
        List<PostDto> postDtos = allPostsByUserSearch.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getUser().getUsername(),
                        post.getUser().getProfilePicture(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getDate(),
                        likeRepository.isLikedByUser(post.getId(),currentUser.getId()),
                        likeRepository.countLikeByPost(post.getId()),
                        commentRepository.countCommentByPostId(post.getId())
                        ))
                .toList();

        ProfileDto profileDto =  new ProfileDto(
                searchUser.getUsername(),
                searchUser.getProfilePicture(),
                followers,
                following,
                isFollowing,
                postDtos
        );


        return new ProfileResponse(true,"Profile response success.",profileDto);
    }
     */
    @Cacheable(value = "profileCache", key = "#currentUsername + '_' + #username")
    public ProfileResponse getAllDetailsOfProfileSearch(String currentUsername, String username){
        User currentUser = userRepository.findByUsername(currentUsername);
        User searchUser = userRepository.findByUsername(username);

        if (currentUser == null || searchUser == null){
            return new ProfileResponse(false, "No find user.", null);
        }

        ProfileStatsDto profileStats = followRepository.getProfileStats(username, currentUser.getId())
                .orElse(new ProfileStatsDto(0L, 0L, false));


        List<Object[]> results = postRepository.findProfilePosts(username, currentUser.getId());
        List<PostDto> postDtos = results.stream()
                .map(result -> new PostDto(
                        ((Post) result[0]).getId(),
                        ((Post) result[0]).getUser().getUsername(),
                        ((Post) result[0]).getUser().getProfilePicture(),
                        ((Post) result[0]).getContent(),
                        ((Post) result[0]).getImageUrl(),
                        ((Post) result[0]).getDate(),
                        (Boolean) result[3], // isLiked
                        ((Number) result[1]).intValue(), // likeCount
                        ((Number) result[2]).intValue() // commentCount
                ))
                .toList();

        ProfileDto profileDto = new ProfileDto(
                searchUser.getUsername(),
                searchUser.getProfilePicture(),
                profileStats.getFollowersCount().intValue(),
                profileStats.getFollowingCount().intValue(),
                profileStats.isFollowing(),
                postDtos
        );

        return new ProfileResponse(true, "Profile response success.", profileDto);
    }


    public FollowResponse getFollower(@PathVariable String username){
       List<FollowDto> getAllFollowers = followRepository.getAllFollowers(username);
       List<FollowDto> getAllFollowing = followRepository.getAllFollowing(username);

       if ((getAllFollowers.isEmpty() && getAllFollowing.isEmpty())){
           return new FollowResponse(false,"Empty followers and following",null,null);
       }



       return new FollowResponse(true,"Success get followers and following",getAllFollowers,getAllFollowing);
    }



    public BasicResponse followUser(@PathVariable String username, @PathVariable String currentUsername){
        User userToFollow = userRepository.findByUsername(username);
        User currentUser = userRepository.findByUsername(currentUsername);

        if (userToFollow == null || currentUser == null){
            return new BasicResponse(false,"User Not found.");
        }

        boolean isFollow = followRepository.isFollowing(currentUsername,username);
        if (isFollow){
            return new BasicResponse(false,"You are already following this user.");
        }

        Follow follow = new Follow();
        follow.setFollower(currentUser);
        follow.setFollowing(userToFollow);
        followRepository.save(follow);

        Notification notification = new Notification(userToFollow, currentUser, currentUser.getProfilePicture(), Constants.Notification.FOLLOW);
        notificationRepository.save(notification);

        System.out.println(notification);


        NotificationDto notificationDto = new NotificationDto(
                notification.getId(),
                userToFollow.getUsername(),
                currentUser.getUsername(),
                currentUser.getProfilePicture(),
                Constants.Notification.FOLLOW,
                notification.getDate(),
                notification.isRead()
        );

        notificationController.sendNotification(userToFollow.getUsername(),notificationDto);

        return new BasicResponse(true,"Followed successfully!");
    }


    @Transactional
    public BasicResponse unfollowUser(String username, String currentUsername) {
        User userToUnFollow = userRepository.findByUsername(username);
        User currentUser = userRepository.findByUsername(currentUsername);

        if (userToUnFollow == null || currentUser == null) {
            return new BasicResponse(false, "User Not found.");
        }

        followRepository.deleteByFollowerAndFollowing(currentUser, userToUnFollow);
        return new BasicResponse(true, "Unfollowed successfully");
    }
}
