package com.booking.customer;

import com.booking.exceptions.CustomerNotFoundException;
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

        customerController.getCustomerByUsername(username);

        verify(customerService, times(1)).getCustomerByUsername(username);
    }
}
