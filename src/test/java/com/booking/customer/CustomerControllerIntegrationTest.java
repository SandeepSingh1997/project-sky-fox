package com.booking.customer;

import com.booking.App;

import com.booking.movieGateway.exceptions.FormatException;


import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.UserPrincipalService;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser

public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;



    @BeforeEach
    public void beforeEach() throws IOException, FormatException {
        passwordHistoryRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();

    }

    @AfterEach
    public void afterEach() {
        passwordHistoryRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void should_save_customer_details_into_user_and_customer() throws Exception {
        final String requestJson = "{" +
                "\"name\": \"test-user1\"," +
                "\"email\": \"Example@email.com\"," +
                "\"phoneNumber\": \"9898989898\"," +
                "\"username\": \"test-username167\"," +
                "\"password\": \"Password@1\"" +
                "}";


        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isCreated());


        assertThat(userRepository.findAll().size(), is(1));
        assertThat(customerRepository.findAll().size(), is(1));
    }

    @Test
    public void should_not_save_customer_details_when_username_already_exists() throws Exception {
        final String requestJson = "{" +
                "\"name\": \"test-user1\"," +
                "\"email\": \"Example@email.com\"," +
                "\"phoneNumber\": \"9898989898\"," +
                "\"username\": \"test-username1\"," +
                "\"password\": \"Password@1\"" +
                "}";


        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

    }
}





