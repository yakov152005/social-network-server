package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.repositoris.MessageRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

}
