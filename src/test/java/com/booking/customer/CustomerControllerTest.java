package com.booking.customer;

import com.booking.exceptions.CustomerNotFoundException;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class CustomerControllerTest {
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
    }

    @Test
    void shouldBeAbleToReturnCustomerDetailsByUsername() throws CustomerNotFoundException {
        CustomerController customerController = new CustomerController(customerService);
        String username = "test-user";
        User user = new User(username, "password", new Role(2L, "Customer"));
        Customer customer = new Customer("test-customer", "example@email.com", "1234567890", user);
        when(customerService.getCustomerByUsername(username)).thenReturn(customer);

        customerController.getCustomerByUsername(username);

        verify(customerService, times(1)).getCustomerByUsername(username);
    }
}
