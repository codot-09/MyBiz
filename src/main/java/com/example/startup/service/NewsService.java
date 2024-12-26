package com.example.startup.service;

import com.example.startup.entity.New;
import com.example.startup.entity.Subscription;
import com.example.startup.entity.User;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.request.RequestNew;
import com.example.startup.payload.response.ResponseNews;
import com.example.startup.repository.NewRepository;
import com.example.startup.repository.SubscriptionRepository;
import com.example.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewRepository newRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailSenderService emailSenderService;

    public ApiResponse createNew(User user,RequestNew requestNew){
        LocalDate expirationDate = LocalDate.now().plusMonths(1);
        New news = New.builder()
                .title(requestNew.title())
                .text(requestNew.text())
                .user(user)
                .expirationDate(expirationDate)
                .build();
        newRepository.save(news);
        List<Subscription> subscribers = subscriptionRepository.findBySeller(user);
        for (Subscription subscriber : subscribers) {
            String email = subscriber.getCustomer().getEmail(); // Obunachining emaili
            String subject = "Yangi Yangilik!";
            String body = "Sizning sotuvchingiz yangilik e'lo qildi!Tezroq uni o'tib ko'ring balki foydali bo'lar";
            emailSenderService.sendEmail(email, subject, body);
        }
        return new ApiResponse("Yangilik yaratildi",null);
    }

    public ApiResponse getNewsFromSeller(Long sellerId){
        List<New> news = newRepository.findNewByUserId(sellerId);
        if (news.isEmpty()){
            return new ApiResponse("Yangiliklar topilmadi",null);
        }
        List<ResponseNews> responseNews = news.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
        return new ApiResponse(responseNews,null);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredNews() {
        LocalDate today = LocalDate.now();
        List<New> expiredNews = newRepository.findAllByExpirationDate(today);
        newRepository.deleteAll(expiredNews);
    }

    public ApiResponse deleteUserNewsById(Long newsId, User user) {
        New news = newRepository.findByIdAndUser(newsId, user);
        if (news == null) {
            return new ApiResponse("Yangilik topilmadi yoki foydalanuvchiga tegishli emas", null);
        }
        newRepository.delete(news);
        return new ApiResponse("Yangilik o'chirildi", null);
    }

    public ApiResponse getMyNews(User user){
        List<New> news = newRepository.findNewByUserId(user.getId());
        if (news.isEmpty()){
            return new ApiResponse("Sizda yangiliklar yo'q",null);
        }
        List<ResponseNews> responseNews = news.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
        return new ApiResponse(responseNews,null);
    }

    private ResponseNews mapper(New news){
        return new ResponseNews(
                news.getId(),
                news.getTitle(),
                news.getText(),
                news.getUser().getId()
        );
    }
}
