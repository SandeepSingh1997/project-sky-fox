package com.booking.users;

import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.passwordHistory.PasswordHistoryService;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import com.booking.users.view.ChangePasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.booking.passwordHistory.repository.Constants.THREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserPrincipalServiceTest {

    private UserRepository userRepository;
    private UserPrincipalService userPrincipalService;
    private PasswordHistoryService passwordHistoryService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordHistoryService = mock(PasswordHistoryService.class);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userPrincipalService = new UserPrincipalService(userRepository, passwordHistoryService, bCryptPasswordEncoder);
    }

    @Test
    void shouldBeAbleToChangePasswordInRepository() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@Password1", "New@Password1");

        User user = new User("username", "Old@Password1", new Role(1L,"Admin"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnExceptionWhenReceivedCurrentPasswordDoesNotMatchTheCurrentPasswordInTheRepository() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@Password1", "New@Password1");

        User user = new User("username", "differentCurrent@Password", new Role(1L,"Admin"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(PasswordMismatchException.class, () -> {
            userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
        });
    }

    @Test
    void shouldNotBeAbleToChangePasswordWhenNewPasswordMatchesWithAnyOneOfPreviousThreePasswords() throws PasswordMismatchException {

        User user = new User("test-user", "Password@123", new Role(1L,"Admin"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        List<String> passwords = new ArrayList<>();
        passwords.add(bCryptPasswordEncoder.encode("Password@1"));
        passwords.add(bCryptPasswordEncoder.encode("Password@2"));
        passwords.add(bCryptPasswordEncoder.encode("Password@3"));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password@123", "Password@2");

        assertThrows(PasswordMatchesWithLastThreePasswordsException.class, () -> {
            userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
        });
    }

    @Test
    void shouldBeAbleToSaveNewPasswordInPasswordHistoryTableWhenItDoesNotMatchesWithLastThreePasswords() throws Exception {

        User user = new User("test-user", "Password@123", new Role(1L,"Admin"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        List<String> passwords = new ArrayList<>();
        passwords.add(bCryptPasswordEncoder.encode("Password@1"));
        passwords.add(bCryptPasswordEncoder.encode("Password@2"));
        passwords.add(bCryptPasswordEncoder.encode("Password@3"));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE))
                .thenReturn(passwords);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password@123", "Password@4");

        userPrincipalService.changePassword(user.getUsername(), changePasswordRequest);
    }

    @Test
    void shouldBeAbleToGetUserRoleBasedOnUserName() {
        User user = new User("test-user", "Password@123", new Role(2L,"Customer"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        String role=userPrincipalService.getUserRoleName(user.getUsername());
        assertEquals("Customer",role);

    }
}