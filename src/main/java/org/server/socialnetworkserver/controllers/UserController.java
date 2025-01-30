package org.server.socialnetworkserver.controllers;
import org.server.socialnetworkserver.responses.*;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/get-num-of-users")
    public BasicResponse getNumOfUsers(){
        return userService.getNumOfUsers();
    }

    @PostMapping("/validateToken")
    public ResponseEntity<TokenResponse> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            System.out.println("Token missing");
            return ResponseEntity.badRequest().body(new TokenResponse(false, "Token is missing", false,null));
        }

        String cleanToken = token.replace("Bearer ", "");

        TokenResponse response = userService.validateToken(cleanToken);
        return ResponseEntity.ok(response);
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

    @PostMapping(value = "/add-profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BasicResponse addProfilePicture(
            @RequestParam("username") String username,
            @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePictureFile,
            @RequestParam(value = "profilePictureUrl", required = false) String profilePictureUrl) {

        return userService.addProfilePicture(username, profilePictureFile, profilePictureUrl);
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

