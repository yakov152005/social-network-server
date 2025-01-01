package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.dtos.PostDto;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.CommentRepository;
import org.server.socialnetworkserver.repositoris.LikeRepository;
import org.server.socialnetworkserver.repositoris.PostRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class PostService {


    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository,LikeRepository likeRepository,CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }


    public BasicResponse addPost(@PathVariable String username, @RequestBody Map<String, String> postDetails) {
        String content = postDetails.get("content");
        String imageUrl = postDetails.get("imageUrl");

        if (content == null || content.isEmpty()) {
            return new BasicResponse(false, "Content is Empty.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new BasicResponse(false, "User not Found.");
        }

        Post newPost = new Post(user, content, imageUrl);
        postRepository.save(newPost);

        return new BasicResponse(true, "Post added successfully.");
    }



    public PostResponse getPostByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new PostResponse(false, "User not found", null);
        }

        List<Post> allPostsByUser = postRepository.findPostsByUsername(username);
        if (allPostsByUser.isEmpty()) {
            return new PostResponse(false, "No posts found for the user.", null);
        }

        List<PostDto> postDtos = allPostsByUser.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getUser().getUsername(),
                        post.getUser().getProfilePicture(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getDate(),
                        likeRepository.isLikedByUser(post.getId(),user.getId()),
                        likeRepository.countLikeByPost(post.getId())
                ))
                .toList();

        return new PostResponse(true, "All posts by user.", postDtos);
    }



    public PostResponse getHomeFeedPost
            (@PathVariable String username,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size)
    {
        System.out.println("Received request - Username: " + username + ", Page: " + page + ", Size: " + size);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("No posts found for this page.");

            return new PostResponse(false, "User not found.", null);
        }


        Pageable pageable = PageRequest.of(page, size);
        Page<Post> homeFeedPosts = postRepository.findHomeFeedPosts(username, pageable);

        if (homeFeedPosts.isEmpty()) {
            return new PostResponse(false, "No posts found for the user.", null);
        }

        List<PostDto> postDtos = homeFeedPosts.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getUser().getUsername(),
                        post.getUser().getProfilePicture(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getDate(),
                        likeRepository.isLikedByUser(post.getId(),user.getId()),
                        likeRepository.countLikeByPost(post.getId()),
                        commentRepository.countCommentByPostId(post.getId())
                ))
                .toList();

        System.out.println("Returning " + postDtos.size() + " posts for page " + page);
        return new PostResponse(true, "All posts home feed.", postDtos);
    }


}
