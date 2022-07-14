package com.booking.passwordHistory;

import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.booking.passwordHistory.repository.Constants.THREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PasswordHistoryServiceTest {

    private PasswordHistoryRepository passwordHistoryRepository;

    private UserRepository userRepository;

    private PasswordHistoryService passwordHistoryService;

    private User user;
    private PasswordHistory passwordHistory;

    @BeforeEach
    public void setUp() {
        passwordHistoryRepository = mock(PasswordHistoryRepository.class);
        userRepository = mock(UserRepository.class);
        passwordHistoryService = new PasswordHistoryService(passwordHistoryRepository, userRepository);
        user = new User("test-user", "Password@123", new Role("Admin"));
        Timestamp instant = Timestamp.from(Instant.now());
        passwordHistory = new PasswordHistory(new PasswordHistoryPK(user, "Password@12"), instant);
    }

    @Test
    void shouldBeAbleReturnRecentPasswords() {
        User user = new User("test-user", "Password@123", new Role("Admin"));
        List<String> passwords = new ArrayList<>();
        passwords.add("Password@1");
        passwords.add("Password@2");
        passwords.add("Password@3");

        when(passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue())).thenReturn(passwords);

        List<String> recentPasswords = passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE);

        assertEquals(3, recentPasswords.size());

        verify(passwordHistoryRepository, times(1)).findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue());
    }

    @Test
    void shouldBeAbleToSavePassword() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordHistoryRepository.save(passwordHistory)).thenReturn(any(PasswordHistory.class));

        passwordHistoryService.add(user.getId(), passwordHistory.getPasswordHistoryPK().getPassword());

        verify(passwordHistoryRepository, times(1)).save(any(PasswordHistory.class));
    }

    @Test
    void shouldBeAbleToThrowWhenInvalidUserIdGiven() {
        User user = new User("test-user", "Password@123", new Role("Admin"));
        when(userRepository.findById(user.getId())).thenThrow(new UsernameNotFoundException("User is not found"));

        assertThrows(UsernameNotFoundException.class,
                () -> passwordHistoryService.add(user.getId(), passwordHistory.getPasswordHistoryPK().getPassword()));
    }
}
