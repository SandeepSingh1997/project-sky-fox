package com.booking.users;

import com.booking.exceptions.PasswordMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserPrincipalServiceTest {

    private UserRepository userRepository;
    private UserPrincipalService userPrincipalService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userPrincipalService = new UserPrincipalService(userRepository);

    }

    @Test
    void shouldBeAbleToChangePasswordInRepository() throws PasswordMismatchException {
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
}