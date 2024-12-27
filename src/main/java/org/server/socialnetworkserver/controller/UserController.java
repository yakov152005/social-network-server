package org.server.socialnetworkserver.controller;

import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.repository.PostRepository;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.repository.UserRepository;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.utils.ApiSmsSender;
import org.server.socialnetworkserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.server.socialnetworkserver.service.HelpMethods.*;
import static org.server.socialnetworkserver.utils.ApiEmailProcessor.sendEmail;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;
import static org.server.socialnetworkserver.utils.GeneratorUtils.*;

@RestController
@RequestMapping("/social-network")
public class UserController {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public UserController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/add-user")
    public ValidationResponse addUser(@RequestBody User user) {
        User newUser = userRepository.findByUsername(user.getUsername());

        if (newUser != null) {
            String current = newUser.getUsername().toLowerCase();
            String newUserName = user.getUsername().toLowerCase();
            if (newUserName.equals(current)) {
                return new ValidationResponse(false, ERROR_2, 2);
            }
        }

        final String error = checkAllFiled(user, userRepository);
        int errorCode = errorCodeCheck(error);
        if (errorCode != NO_ERROR) {
            return new ValidationResponse(false, error, errorCode);
        }

        StringBuilder userDetails = new StringBuilder();
        userDetails.append("Username: ").append(user.getUsername()).append("\n")
                .append("Password: ").append(user.getPassword()).append("\n")
                .append("Phone number: ").append(user.getPhoneNumber()).append("\n")
                .append("Email: ").append(user.getEmail());
        System.out.println(userDetails);
        System.out.println(sendEmail(user.getEmail(), "Details", userDetails.toString()));
        // System.out.println(sendEmail("yakov152005@walla.co.il","Details",userDetails.toString()));

        String salt = generateSalt();
        String hashedPassword = hashPassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        return new ValidationResponse(true, "Success: user " + user.getUsername() + " created.", errorCode);
    }


    @PostMapping("/login-user")
    public LoginResponse loginUser(@RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String password = loginDetails.get("password");

        if (username != null && password != null) {
            User currentUser = userRepository.findByUsername(username);
            if (currentUser != null) {

                String hashedPassword = hashPassword(password, currentUser.getSalt());
                if (hashedPassword.equals(currentUser.getPasswordHash())) {
                    String verificationCode = generatorCode();
                    verificationCodes.put(username, verificationCode);

                    ApiSmsSender.sendSms("Your verification code: " + verificationCode,
                            List.of(currentUser.getPhoneNumber()));

                    return new LoginResponse(true, "SMS sent with verification code.", username);
                }
            } else {
                return new LoginResponse(false, "This username is not exist!", null);
            }
        }
        return new LoginResponse(false, "The username or password is incorrect.", null);
    }

    @PostMapping("/change-password")
    public BasicResponse changePassword(@RequestBody Map<String, String> changePasswordDetails) {
        String username = changePasswordDetails.get("username");
        String currentPassword = changePasswordDetails.get("currentPassword");
        String newPassword = changePasswordDetails.get("newPassword");

        boolean checkNewPassword = checkPassword(newPassword);

        User user = userRepository.findByUsername(username);
        if (user != null && checkNewPassword) {

            String storedSalt = user.getSalt();
            String currentPasswordHash = hashPassword(currentPassword, storedSalt);

            String storedPasswordHash = user.getPasswordHash();
            if (storedPasswordHash.equals(currentPasswordHash)) {

                String newSalt = generateSalt();
                String hashedPassword = hashPassword(newPassword, newSalt);

                user.setSalt(newSalt);
                user.setPasswordHash(hashedPassword);
                userRepository.save(user);

                return new BasicResponse(true, "The password successful Changed");
            } else {
                return new BasicResponse(false, "The current password you entered is incorrect.");
            }
        }
        return new BasicResponse(false, ERROR_3);
    }


    @PostMapping("/verify-code")
    public Map<String, String> verifyCode(@RequestBody Map<String, String> verificationDetails) {
        String username = verificationDetails.get("username");
        String code = verificationDetails.get("code");

        if (username != null && code != null && verificationCodes.containsKey(username)) {
            if (verificationCodes.get(username).equals(code)) {
                verificationCodes.remove(username);
                String token = JwtUtils.generateToken(username);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful");
                return response;
            }
        }
        throw new RuntimeException("Invalid verification code");
    }

