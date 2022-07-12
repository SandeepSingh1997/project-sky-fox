package com.booking;

import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {
                repository.save(new User("seed-user-1", "foobar"));
            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                repository.save(new User("seed-user-2", "foobar"));
            }
        };
    }
}
