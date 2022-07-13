package com.booking.customer;

import com.booking.movieAudience.repository.MovieAudience;
import com.booking.shows.respository.Show;
import com.booking.slots.repository.Slot;
import com.booking.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomerTest {
    private Validator validator;

    private User user;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        user = new User("seed-user-1","foobar");
    }

    @Test
    public void should_not_allow_customer_name_to_be_blank() {
        final Customer customer = new Customer("","abc_123@gmail.com" ,"9977885566", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Customer name must be provided"));
    }

    @Test
    public void should_not_allow_invalid_email() {
        final Customer customer = new Customer("testCustomer","abc_123gmail.com" ,"9977885566", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Enter valid email"));
    }

}
