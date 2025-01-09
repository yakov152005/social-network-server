package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.controllers.StreamController;
import org.server.socialnetworkserver.dtos.MessageDto;
import org.server.socialnetworkserver.entitys.Message;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.MessageRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.dtos.ChatUserDto;
import org.server.socialnetworkserver.responses.ChatUserResponse;
import org.server.socialnetworkserver.responses.MessageDtoResponse;
import org.server.socialnetworkserver.responses.MessageResponse;
import org.server.socialnetworkserver.utils.ApiChatGpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final StreamController streamController;

    @Autowired
    public MessageService(MessageRepository messageRepository,UserRepository userRepository, StreamController streamController){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.streamController = streamController;
    }


    public MessageDtoResponse getMessagesByUser(String sender,String receiver){
        List<Message> messages = messageRepository.findMessagesBetweenUsers(sender,receiver);
        if (messages == null){
            return new MessageDtoResponse(false,"No find message.",null);
        }

        List<MessageDto> messageDtos = messages.stream()
                .map(message -> new MessageDto(
                        message.getId(),
                        message.getSender().getUsername(),
                        message.getReceiver().getUsername(),
                        message.getReceiver().getProfilePicture(),
                        message.getContent(),
                        message.isRead(),
                        message.getSentAt()
                )).toList();



        return new MessageDtoResponse(true,"All message history send.",messageDtos);
    }


    public MessageResponse sendMessage(String senderUsername, String receiverUsername, String content){
        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (sender == null || receiver == null){
            return new MessageResponse(false,"Sender or Receiver not found.",null);
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        messageRepository.save(message);

        MessageDto messageDto = new MessageDto(
                message.getId(),
                message.getSender().getUsername(),
                message.getReceiver().getUsername(),
                message.getReceiver().getProfilePicture(),
                message.getContent(),
                message.isRead(),
                message.getSentAt()
        );


        if ("chatGpt".equals(receiverUsername)) {
            try {
                String chatGptResponse = ApiChatGpt.getResponseFromServer(content);
                sendMessage("chatGpt", senderUsername, chatGptResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return new MessageResponse(false, "Failed to get response from ChatGPT.", null);
            }
        }

        streamController.notifyUser(receiverUsername,messageDto);
        return new MessageResponse(true,"Message send successfully.",messageDto);
    }

    public ChatUserResponse getChatUsers(String username) {
        List<ChatUserDto> chatUserDtos = messageRepository.findChatUsers(username);
        if (chatUserDtos == null){
            return new ChatUserResponse(false,"No chat user founded.",null);
        }
        return new ChatUserResponse(true,"All chats with profile pic send.",chatUserDtos);
    }



}
