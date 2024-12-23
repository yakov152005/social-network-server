package org.server.socialnetworkserver.controller;
import org.server.socialnetworkserver.entitys.Response;
import org.server.socialnetworkserver.repository.UserRepository;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.utils.ApiSmsSender;
import org.server.socialnetworkserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.server.socialnetworkserver.service.HelpMethods.*;
import static org.server.socialnetworkserver.utils.ApiEmailProcessor.sendEmail;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/add-user")
    public Response addUser(@RequestBody User user) {
        final String error = checkAllFiled(user, userRepository);
        int errorCode = errorCodeCheck(error);
        if (errorCode != NO_ERROR ) {
            return new Response(false, error,errorCode);
        }

        String userDetails = "Username: " + user.getUsername() + "\n" +
                "Password: " + user.getPassword() + "\n" +
                "Phone number: " + user.getPhoneNumber() + "\n" +
                "Email: " + user.getEmail();
        System.out.println(userDetails);
        System.out.println(sendEmail(user.getEmail(), "Details", userDetails));

        String salt = generateSalt();
        String hashedPassword = hashPassword(user.getPassword(), salt);


        user.setSalt(salt);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        return new Response(true, "Success: user " + user.getUsername() + " created.", errorCode);
    }


    @PostMapping("/login-user")
    public Map<String, String> loginUser(@RequestBody Map<String, String> loginDetails) {
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

                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Verification code sent");
                    response.put("username", username);
                    return response;
                }
            }
        }
        throw new RuntimeException("Invalid login details");
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
    public List<String> getAllUserNames(){
        List<String> list = userRepository.findAll().stream()
                .map(User::getUsername).toList();
        System.out.println(list);
        return list;
    }

    @GetMapping("/reset-password/{email}")
    public Response resetPasswordForThisUser(@PathVariable String email) {
        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return new Response(false, "This email does not exist", ERROR_EMAIL);
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

        return new Response(true, "The password was sent to your email. Check it.", NO_ERROR);
    }







}

