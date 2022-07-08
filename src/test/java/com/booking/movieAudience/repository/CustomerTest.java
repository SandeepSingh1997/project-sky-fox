package com.booking.movieAudience.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomerTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void should_not_allow_customer_name_to_be_blank() {
        final MovieAudience customer = new MovieAudience("", "9099234568");

        final Set<ConstraintViolation<MovieAudience>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Customer name must be provided"));
    }

    @Test
    public void should_allow_phone_number_only_10_digits() {
        final MovieAudience customer = new MovieAudience("Customer 1", "999332");

        final Set<ConstraintViolation<MovieAudience>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must have exactly 10 digits"));
    }

    @Test
    public void should_not_allow_blank_phone_number() {
        final MovieAudience customer = new MovieAudience("Customer 1", "");

        final Set<ConstraintViolation<MovieAudience>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must be provided"));
    }
}
