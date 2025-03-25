package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.OnlineFriendsResponse;
import org.server.socialnetworkserver.services.OnlineFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;



import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class OnlineFriendsController {

    private final OnlineFriendsService onlineFriendsService;
    private final UserRepository userRepository;

    @Autowired
    public OnlineFriendsController(OnlineFriendsService onlineFriendsService, UserRepository userRepository) {
        this.onlineFriendsService = onlineFriendsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/online-friends/sse/{username}")
    public SseEmitter connect(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return onlineFriendsService.connect(user);
    }

    @GetMapping("/online-friends/list/{username}")
    public OnlineFriendsResponse getOnlineFriends(@PathVariable String username) {
        return onlineFriendsService.getInitialOnlineFriends(username);
    }

    @GetMapping("/online-friends/disconnect/{username}")
    public BasicResponse disconnect(@PathVariable String username) {
      return onlineFriendsService.disconnect(username);
    }

}
