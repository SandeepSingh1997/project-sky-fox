package com.booking.users.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangePasswordRequestTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldAllowToChangePasswordWhenPasswordIsValid() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password123", "New@password123");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.isEmpty(), is(true));
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithoutCapitalLetter() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password", "new@password");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithoutSpecialCharacter() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password", "Newpassword");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithoutDigit() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password123", "New@password");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithLessThanMinimumCharacters() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password123", "pass@123");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    void shouldNotAllowToSetNewPasswordWithMoreThanMaximumCharacters() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Old@password123", "Passwordpasswo@12");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    void shouldPassAllValidationsForCurrentPasswordOfChangePasswordRequestBody() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("passwordpasswordd", "Password@123");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        assertThat(violations.iterator().next().getMessage(), is("Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters"));
    }

    @Test
    public void shouldNotAllowNullFields() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(null, null);

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        List<String> messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        assertTrue(messages.containsAll(Arrays
                .asList("Current password must be provided",
                        "New password must be provided")));
    }

    @Test
    public void shouldNotAllowBlankFields() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("", "");

        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);

        List<String> messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        assertTrue(messages.containsAll(Arrays
                .asList("Current password must not be empty value",
                        "New password must not be empty value")));
    }
}
