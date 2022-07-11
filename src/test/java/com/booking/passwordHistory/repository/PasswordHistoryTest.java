package com.booking.passwordHistory.repository;

import com.booking.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordHistoryTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldNotReturnAnyViolations() {
        User user = new User("test-user", "Password@123");
        Timestamp instant = Timestamp.from(Instant.now());
        PasswordHistory passwordHistory = new PasswordHistory(new PasswordHistoryPK(user, "Password@123"), instant);

        Set<ConstraintViolation<PasswordHistory>> validate = validator.validate(passwordHistory);

        assertTrue(validate.isEmpty());
    }

    @Test
    void shouldNotAllowNullFields() {
        PasswordHistory passwordHistory = new PasswordHistory(null, null);

        Set<ConstraintViolation<PasswordHistory>> validate = validator.validate(passwordHistory);
        List<String> messages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        assertTrue(messages.containsAll(Arrays
                .asList("User id and password must be provided",
                        "Created at must be provided")));
    }

    @Test
    void shouldNotAllowNullFieldsAsPrimaryKey() {
        PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(null, null);

        Set<ConstraintViolation<PasswordHistoryPK>> validate = validator.validate(passwordHistoryPK);
        List<String> messages = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        assertTrue(messages.containsAll(Arrays
                .asList("User must be provided",
                        "Password must be provided")));
    }

    @Test
    void shouldNotAllowInvalidPassword() {
        User user = new User("test-user", "Password@123");
        PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(user, "new");

        Set<ConstraintViolation<PasswordHistoryPK>> validate = validator.validate(passwordHistoryPK);

        assertEquals(validate.iterator().next().getMessage(), "Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters");
    }
}
