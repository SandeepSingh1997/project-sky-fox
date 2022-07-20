package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.bookings.view.BookingConfirmationResponse;
import com.booking.customer.CustomerService;
import com.booking.customer.repository.Customer;
import com.booking.exceptions.CustomerNotFoundException;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.movieAudience.repository.MovieAudienceRepository;
import com.booking.roles.repository.Role;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    public static final Long TEST_SHOW_ID = 1L;
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private Date bookingDate;
    private Show show;
    private MovieAudience movieAudience;
    private MovieAudienceRepository movieAudienceRepository;
    private ShowRepository showRepository;
    private UserRepository userRepository;
    private CustomerService customerService;

    @BeforeEach
    public void beforeEach() {
        bookingRepository = mock(BookingRepository.class);
        movieAudienceRepository = mock(MovieAudienceRepository.class);
        showRepository = mock(ShowRepository.class);
        userRepository = mock(UserRepository.class);
        customerService = mock(CustomerService.class);
        bookingDate = Date.valueOf("2020-06-01");
        Slot slot = new Slot("13:00-16:00", Time.valueOf("13:00:00"), Time.valueOf("16:00:00"));
        show = new Show(bookingDate, slot, BigDecimal.valueOf(250), "1");
        movieAudience = new MovieAudience("Customer name", "9090909090");
        bookingService = new BookingService(bookingRepository, movieAudienceRepository, showRepository, userRepository, customerService);
    }

    @Test
    public void should_save_booking_for_admin() throws NoSeatAvailableException, CustomerNotFoundException {
        User user = new User("test-user1", "Password@12", new Role(1L, "Admin"));
        int noOfSeats = 2;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Booking booking = new Booking(bookingDate, show, movieAudience, noOfSeats, BigDecimal.valueOf(500));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        BookingConfirmationResponse mockBookingConfirmationResponse = new BookingConfirmationResponse(booking.getId(),
                booking.getMovieAudience().getName(),
                booking.getShow().getDate(),
                booking.getShow().getSlot().getStartTime(),
                booking.getAmountPaid(),
                booking.getNoOfSeats(),
                null);
        when(bookingRepository.save(booking)).thenReturn(booking);
        BookingConfirmationResponse actualBookingConfirmationResponse = bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, noOfSeats);

        verify(bookingRepository).save(booking);
        assertEquals(actualBookingConfirmationResponse, mockBookingConfirmationResponse);
    }

    @Test
    public void should_save_customer_who_requests_booking_for_admin() throws NoSeatAvailableException, CustomerNotFoundException {

        User user = new User("test-user1", "Password@12", new Role(1L, "Admin"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        Booking booking = new Booking(bookingDate, show, movieAudience, 2, BigDecimal.valueOf(500));
        when(bookingRepository.save(booking)).thenReturn(booking);
        bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2);

        verify(movieAudienceRepository).save(movieAudience);
    }

    @Test
    public void should_not_book_seat_when_seats_are_not_available_for_admin() {
        User user = new User("test-user1", "Password@12", new Role(1L, "Admin"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(bookingRepository.bookedSeatsByShow(show.getId())).thenReturn(TOTAL_NO_OF_SEATS);
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));

        assertThrows(NoSeatAvailableException.class, () -> bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void should_not_book_seat_when_show_is_not_found_for_admin() {
        User user = new User("test-user1", "Password@12", new Role(1L, "Admin"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.empty());
        final var emptyResultDataAccessException =
                assertThrows(EmptyResultDataAccessException.class,
                        () -> bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2));

        assertThat(emptyResultDataAccessException.getMessage(), is(equalTo("Show not found")));
        assertThat(emptyResultDataAccessException.getExpectedSize(), is(equalTo(1)));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void should_save_booking_for_customer() throws NoSeatAvailableException, CustomerNotFoundException {
        User user = new User("test-user1", "Password@12", new Role(2L, " Customer"));
        Customer customer = new Customer("testCustomer", "abc123@gmail.com", "9977885566", user);
        int noOfSeats = 2;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Booking booking = new Booking(bookingDate, show, movieAudience, noOfSeats, BigDecimal.valueOf(500));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        when(customerService.getCustomerByUserId(user.getId())).thenReturn(customer);
        BookingConfirmationResponse mockBookingConfirmationResponse = new BookingConfirmationResponse(booking.getId(),
                booking.getMovieAudience().getName(),
                booking.getShow().getDate(),
                booking.getShow().getSlot().getStartTime(),
                booking.getAmountPaid(),
                booking.getNoOfSeats(),
                customer.getEmail()
        );
        when(bookingRepository.save(booking)).thenReturn(booking);
        BookingConfirmationResponse actualBookingConfirmationResponse = bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, noOfSeats);

        verify(bookingRepository).save(booking);
        assertEquals(actualBookingConfirmationResponse, mockBookingConfirmationResponse);
    }

    @Test
    public void should_save_customer_who_requests_booking_for_customer() throws NoSeatAvailableException, CustomerNotFoundException {

        User user = new User("test-user1", "Password@12", new Role(2L, "Customer"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        Customer customer = new Customer("testCustomer", "abc123@gmail.com", "9977885566", user);
        when(customerService.getCustomerByUserId(user.getId())).thenReturn(customer);
        Booking booking = new Booking(bookingDate, show, movieAudience, 2, BigDecimal.valueOf(500));
        when(bookingRepository.save(booking)).thenReturn(booking);
        bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2);

        verify(movieAudienceRepository).save(movieAudience);
    }

    @Test
    public void should_not_book_seat_when_seats_are_not_available_for_customer() {
        User user = new User("test-user1", "Password@12", new Role(2L, "Customer"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(bookingRepository.bookedSeatsByShow(show.getId())).thenReturn(TOTAL_NO_OF_SEATS);
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));

        assertThrows(NoSeatAvailableException.class, () -> bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void should_not_book_seat_when_show_is_not_found_for_customer() {
        User user = new User("test-user1", "Password@12", new Role(2L, "Customer"));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.empty());
        final var emptyResultDataAccessException =
                assertThrows(EmptyResultDataAccessException.class,
                        () -> bookingService.book(user.getUsername(), movieAudience, TEST_SHOW_ID, bookingDate, 2));

        assertThat(emptyResultDataAccessException.getMessage(), is(equalTo("Show not found")));
        assertThat(emptyResultDataAccessException.getExpectedSize(), is(equalTo(1)));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
