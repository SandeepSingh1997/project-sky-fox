package com.booking.bookings.repository;

import com.booking.bookings.view.BookingConfirmationResponse;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.shows.respository.Show;
import com.booking.slots.repository.Slot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingTest {

    private Date date;
    private Show show;
    private MovieAudience movieAudience;
    private Validator validator;
    private Slot slot;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        slot = new Slot("Slot name", Time.valueOf("13:00:00"), Time.valueOf("15:00:00"));
        date = Date.valueOf("2020-06-01");
        show = new Show(date, slot, BigDecimal.valueOf(245.99), "movie-1");
        movieAudience = new MovieAudience("customer", "9081238761");
    }

    @Test
    public void should_return_no_violation() {
        final Booking booking = new Booking(date, show, movieAudience, 2, BigDecimal.valueOf(244.99));

        final Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void should_not_allow_null_fields() {
        Booking booking = new Booking(null, null, null, null, null);

        final Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        final List<String> messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        assertTrue(messages.containsAll(Arrays
                .asList("No of seats must be provided",
                        "Customer must be provided",
                        "Amount paid must be provided",
                        "Date must be provided",
                        "Show must be provided")));
    }

    @Test
    public void should_not_allow_no_of_seats_less_than_0() {
        final Booking booking = new Booking(date, show, movieAudience, -1, BigDecimal.valueOf(299));

        final Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertThat(violations.iterator().next().getMessage(), is("Seats should be greater than 0"));
    }

    @Test
    public void should_not_allow_amount_paid_less_than_0() {
        final Booking booking = new Booking(date, show, movieAudience, 1, BigDecimal.valueOf(-1));

        final Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertThat(violations.iterator().next().getMessage(), is("Amount paid should be greater than 0"));
    }
}
