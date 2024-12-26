package com.example.startup.repository;

import com.example.startup.entity.New;
import com.example.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NewRepository extends JpaRepository<New,Long> {
    List<New> findNewByUserId(Long userId);
    List<New> findAllByExpirationDate(LocalDate expirationDate);
    New findByIdAndUser(Long newsId, User user);
}
