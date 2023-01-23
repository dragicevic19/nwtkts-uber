package com.nwtkts.uber.controller;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.nwtkts.uber.dto.MessageDTO;
import com.nwtkts.uber.dto.ReceivedMessageDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.MessageRepository;
import com.nwtkts.uber.service.ChatService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.facebook.api.Page;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping(path = "/newMessage")
    public ResponseEntity<?> newMessage(Principal user, @RequestBody ReceivedMessageDTO messageDTO) {
        User loggedInUser = this.userService.findByEmail(user.getName());

        if (loggedInUser == null || loggedInUser.getId() != messageDTO.getSenderId())
            throw new BadRequestException("Bad user");

        Message message = this.chatService.newMessage(loggedInUser, messageDTO);
        this.simpMessagingTemplate.convertAndSend("/map-updates/chat/new-message", new MessageDTO(message));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(
            path = "/getAllMessagesForUser/{userId}",
            produces = "application/json"
    )
    public ResponseEntity<List<MessageDTO>>getAllMessagesForUser(@PathVariable Long userId) throws AccessDeniedException {
        List<MessageDTO> retList = new ArrayList<>();
        User user = this.userService.findById(userId);
        if (!(user instanceof Driver) && !(user instanceof Client)) throw new BadRequestException("Bad user");

        List<Message> messagesForUser = this.chatService.getAllMessagesForUser(user);
        for (Message message : messagesForUser) {
            retList.add(new MessageDTO(message));
        }

        return new ResponseEntity<>(retList, HttpStatus.OK);
    }
//
//    @GetMapping(
//            path = "/getAllMessagesWithAdmins/{userId}",
//            produces = "application/json"
//    )
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<List<MessageDTO>>getAllMessagesWithUser(Principal user, @PathVariable Long userId) {
//        List<MessageDTO> retList = new ArrayList<>();
//        User loggedInUser = this.userService.findByEmail(user.getName());
//        if (!(loggedInUser instanceof Admin)) throw new BadRequestException("Bad user");
//
//        List<Message> messagesForUser = this.chatService.getAllMessagesForAdminWithUser(loggedInUser, userId);
//        for (Message message : messagesForUser) {
//            retList.add(new MessageDTO(message));
//        }
//        return new ResponseEntity<>(retList, HttpStatus.OK);
//    }

}
