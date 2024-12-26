package com.example.startup.component;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    @Value(value = "${spring.jpa.hibernate.ddl-auto}")

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .fullname("Otabek")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .blocked(false)
                    .role(UserRole.ROLE_SUPER_ADMIN)
                    .build();
            userRepository.save(user1);
        }
    }
}
