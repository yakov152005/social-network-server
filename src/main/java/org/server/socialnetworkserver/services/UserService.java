package org.server.socialnetworkserver.services;
import org.server.socialnetworkserver.dtos.UsernameWithPicDto;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.utils.ApiSmsSender;
import org.server.socialnetworkserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.server.socialnetworkserver.services.HelpMethods.*;
import static org.server.socialnetworkserver.utils.ApiEmailProcessor.sendEmail;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;
import static org.server.socialnetworkserver.utils.GeneratorUtils.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
        // System.out.println(sendEmail("yakov152005@walla.co.il","Details",userDetails.toString()));

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


    public ProfilePictureResponse getProfilePictureByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ProfilePictureResponse(true, "Success send profile pic", user.getProfilePicture());
        }
        return new ProfilePictureResponse(false, "Not success", null);
    }


    public UserNamesWithPicResponse getAllUserNamesAndPic() {
        List<UsernameWithPicDto> result = userRepository.findAllUsernamesWithPic();
        if (result.isEmpty()){
            return new UserNamesWithPicResponse(false,"No Users exist.",null);
        }

        return new UserNamesWithPicResponse(true,"All users with pic.",result);
    }

}
