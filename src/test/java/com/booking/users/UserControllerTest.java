package com.booking.users;

import com.booking.users.view.ChangePasswordRequest;
import com.booking.users.view.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Mockito.*;


class UserControllerTest {

    private UserPrincipalService userPrincipalService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userPrincipalService = mock(UserPrincipalService.class);
        userController = new UserController(userPrincipalService);
    }

    @Test
    void shouldAbleToUpdatePasswordSuccessfully() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("old@Password1", "new@Password1");
        Principal principal = () -> "test-user";

        userController.changePassword(principal, changePasswordRequest);

        verify(userPrincipalService, times(1)).changePassword(principal.getName(), changePasswordRequest);
    }
}