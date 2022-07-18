package com.booking.users.repository;

import com.booking.roles.repository.Role;
import com.booking.roles.repository.RoleRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class UserTest {

    private Role role;

    private Validator validator;

    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAUserWithUserRole() {
        User user = new User("seed-user-4","Password@123",new Role(1L,"Admin"));
        userRepository.save(user);

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());

    }
}
