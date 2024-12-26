package com.example.startup.repository;

import com.example.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByCode(Integer code);
    @Query("SELECT u FROM User u WHERE LOWER(u.fullname) LIKE LOWER(CONCAT('%', :fullname, '%'))")
    List<User> findByFullnameContainingIgnoreCase(@Param("fullname") String fullname);
    List<User> findAllByBlockedTrue();
}
