package com.booking.customer;

import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private UserRepository userRepository;
    private CustomerService customerService;

    @BeforeEach
    public void beforeEach() {
        customerRepository = mock(CustomerRepository.class);
        userRepository = mock(UserRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void should_save_customer() {
        User user = new User("test-user", "foobar", new Role("Admin"));
        Customer customer = new Customer("test-name", "Axyz@gmail.com", "1234567890", user);
        Customer mockCustomer = mock(Customer.class);
        when(customerRepository.save(customer)).thenReturn(mockCustomer);

        Customer actualCustomer = customerService.signup(customer);

        verify(customerRepository).save(customer);
        assertThat(actualCustomer, is(equalTo(mockCustomer)));

    }
}
