package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.dtos.UsernameWithPicDto;
import org.server.socialnetworkserver.entitys.LoginActivity;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.*;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.utils.ApiSmsSender;
import org.server.socialnetworkserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.server.socialnetworkserver.services.HelpMethods.*;
import static org.server.socialnetworkserver.utils.ApiEmailProcessor.sendEmail;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;
import static org.server.socialnetworkserver.utils.GeneratorUtils.*;
import static org.server.socialnetworkserver.utils.UploadFileToCloud.uploadFileToCloud;

@Service
public class UserService {


    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;
    private final LoginActivityRepository loginActivityRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public UserService(LikeRepository likeRepository,UserRepository userRepository,
                       PostRepository postRepository, MessageRepository messageRepository,
                       LoginActivityRepository loginActivityRepository,CommentRepository commentRepository,
                       NotificationRepository notificationRepository
    ) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.messageRepository = messageRepository;
        this.loginActivityRepository = loginActivityRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
    }

    public BasicResponse getNumOfUsers(){
        String numOfUsers = String.valueOf(userRepository.findAll().size());
        return new BasicResponse(true,numOfUsers);
    }

    public TokenResponse validateToken(String cleanToken) {
        boolean isValid = JwtUtils.isTokenValid(cleanToken);
        String username = "";
        if (isValid){
            username = JwtUtils.extractUsername(cleanToken);
        }

        return new TokenResponse(isValid, isValid ? "Token is valid" : "Token is invalid", isValid,username);
    }

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


        String salt = generateSalt();
        String hashedPassword = hashPassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        return new ValidationResponse(true, "Success: user " + user.getUsername() + " created.", errorCode);
    }


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


    public Map<String, String> verifyCode(@RequestBody Map<String, String> verificationDetails) {
        String username = verificationDetails.get("username");
        String code = verificationDetails.get("code");

        if (username != null && code != null && verificationCodes.containsKey(username)) {
            if (verificationCodes.get(username).equals(code)) {
                LoginActivity session = loginActivityRepository.findByUsername(username);


                if (session != null){
                    System.out.println(session.getUser().getUsername()  + "current session");
                    session.setUser(session.getUser());
                    session.setDate(new Date());
                    loginActivityRepository.save(session);
                }else {
                    LoginActivity newSession = new LoginActivity();
                    newSession.setUser(userRepository.findByUsername(username));
                    System.out.println(newSession.getUser().getUsername() + " new session");
                    loginActivityRepository.save(newSession);
                }

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
        String profilePicture = userRepository.findByUsername(username).getProfilePicture();
        response.put("username", username);
        response.put("profilePicture",profilePicture);

        return response;
    }


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


    public BasicResponse addProfilePicture(String username, MultipartFile profilePictureFile, String profilePictureUrl) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new BasicResponse(false, "User not found.");
        }

        try {
            String finalProfilePictureUrl;
            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {

                finalProfilePictureUrl = uploadFileToCloud(profilePictureFile);
            } else if (profilePictureUrl != null && profilePictureUrl.startsWith("http")) {

                finalProfilePictureUrl = profilePictureUrl;
            } else {
                return new BasicResponse(false, "Invalid profile picture format.");
            }

            user.setProfilePicture(finalProfilePictureUrl);
            userRepository.save(user);

            return new BasicResponse(true, "Profile picture updated successfully.");
        } catch (IOException e) {
            return new BasicResponse(false, "Error uploading profile picture.");
        }
    }


    public UserNamesWithPicResponse getAllUserNamesAndPic() {
        List<UsernameWithPicDto> result = userRepository.findAllUsernamesWithPic();
        if (result.isEmpty()) {
            return new UserNamesWithPicResponse(false, "No Users exist.", null);
        }

        return new UserNamesWithPicResponse(true, "All users with pic.", result);
    }

    @Transactional
    public BasicResponse deleteUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            String storedSalt = user.getSalt();
            String currentPasswordHash = hashPassword(password, storedSalt);

            String storedPasswordHash = user.getPasswordHash();
            if (storedPasswordHash.equals(currentPasswordHash)) {
                System.out.println("Password match. Proceeding to delete...");

                notificationRepository.deleteByRecipient(user.getId());
                System.out.println("Notifications deleted.");
                commentRepository.deleteByPostUser(user);
                System.out.println("All comments on user's posts deleted.");
                commentRepository.deleteByUser(user);
                System.out.println("User's own comments deleted.");
                likeRepository.deleteByPostUser(user);
                System.out.println("Likes for user's posts deleted.");
                likeRepository.deleteByUser(user);
                System.out.println("Likes by user deleted.");
                messageRepository.deleteBySenderOrReceiver(user);
                System.out.println("Messages deleted.");
                postRepository.deleteByUser(user);
                loginActivityRepository.deleteByUser(user.getId());
                System.out.println("Login records deleted.");
                System.out.println("Posts deleted.");
                userRepository.delete(user);

                return new BasicResponse(true, "User deleted successfully.");
            }
        }
        return new BasicResponse(false, "User not deleted. Invalid username or password.");
    }



}
