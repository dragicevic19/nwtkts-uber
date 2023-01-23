package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.ChatListItemDTO;
import com.nwtkts.uber.dto.ReceivedMessageDTO;
import com.nwtkts.uber.model.Message;
import com.nwtkts.uber.model.User;

import java.util.Collection;
import java.util.List;

public interface ChatService {

    Message newMessage(User sender, ReceivedMessageDTO messageDTO);

    List<Message> getAllMessagesForUser(User loggedInUser);

    List<Message> getAllMessagesForAdminWithUser(User loggedInUser, Long userId);

    Collection<ChatListItemDTO> getAllMessagesForAdminGrouped();
}
