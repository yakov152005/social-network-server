package org.server.socialnetworkserver.controllers;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;


@RestController
@RequestMapping(URL_SERVER)
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add-user")
    public ValidationResponse addUser(@RequestBody User user) {

        return userService.addUser(user);
    }


    @PostMapping("/login-user")
    public LoginResponse loginUser(@RequestBody Map<String, String> loginDetails) {
        return userService.loginUser(loginDetails);
    }

    @PostMapping("/change-password")
    public BasicResponse changePassword(@RequestBody Map<String, String> changePasswordDetails) {
        return userService.changePassword(changePasswordDetails);
    }


    @PostMapping("/verify-code")
    public Map<String, String> verifyCode(@RequestBody Map<String, String> verificationDetails) {
        return userService.verifyCode(verificationDetails);
    }

    @GetMapping("/get-user-details")
    public Map<String, Object> getUserDetails(@RequestHeader("Authorization") String token) {
        return userService.getUserDetails(token);
    }


    @GetMapping("/reset-password/{email}&{username}")
    public BasicResponse resetPasswordForThisUser(@PathVariable String email, @PathVariable String username) {
        return userService.resetPasswordForThisUser(email,username);
    }


    @PostMapping("/add-profile-pic")
    public BasicResponse addProfilePicture(@RequestBody Map<String, String> addPicProfile) {
        return userService.addProfilePicture(addPicProfile);
    }

    @GetMapping("/get-all-user-names-and-pic")
    public UserNamesWithPicResponse getAllUserNamesAndPic() {
        return userService.getAllUserNamesAndPic();
    }

    @DeleteMapping("/delete-user/{username}&{password}")
    public BasicResponse deleteUser(@PathVariable String username, @PathVariable String password){
        return userService.deleteUser(username,password);
    }

}

