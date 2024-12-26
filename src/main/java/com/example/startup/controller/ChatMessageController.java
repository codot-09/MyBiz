package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.payload.ApiResponse;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatMessage")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/createChat/{receiverId}")
    @Operation(summary = "Chat yaratish")
    public ResponseEntity<ApiResponse> createChat(
            @CurrentUser User user,
            @PathVariable Long receiverId
    ){
        return ResponseEntity.ok(chatMessageService.createChat(user, receiverId));
    }

    @PostMapping("/sendMessage/{chatId}")
    @Operation(summary = "Xabar yuborish")
    public ResponseEntity<ApiResponse> createMessage(
            @PathVariable Long chatId,
            @RequestParam String text
    ){
        return ResponseEntity.ok(chatMessageService.createMessage(chatId, text));
    }

    @GetMapping("/getChats")
    @Operation(summary = "Chatlar ro'yxatini ko'rish")
    public ResponseEntity<ApiResponse> getChats(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(chatMessageService.getChatsByUser(user));
    }

    @GetMapping("/getMessages/{chatId}")
    @Operation(summary = "Chat ichidagi messagelarni ko'rish")
    public ResponseEntity<ApiResponse> getMessages(
            @PathVariable Long chatId
    ){
        return ResponseEntity.ok(chatMessageService.getMessages(chatId));
    }

    @DeleteMapping("/deleteChat/{chatId}")
    @Operation(summary = "Chatni o'chirish")
    public ResponseEntity<ApiResponse> deleteChat(
            @CurrentUser User user,
            @PathVariable Long chatId
    ){
        return ResponseEntity.ok(chatMessageService.deleteChat(user, chatId));
    }

    @DeleteMapping("/deleteMessages")
    @Operation(summary = "Xabarlarni o'chirish")
    public ResponseEntity<ApiResponse> deleteMessages(
            @RequestParam List<Long> messageIds
            ){
        return ResponseEntity.ok(chatMessageService.deleteMessages(messageIds));
    }
}
