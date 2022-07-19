package com.booking.customer;

import com.booking.App;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Test
    public void shouldBeAbleToGetCustomerDetailsByUsername() throws Exception {
        User user = new User("test-user", "password", new Role(2L, "Customer"));
        userRepository.save(user);
        Customer customer = new Customer("test-customer", "example@email.com", "1234567890", user);
        customerRepository.save(customer);

        mockMvc.perform(get("/customers/{username}", "test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.username").value(customer.getUser().getUsername()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.mobile").value(customer.getPhoneNumber()));
    }

    @Test
    void shouldBeAbleToReturnBadRequestWhenUsernameIsNotFound() throws Exception {
        mockMvc.perform(get("/customers/{username}", "test-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username not found"))
                .andExpect(jsonPath("$.details[0]").value("User not found"));
    }

    @Test
    void shouldBeAbleToReturnBadRequestWhenCustomerIsNotFoundWithUsername() throws Exception {
        User testUserOne = new User("test-user-1", "password", new Role(2L, "Customer"));
        User testUserTwo = new User("test-user-2", "password", new Role(2L, "Customer"));
        userRepository.save(testUserOne);
        userRepository.save(testUserTwo);
        Customer customer = new Customer("test-customer", "example@email.com", "1234567890", testUserTwo);
        customerRepository.save(customer);

        mockMvc.perform(get("/customers/{username}", "test-user-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer not found with username"))
                .andExpect(jsonPath("$.details[0]").value("Customer not found"));
    }
}





