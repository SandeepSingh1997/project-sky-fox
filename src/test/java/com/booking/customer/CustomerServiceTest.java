package com.booking.customer;


import com.booking.exceptions.CustomerNotFoundException;
import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.roles.repository.Role;
import com.booking.users.UserPrincipalService;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private UserPrincipalService userPrincipalService;
    private CustomerService customerService;

    @BeforeEach
    public void beforeEach() {
        customerRepository = mock(CustomerRepository.class);
        userPrincipalService = mock(UserPrincipalService.class);
        customerService = new CustomerService(customerRepository, userPrincipalService);
    }

    @Test

    public void should_save_customer() throws UsernameAlreadyExistsException {
        User user = new User("test-user", "foobar", new Role(2L, "Customer"));
        Customer customer = new Customer("test-name", "Axyz@gmail.com", "1234567890", user);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer actualCustomer = customerService.signup(customer);

        verify(customerRepository).save(customer);
        assertThat(actualCustomer, is(equalTo(customer)));

    }

    @Test
    public void should_save_customer_credentials_in_user() throws UsernameAlreadyExistsException {
        User user = new User("test-user", "foobar", new Role(2L, "Customer"));
        Customer customer = new Customer("test-name", "Axyz@gmail.com", "1234567890", user);
        when(userPrincipalService.add(user)).thenReturn(user);

        customerService.signup(customer);

        verify(userPrincipalService).add(user);

    }

    @Test
    public void should_not_save_customer_when_username_already_exists() throws UsernameAlreadyExistsException {
        User user1 = new User("test-user", "foobar", new Role(2L, "Customer"));
        Customer customer1 = new Customer("test-name", "Axyz@gmail.com", "1234567890", user1);
        customerService.signup(customer1);
        User user2 = new User("test-user", "foobar", new Role(2L, "Customer"));
        Customer customer2 = new Customer("test-name", "Axyz@gmail.com", "1234567890", user2);
        when(userPrincipalService.add(customer2.getUser())).thenThrow(new UsernameAlreadyExistsException());

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            customerService.signup(customer2);
        });
    }

    @Test
    void shouldBeAbleToReturnCustomerDetailsByUsername() throws CustomerNotFoundException {
        String username = "test-user";
        User user = new User("test-user", "password", new Role(2L,"Customer"));
        when(userPrincipalService.findUserByUsername(username)).thenReturn(user);
        Customer customer = new Customer("test-customer", "example@email.com", "1234567890", user);
        when(customerRepository.findByUserId(user.getId())).thenReturn(Optional.of(customer));

        Customer customerByUsername = customerService.getCustomerByUsername(username);

        assertEquals(customerByUsername.getId(), customer.getId());
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenUserIsNotFoundWithTheUsername() {
        String username = "test-user";
        when(userPrincipalService.findUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, ()-> customerService.getCustomerByUsername(username));
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenCustomerIsNotFoundWithUserId() {
        String username = "test-user";
        User user = new User("test-user", "password", new Role(2L,"Customer"));
        when(userPrincipalService.findUserByUsername(username)).thenReturn(user);
        when(customerRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByUsername(username));
    }
}
