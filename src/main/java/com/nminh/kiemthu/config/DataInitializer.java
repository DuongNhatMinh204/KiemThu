package com.nminh.kiemthu.config;

import com.nminh.kiemthu.entity.User;
import com.nminh.kiemthu.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner createDefaultUsers(UserRepository userRepo) {
        return args -> {
            if (!userRepo.existsByUsername("truongkhoa")) {
                User u = new User();
                u.setUsername("truongkhoa");
                u.setPassword("123456");
                u.setRoleName("truongkhoa");
                userRepo.save(u);
            }
            if (!userRepo.existsByUsername("ketoan")) {
                User u = new User();
                u.setUsername("ketoan");
                u.setPassword("123456");
                u.setRoleName("ketoan");
                userRepo.save(u);
            }
        };
    }
}
