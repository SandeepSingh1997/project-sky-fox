package com.booking.users.view;

import com.booking.users.ChangePasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ChangePasswordRequestTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithoutCapitalLetter() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password", "new@password");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter"));
    }
}
