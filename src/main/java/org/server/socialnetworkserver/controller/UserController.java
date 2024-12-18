package org.server.socialnetworkserver.controller;
import org.server.socialnetworkserver.entitys.Response;
import org.server.socialnetworkserver.repository.UserRepository;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.utils.ApiSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/addUser")
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
        userRepository.save(user);

        return new Response(true, "Success: user " + user.getUsername() + " created.", errorCode);
    }


    @PostMapping("/loginUser")
    public boolean loginUser(@RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String password = loginDetails.get("password");

        if (username != null && password != null) {
            for (User currentUser : userRepository.findAll()) {
                if (username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword())) {
                    String verificationCode = generatorCode();
                    verificationCodes.put(username, verificationCode);

                    ApiSmsSender.sendSms("Your verification code: " + verificationCode,
                            List.of(currentUser.getPhoneNumber()));
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/verifyCode")
    public boolean verifyCode(@RequestBody Map<String, String> verificationDetails) {
        String username = verificationDetails.get("username");
        String code = verificationDetails.get("code");

        if (username != null && code != null && verificationCodes.containsKey(username)) {
            if (verificationCodes.get(username).equals(code)) {
                verificationCodes.remove(username);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/getAllUserName")
    public List<String> getAllUserName(){
        List<String> list = userRepository.findAll().stream()
                .map(User::getUsername).toList();
        return list;
    }






}

