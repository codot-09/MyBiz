package com.example.startup.service;

import com.example.startup.entity.Subscription;
import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.response.ResponseSubscription;
import com.example.startup.payload.response.ResponseUser;
import com.example.startup.repository.SubscriptionRepository;
import com.example.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;

    public ApiResponse showProfile(User user) {
        return new ApiResponse(mapper(user),null);
    }

    public ApiResponse search(String fullname) {
        List<User> users = userRepository.findByFullnameContainingIgnoreCase(fullname);
        if (users.isEmpty()) {
            return new ApiResponse("Foydalanuvchilar topilmadi!",null);
        }
        List<ResponseUser> responseUsers = users.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
        return new ApiResponse(responseUsers,null);
    }

    public ApiResponse delete(User user) {
        userRepository.delete(user);
        return new ApiResponse("Hisob o'chirildi",null);
    }

    public ApiResponse changeStatus(Long id, boolean enabled) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return new ApiResponse("Foydalanuvchi topilmadi",null);
        user.setBlocked(enabled);
        userRepository.save(user);
        return new ApiResponse("Status o'zgartirildi",null);
    }

    public ApiResponse resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse("Parol muvaffaqiyatli yangilandi",null);
    }

    public ApiResponse subscribe(User customer,Long sellerId){
        User user = userRepository.findById(sellerId).orElse(null);
        if (user == null){
            return new ApiResponse("Sotuvchi topilmadi",null);
        }
        Subscription subscription = Subscription.builder()
                .customer(customer)
                .seller(user)
                .build();
        subscriptionRepository.save(subscription);
        return new ApiResponse("Obuna qilindi",null);
    }

    public ApiResponse showSubscribers(User user){
        if (user.getRole().equals(UserRole.ROLE_CUSTOMER)){
            List<Subscription> subs = subscriptionRepository.findByCustomer(user);
            if (subs.isEmpty()) {
                return new ApiResponse("Sizda obunalar yo'q",null);
            }
            List<ResponseSubscription> responseSubscriptions = subs.stream()
                    .map(subscription -> new ResponseSubscription(
                            subscription.getId(),
                            subscription.getSeller().getFullname(),
                            subscription.getSeller().getEmail()
                    ))
                    .collect(Collectors.toList());
            return new ApiResponse(responseSubscriptions,null);
        } else if (user.getRole().equals(UserRole.ROLE_SELLER)) {
            List<Subscription> subs = subscriptionRepository.findBySeller(user);
            if (subs.isEmpty()) {
                return new ApiResponse("Sizda obunachilar yo'q",null);
            }
            List<ResponseSubscription> responseSubscriptions = subs.stream()
                    .map(subscription -> new ResponseSubscription(
                            subscription.getId(),
                            subscription.getCustomer().getFullname(),
                            subscription.getCustomer().getEmail()
                    ))
                    .collect(Collectors.toList());
            return new ApiResponse(responseSubscriptions,null);
        }
        return new ApiResponse("Xatolik",null);
    }


    public ApiResponse deleteSubscription(Long subscriptionId){
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if (subscription == null){
            return new ApiResponse("Obuna topilmadi",null);
        }
        subscriptionRepository.delete(subscription);
        return new ApiResponse("Obuna bekor qilindi",null);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Har kuni 00:00 da ishga tushadi
    public void deleteBlockedUsers() {
        List<User> blockedUsers = userRepository.findAllByBlockedTrue();
        if (!blockedUsers.isEmpty()) {
            userRepository.deleteAll(blockedUsers);
            System.out.println(blockedUsers.size() + " ta blocked foydalanuvchi o'chirildi.");
        } else {
            System.out.println("Blocked foydalanuvchilar topilmadi.");
        }
    }

    private ResponseSubscription subMapper(Subscription subscription){
        return new ResponseSubscription(
                subscription.getId(),
                subscription.getCustomer().getFullname(),
                subscription.getCustomer().getEmail()
        );
    }

    private ResponseUser mapper(User user) {
        return new ResponseUser(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
