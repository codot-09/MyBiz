package com.example.startup.repository;

import com.example.startup.entity.Chat;
import com.example.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
//    Chat findBySenderAndReceiver(User user1, User user2);
    List<Chat> findBySenderOrReceiver(User sender, User receiver);
    Chat findByReceiverAndSenderOrSenderAndReceiver(User user1, User user2, User user2Rev, User user1Rev);
}
