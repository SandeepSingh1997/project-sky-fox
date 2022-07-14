package com.booking.customer;

import com.booking.bookings.BookingService;
import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.movieAudience.repository.MovieAudienceRepository;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private UserRepository userRepository;
    private CustomerService customerService;

    @BeforeEach
    public void beforeEach() {
        customerRepository = mock(CustomerRepository.class);
        userRepository = mock(UserRepository.class);
        customerService = new CustomerService(customerRepository, userRepository);
    }

    @Test
    public void should_save_customer() throws UsernameAlreadyExistsException {
        User user = new User("test-user","foobar");
        Customer customer = new Customer("test-name", "Axyz@gmail.com", "1234567890", user);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer actualCustomer = customerService.signup(customer);

        verify(customerRepository).save(customer);
        assertThat(actualCustomer, is(equalTo(customer)));

    }

    @Test
    public void should_save_customer_credentials_in_user() throws UsernameAlreadyExistsException {
        User user = new User("test-user","foobar");
        Customer customer = new Customer("test-name", "Axyz@gmail.com", "1234567890", user);
        when(userRepository.save(user)).thenReturn(user);

        customerService.signup(customer);

        verify(userRepository).save(user);

    }

    @Test
    public void should_not_save_customer_when_username_already_exists() throws UsernameAlreadyExistsException {
        User user1 = new User("test-user","foobar");
        Customer customer1 = new Customer("test-name", "Axyz@gmail.com", "1234567890", user1);
        customerService.signup(customer1);
        User user2 = new User("test-user","foobar");
        Customer customer2 = new Customer("test-name", "Axyz@gmail.com", "1234567890", user2);
        when(userRepository.findByUsername(customer2.getUser().getUsername())).thenReturn(Optional.of(user1));

        assertThrows(UsernameAlreadyExistsException.class, ()->{
            customerService.signup(customer2);
        });
    }
}
