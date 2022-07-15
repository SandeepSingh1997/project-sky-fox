package com.booking;

import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {
                User user = new User("seed-user-1", "Foobar@123", new Role("Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                repository.save(user);
            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                User user = new User("seed-user-2", "Foobar@124", new Role("Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                repository.save(user);
            }
            if (repository.findByUsername("seed-user-3").isEmpty()) {
                User user = new User("seed-user-3", "Foobar@125", new Role("Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                repository.save(user);
            }
        };
    }
}
