package com.example.startup.repository;

import com.example.startup.entity.Subscription;
import com.example.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findByCustomer(User customer);
    List<Subscription> findBySeller(User seller);
}
