package com.booking.passwordHistory;

import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.booking.passwordHistory.repository.Constants.THREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PasswordHistoryServiceTest {

    private PasswordHistoryRepository passwordHistoryRepository;

    private PasswordHistoryService passwordHistoryService;

    @BeforeEach
    public void setUp() {
        passwordHistoryRepository = mock(PasswordHistoryRepository.class);
        passwordHistoryService = new PasswordHistoryService(passwordHistoryRepository);
    }

    @Test
    void shouldBeAbleReturnRecentPasswords() {
        User user = new User("test-user", "Password@123");
        List<PasswordHistory> passwords = new ArrayList<>();
        Timestamp instant = Timestamp.from(Instant.now());
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password1"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password2"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password3"), instant));
        when(passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue())).thenReturn(passwords);

        List<PasswordHistory> recentPasswords = passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE);

        assertEquals(3, recentPasswords.size());

        verify(passwordHistoryRepository, times(1)).findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue());
    }
}
