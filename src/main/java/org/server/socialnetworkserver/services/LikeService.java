package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.entitys.Like;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.LikeRepository;
import org.server.socialnetworkserver.repositoris.PostRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository,UserRepository userRepository,PostRepository postRepository){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

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

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        return new BasicResponse(true, "Post liked successfully.");
    }

    public BasicResponse unlikePost(@PathVariable Long postId, @PathVariable String username){
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(username);

        if (post == null || user == null){
            return new BasicResponse(false,  "Post or user not found.");
        }

        likeRepository.deleteByPostAndUser(post,user);

        return new BasicResponse(true,"Unliked success.");
    }

    public int countLikes(@PathVariable Long postId){
        return likeRepository.countLikeByPost(postId);
    }




}
