package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.request.RequestNew;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(summary = "Yangilik yaratish,faqat sotuvchi uchun")
    public ResponseEntity<ApiResponse> create(
            @CurrentUser User user,
            @RequestBody RequestNew requestNew
            ){
        return ResponseEntity.ok(newsService.createNew(user, requestNew));
    }

    @GetMapping("/getNews/{sellerId}")
    @Operation(summary = "Sotuvchining yangiliklarini ko'rish")
    public ResponseEntity<ApiResponse> getNews(
            @PathVariable Long sellerId
    ){
        return ResponseEntity.ok(newsService.getNewsFromSeller(sellerId));
    }

    @DeleteMapping("/delete/{newsId}")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(summary = "Har bir sotuvchi o'z yangiligini o'chirish")
    public ResponseEntity<ApiResponse> delete(
            @CurrentUser User user,
            @PathVariable Long newsId
    ){
        return ResponseEntity.ok(newsService.deleteUserNewsById(newsId, user));
    }

    @GetMapping("/getMyNews")
    @Operation(summary = "Har bir user o'z yangiliklarini ko'rish")
    public ResponseEntity<ApiResponse> get(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(newsService.getMyNews(user));
    }
}
