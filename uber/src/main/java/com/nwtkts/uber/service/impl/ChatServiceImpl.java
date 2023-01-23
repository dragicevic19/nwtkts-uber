package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.MessageDTO;
import com.nwtkts.uber.dto.ReceivedMessageDTO;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Message;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.MessageRepository;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.ChatService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message newMessage(User sender, ReceivedMessageDTO messageDTO) {
        Message message = new Message(sender, messageDTO.getText(), LocalDateTime.now());

        if (messageDTO.getReceiverId() != null) {
            User receiver = this.userRepository.findById(messageDTO.getReceiverId()).orElseThrow(
                    () -> new NotFoundException("Receiver doesn't exist"));
            message.setReceiver(receiver);
        }
        return this.messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessagesForUser(User loggedInUser) {
        return this.messageRepository.findAllBySender_IdOrReceiver_Id(loggedInUser.getId(), loggedInUser.getId());
    }

    @Override
    public List<Message> getAllMessagesForAdminWithUser(User loggedInUser, Long userId) {
        return this.messageRepository.findAllBySender_IdOrReceiver_Id(userId, userId);
    }
}
