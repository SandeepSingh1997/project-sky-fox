package com.booking.bookings.view;

import com.booking.movieAudience.repository.MovieAudience;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.Date;
import java.util.Set;

import static com.booking.shows.respository.Constants.MAX_NO_OF_SEATS_PER_BOOKING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookingRequestTest {
    @Test
    public void should_not_allow_seat_booking_for_more_than_maximum() {
        final var showId = 1L;
        final MovieAudience customer = new MovieAudience("customer 1", "992212399");
        int greaterThanMaxSeats = Integer.parseInt(MAX_NO_OF_SEATS_PER_BOOKING) + 1;
        final BookingRequest bookingRequest = new BookingRequest(Date.valueOf("2020-11-06"), showId, customer, greaterThanMaxSeats);

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingRequest);

        assertThat(violations.iterator().next().getMessage(), is("Maximum " + MAX_NO_OF_SEATS_PER_BOOKING + " seats allowed per booking"));
    }
}