    @GetMapping("/get-user-details")
    public Map<String, Object> getUserDetails(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        if (token == null || token.isEmpty()) {
            response.put("error", "Token is missing");
            return response;
        }

        String cleanToken = token.replace("Bearer ", "");
        if (!JwtUtils.isTokenValid(cleanToken)) {
            response.put("error", "Invalid token");
            return response;
        }
        String username = JwtUtils.extractUsername(cleanToken);
        response.put("username", username);

        return response;
    }


    @GetMapping("/get-all-user-names")
    public UserNamesResponse getAllUserNames() {
        List<String> usernames = userRepository.findAllUsernames();
        if (usernames.isEmpty()){
            return new UserNamesResponse(false,"No names exist",null);
        }
        return new UserNamesResponse(true,"All usernames.",usernames);
    }

    @GetMapping("/reset-password/{email}&{username}")
    public BasicResponse resetPasswordForThisUser(@PathVariable String email, @PathVariable String username) {
        User user = userRepository.findByEmailIgnoreCase(email);
        User user1 = userRepository.findByUsername(username);

        if (user == null || user1 == null) {
            return new ValidationResponse(false, "This email/username does not exist", ERROR_EMAIL);
        }

        if (!user.getUsername().equals(user1.getUsername())) {
            return new BasicResponse(false, "Username does not match email, password reset failed.");
        }

        String newPassword = generatorPassword();
        String salt = generateSalt();
        String hashedPassword = hashPassword(newPassword, salt);

        user.setSalt(salt);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        String title = "Reset Password";
        String passwordDetails = "Your new password: " + newPassword + "\n";
        sendEmail(user.getEmail(), title, passwordDetails);

        return new BasicResponse(true, "The password was sent to your email. Check it.");
    }


    @PostMapping("/add-post/{username}")
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

    @GetMapping("/get-post-by-username/{username}")
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
                .map(post -> new PostDto(post.getUser().getUsername(), post.getUser().getProfilePicture(), post.getContent(), post.getImageUrl(), post.getDate()))
                .toList();

        return new PostResponse(true, "All posts by user.", postDtos);
    }

    @GetMapping("/home-feed-post/{username}")
    public PostResponse getHomeFeedPost
            (
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            )
    {
        System.out.println("Received request - Username: " + username + ", Page: " + page + ", Size: " + size);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("No posts found for this page.");

            return new PostResponse(false, "User not found.", null);
        }

        /**
         * אם אני רוצה אני יכול לעשות SORT ישירות מה PAGEABLE ולהוריד מהשאילתה בשביל מיון דינמי
         * משהו שאני לא חושב שיהיה לי בו שימוש אבל נשאיר בנתיים על הערה
         */
        // Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")); WITH SORT
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> homeFeedPosts = postRepository.findHomeFeedPosts(username, pageable);

        if (homeFeedPosts.isEmpty()) {
            return new PostResponse(false, "No posts found for the user.", null);
        }

        List<PostDto> postDtos = homeFeedPosts.stream()
                .map(post -> new PostDto(
                        post.getUser().getUsername(),
                        post.getUser().getProfilePicture(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getDate()))
                .toList();

        System.out.println("Returning " + postDtos.size() + " posts for page " + page);
        return new PostResponse(true, "All posts home feed.", postDtos);
    }

    @PostMapping("/add-profile-pic")
    public BasicResponse addProfilePicture(@RequestBody Map<String, String> addPicProfile) {
        String username = addPicProfile.get("username");
        String newPicture = addPicProfile.get("profilePicture");
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setProfilePicture(newPicture);
            userRepository.save(user);
            return new BasicResponse(true, "Add profile pic success.");
        }

        return new BasicResponse(false, "Add profile pic NOT success.");
    }

    @GetMapping("/get-profile-pic/{username}")
    public ProfilePicResponse getProfilePictureByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ProfilePicResponse(true, "Success send profile pic", user.getProfilePicture());
        }
        return new ProfilePicResponse(false, "Not success", null);
    }


}

