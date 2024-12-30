package org.server.socialnetworkserver.controllers;

import org.server.socialnetworkserver.responses.MessageDtoResponse;
import org.server.socialnetworkserver.responses.MessageResponse;
import org.server.socialnetworkserver.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.server.socialnetworkserver.utils.Constants.UrlClient.URL_SERVER;

@RestController
@RequestMapping(URL_SERVER)
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/get-message-history/{sender}&{receiver}")
    public MessageDtoResponse getMessagesByUser(@PathVariable String sender ,@PathVariable String receiver) {
        return messageService.getMessagesByUser(sender,receiver);
    }

    @PostMapping("/send-message")
    public MessageResponse sendMessage(@RequestParam String senderUsername,
                                       @RequestParam String receiverUsername,
                                       @RequestParam String content){
        return messageService.sendMessage(senderUsername,receiverUsername,content);
    }

    @GetMapping("/get-chat-users/{username}")
    public List<String> getChatUsers(@PathVariable String username) {
        return messageService.getChatUsers(username);
    }



}
