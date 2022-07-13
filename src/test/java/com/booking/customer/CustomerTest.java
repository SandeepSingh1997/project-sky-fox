package com.booking.customer;


import com.booking.users.repository.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final Customer customer = new Customer("","Axyz123@gmail.com" ,"9977885566", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Customer name must be provided"));
    }

    @Test
    public void should_not_allow_invalid_email() {
        final Customer customer = new Customer("testCustomer","abc_123gmail.com" ,"9977885566", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Enter valid email"));
    }

    @Test
    public void should_allow_phone_number_only_10_digits() {
        final Customer customer = new Customer("testCustomer","abc123@gmail.com" ,"99778855667", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must have exactly 10 digits"));
    }

    @Test
    public void should_not_allow_blank_phone_number() {
        final Customer customer = new Customer("testCustomer","abc123@gmail.com" ,"", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must be provided"));
    }

    @Test
    public void should_return_no_violation() {
        final Customer customer = new Customer("testCustomer","abc123@gmail.com" ,"1234567890", user);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void should_not_allow_null_user() {
        final Customer customer = new Customer("testCustomer","abc123@gmail.com" ,"1234567890", null);

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("User must be provided"));
    }

}
