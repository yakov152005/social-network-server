package org.server.socialnetworkserver.services;

import jakarta.transaction.Transactional;
import org.server.socialnetworkserver.dtos.PostDto;
import org.server.socialnetworkserver.dtos.ProfileDto;
import org.server.socialnetworkserver.entitys.Follow;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.FollowRepository;
import org.server.socialnetworkserver.repositoris.LikeRepository;
import org.server.socialnetworkserver.repositoris.PostRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.AllFollowResponse;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository,PostRepository postRepository,LikeRepository likeRepository ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
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
                        likeRepository.countLikeByPost(post.getId())
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
