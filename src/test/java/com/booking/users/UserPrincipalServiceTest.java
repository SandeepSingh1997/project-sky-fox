package com.booking.users;

import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.passwordHistory.PasswordHistoryService;
import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.booking.passwordHistory.repository.Constants.THREE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserPrincipalServiceTest {

    private UserRepository userRepository;
    private UserPrincipalService userPrincipalService;
    private PasswordHistoryService passwordHistoryService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordHistoryService = mock(PasswordHistoryService.class);
        userPrincipalService = new UserPrincipalService(userRepository, passwordHistoryService);
    }

    @Test
    void shouldBeAbleToChangePasswordInRepository() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@Password1", "New@Password1");
        User user = new User("username", changePasswordRequest.getCurrentPassword());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnExceptionWhenReceivedCurrentPasswordDoesNotMatchTheCurrentPasswordInTheRepository() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@Password1", "New@Password1");
        User user = new User("username", "differentCurrent@Password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertThrows(PasswordMismatchException.class, () -> {
            userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
        });
    }

    @Test
    void shouldNotBeAbleToChangePasswordWhenNewPasswordMatchesWithAnyOneOfPreviousThreePasswords() throws PasswordMismatchException {
        User user = new User("test-user", "Password@123");
        List<PasswordHistory> passwords = new ArrayList<>();
        Timestamp instant = Timestamp.from(Instant.now());
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password1"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password2"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password3"), instant));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(user.getPassword(), "Password2");

        assertThrows(PasswordMatchesWithLastThreePasswordsException.class, () -> {
            userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
        });
    }

    @Test
    void shouldBeAbleToSaveNewPasswordInPasswordHistoryTableWhenItDoesNotMatchesWithLastThreePasswords() throws Exception {
        User user = new User("test-user", "Password@123");
        List<PasswordHistory> passwords = new ArrayList<>();
        Timestamp instant = Timestamp.from(Instant.now());
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@1"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@2"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@3"), instant));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(user.getPassword(), "Password@4");

        userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);

        verify(passwordHistoryService, times(1)).add(user.getId(), changePasswordRequest.getNewPassword());
    }
}