package com.booking.roles.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleTest {

    Validator validator;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldReturnNoViolation() {

        Role admin = new Role("Admin");

        final Set<ConstraintViolation<Role>> violations = validator.validate(admin);

        assertTrue(violations.isEmpty());


    }

    @Test
    void shouldReturnViolationIfRoleIsNotDefined() {
        Role role = new Role(null);
        final Set<ConstraintViolation<Role>> violations = validator.validate(role);

        assertFalse(violations.isEmpty());
    }
}