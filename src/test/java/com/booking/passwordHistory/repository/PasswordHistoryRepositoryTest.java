package com.booking.passwordHistory.repository;

import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.booking.passwordHistory.repository.Constants.THREE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class PasswordHistoryRepositoryTest {

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User("test-user", "Password1");
        userRepository.save(user);
    }

    @AfterEach
    public void afterEach() {
        passwordHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToReturnRecentThreePasswords() {
        List<PasswordHistory> passwords = new ArrayList<>();
        Timestamp instant = Timestamp.from(Instant.now());
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password1"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password2"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password3"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password4"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password5"), instant));

        passwordHistoryRepository.saveAll(passwords);
        List<String> recentPasswords = passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue());

        assertEquals(3, recentPasswords.size());
    }
}
