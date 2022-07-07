package com.booking.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Mockito.*;


class UserControllerTest {

    UserPrincipalService userPrincipalService;

    @BeforeEach
    void setUp() {
        userPrincipalService = mock(UserPrincipalService.class);
    }

    @Test
    void shouldAbleToUpdatePasswordSuccessfully() {

        UserController userController = new UserController(userPrincipalService);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("old@Password1", "new@Password1");
        Principal principal = () -> "test-user";

        userController.changePassword(principal, changePasswordRequest);

        verify(userPrincipalService, times(1)).changePassword(principal.getName(), changePasswordRequest);
    }
}