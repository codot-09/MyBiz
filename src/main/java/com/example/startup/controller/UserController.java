package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.payload.ApiResponse;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Har bir user o'z profilini ko'rish")
    public ResponseEntity<ApiResponse> showProfile(
            @CurrentUser User user
            ){
        return ResponseEntity.ok(userService.showProfile(user));
    }

    @GetMapping("/search")
    @Operation(summary = "Foydalanuvchilarni qidirish hamma uchun")
    public ResponseEntity<ApiResponse> search(
            @RequestParam String fullname
    ){
        return ResponseEntity.ok(userService.search(fullname));
    }

    @DeleteMapping("/logout")
    @Operation(summary = "Har bir user logout qilish")
    public ResponseEntity<ApiResponse> delete(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(userService.delete(user));
    }

    @PutMapping("/changeStatus/{userId}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Admin va Super Admin foydalanuvchi statusini o'zgartirish")
    public ResponseEntity<ApiResponse> change(
            @PathVariable Long userId,
            @RequestParam boolean blocked
    ){
        return ResponseEntity.ok(userService.changeStatus(userId, blocked));
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "Parolni qayta tiklash")
    public ResponseEntity<ApiResponse> resetPassword(
            @CurrentUser User user,
            @RequestParam String newPassword
    ){
        return ResponseEntity.ok(userService.resetPassword(user, newPassword));
    }

    @PostMapping("/subscribe/{sellerId}")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "xaridorlar obuna tizimi")
    public ResponseEntity<ApiResponse> subscribe(
            @CurrentUser User user,
            @PathVariable Long sellerId
    ){
        return ResponseEntity.ok(userService.subscribe(user, sellerId));
    }

    @GetMapping("/showSubs")
    @Operation(summary = "Obuna va obunachilarni ko'rish")
    public ResponseEntity<ApiResponse> showSubscribes(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(userService.showSubscribers(user));
    }

    @DeleteMapping("/ignoreSub/{subscribeId}")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Obunani bekor qilish")
    public ResponseEntity<ApiResponse> ignoreSubscribe(
            @PathVariable Long subscribeId
    ){
        return ResponseEntity.ok(userService.deleteSubscription(subscribeId));
    }
}
