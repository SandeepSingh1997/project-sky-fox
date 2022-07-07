package com.booking.users;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPrincipalServiceTest {

    @Test
    void shouldBeAbleToChangePasswordInRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(userRepository);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@Password1", "New@Password1");
        User user = new User("username", changePasswordRequest.getOldPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userPrincipalService.changePassword("username", changePasswordRequest);

        verify(userRepository, times(1)).save(user);
    }
}