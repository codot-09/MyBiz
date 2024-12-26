package com.example.startup.service;

import com.example.startup.entity.Chat;
import com.example.startup.entity.Message;
import com.example.startup.entity.User;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.response.ResponseChat;
import com.example.startup.payload.response.ResponseMessage;
import com.example.startup.repository.ChatRepository;
import com.example.startup.repository.MessageRepository;
import com.example.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ApiResponse createChat(User user, Long receiverId) {
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (receiver == null) {
            return new ApiResponse("Qabul qiluvchi topilmadi", null);
        }
        Chat existingChat = chatRepository.findByReceiverAndSenderOrSenderAndReceiver(user, receiver, user, receiver);
        if (existingChat != null) {
            return new ApiResponse("Chat allaqachon mavjud", null);
        }
        Chat newChat = new Chat();
        newChat.setSender(user);
        newChat.setReceiver(receiver);
        chatRepository.save(newChat);
        return new ApiResponse("Chat yaratildi", null);
    }

    public ApiResponse getChatsByUser(User user) {
        List<Chat> chats = chatRepository.findBySenderOrReceiver(user, user);
        if (chats.isEmpty()) {
            return new ApiResponse("Sizda chatlar mavjud emas", null);
        }
        List<ResponseChat> receivers = chats.stream()
                .map(this::chatMapper)
                .toList();
        return new ApiResponse(receivers,null);
    }

    public ApiResponse deleteChat(User user,Long chatId){
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null){
            return new ApiResponse("Chat topilmadi",null);
        }
        else if (!chat.getSender().equals(user) && !chat.getReceiver().equals(user)) {
            return new ApiResponse("Sizga tegishli bo'lmagan chatni o'chira olmaysiz", null);
        }
        chatRepository.delete(chat);
        return new ApiResponse("Chat o'chirildi",null);
    }

    public ApiResponse createMessage(Long chatId,String text){
        Chat chat = chatRepository.findById(chatId).orElse(null);
        Message message = Message.builder()
                .chat(chat)
                .text(text)
                .build();
        messageRepository.save(message);
        return new ApiResponse("Yuborildi",null);
    }

    public ApiResponse getMessages(Long chatId){
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null){
            return new ApiResponse("Chat topilmadi",null);
        }
        List<Message> messages = messageRepository.findByChatId(chatId);
        if (messages.isEmpty()){
            return new ApiResponse("Hozircha xabarlar yo'q",null);
        }
        List<ResponseMessage> responseMessages = messages.stream()
                .map(this::mapper)
                .toList();
        return new ApiResponse(responseMessages,null);
    }

    public ApiResponse deleteMessages(List<Long> messageIds){
        List<Message> messages = messageRepository.findAllById(messageIds);
        if (messages.isEmpty()) {
            return new ApiResponse("Hech qanday xabar topilmadi", null);
        }
        messageRepository.deleteAll(messages);
        return new ApiResponse("Xabarlar o'chirildi", null);
    }

    private ResponseMessage mapper(Message message){
        return new ResponseMessage(
                message.getText(),
                message.getCreatedAt()
        );
    }

    private ResponseChat chatMapper(Chat chat){
        return new ResponseChat(
                chat.getId(),
                chat.getReceiver().getFullname()
        );
    }
}
