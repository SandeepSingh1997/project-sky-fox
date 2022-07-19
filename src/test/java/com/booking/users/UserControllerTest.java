package com.booking.users;

import com.booking.exceptions.CustomerNotFoundException;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
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

    @Test
    void shouldBeAbleToGetUserDetailsById() throws CustomerNotFoundException {
        User user = new User("test-user", "Password@123", new Role(1L, "Admin"));
        Principal principal = () -> "test-user";

        userController.getUserDetailsById(user.getId());

        verify(userPrincipalService, times(1)).getUserDetailsById(user.getId());
    }
}