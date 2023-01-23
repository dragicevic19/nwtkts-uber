package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySender_IdOrReceiver_Id(Long senderId, Long receiverId);

}
