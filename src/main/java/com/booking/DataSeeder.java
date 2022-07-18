package com.booking;

import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;


@Configuration
public class DataSeeder {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordHistoryRepository  passwordHistoryRepository) {

        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {

                User user = new User("seed-user-1", "Foobar@123", new Role(1L,"Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(user, user.getPassword());
                Timestamp instant = Timestamp.from(Instant.now());
                PasswordHistory passwordHistory = new PasswordHistory(passwordHistoryPK, instant);
                repository.save(user);
                passwordHistoryRepository.save(passwordHistory);

            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                User user = new User("seed-user-2", "Foobar@124", new Role(1L,"Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(user, user.getPassword());
                Timestamp instant = Timestamp.from(Instant.now());
                PasswordHistory passwordHistory = new PasswordHistory(passwordHistoryPK, instant);
                repository.save(user);
                passwordHistoryRepository.save(passwordHistory);

            }
            if (repository.findByUsername("seed-user-3").isEmpty()) {
                User user = new User("seed-user-3", "Foobar@125", new Role(1L,"Admin"));
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

                PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(user, user.getPassword());
                Timestamp instant = Timestamp.from(Instant.now());
                PasswordHistory passwordHistory = new PasswordHistory(passwordHistoryPK, instant);
                repository.save(user);
                passwordHistoryRepository.save(passwordHistory);
            }
        };
    }
}
