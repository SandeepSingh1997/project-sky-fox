package com.booking.users;

import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.passwordHistory.PasswordHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        List<String> passwords = new ArrayList<>();
        passwords.add("Password@1");
        passwords.add("Password@2");
        passwords.add("Password@3");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(user.getPassword(), "Password@2");

        assertThrows(PasswordMatchesWithLastThreePasswordsException.class, () -> {
            userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
        });
    }

    @Test
    void shouldBeAbleToSaveNewPasswordInPasswordHistoryTableWhenItDoesNotMatchesWithLastThreePasswords() throws Exception {
        User user = new User("test-user", "Password@123");
        List<String> passwords = new ArrayList<>();
        passwords.add("Password@1");
        passwords.add("Password@2");
        passwords.add("Password@3");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(user.getPassword(), "Password@4");

        userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);

        verify(passwordHistoryService, times(1)).add(user.getId(), changePasswordRequest.getNewPassword());
    }
}