package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.movieAudience.repository.MovieAudienceRepository;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
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

    @BeforeEach
    public void beforeEach() {
        bookingRepository = mock(BookingRepository.class);
        movieAudienceRepository = mock(MovieAudienceRepository.class);
        showRepository = mock(ShowRepository.class);
        bookingDate = Date.valueOf("2020-06-01");
        Slot slot = new Slot("13:00-16:00", Time.valueOf("13:00:00"), Time.valueOf("16:00:00"));
        show = new Show(bookingDate, slot, BigDecimal.valueOf(250), "1");
        movieAudience = new MovieAudience("Customer name", "9090909090");
        bookingService = new BookingService(bookingRepository, movieAudienceRepository, showRepository);
    }

    @Test
    public void should_save_booking() throws NoSeatAvailableException {
        int noOfSeats = 2;
        Booking booking = new Booking(bookingDate, show, movieAudience, noOfSeats, BigDecimal.valueOf(500));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        Booking mockBooking = mock(Booking.class);
        when(bookingRepository.save(booking)).thenReturn(mockBooking);

        Booking actualBooking = bookingService.book(movieAudience, TEST_SHOW_ID, bookingDate, noOfSeats);

        verify(bookingRepository).save(booking);
        assertThat(actualBooking, is(equalTo(mockBooking)));
    }

    @Test
    public void should_save_customer_who_requests_booking() throws NoSeatAvailableException {
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        bookingService.book(movieAudience, TEST_SHOW_ID, bookingDate, 2);

        verify(movieAudienceRepository).save(movieAudience);
    }

    @Test
    public void should_not_book_seat_when_seats_are_not_available() {
        when(bookingRepository.bookedSeatsByShow(show.getId())).thenReturn(TOTAL_NO_OF_SEATS);
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        assertThrows(NoSeatAvailableException.class, () -> bookingService.book(movieAudience, TEST_SHOW_ID, bookingDate, 2));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void should_not_book_seat_when_show_is_not_found() {
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.empty());
        final var emptyResultDataAccessException =
                assertThrows(EmptyResultDataAccessException.class,
                        () -> bookingService.book(movieAudience, TEST_SHOW_ID, bookingDate, 2));

        assertThat(emptyResultDataAccessException.getMessage(), is(equalTo("Show not found")));
        assertThat(emptyResultDataAccessException.getExpectedSize(), is(equalTo(1)));
        verifyNoInteractions(movieAudienceRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
