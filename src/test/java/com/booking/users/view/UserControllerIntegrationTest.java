package com.booking.users.view;

import com.booking.App;
import com.booking.users.ChangePasswordRequest;
import com.booking.users.User;
import com.booking.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldLoginSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "Password@12"));
        mockMvc.perform(get("/login")
                        .with(httpBasic("test-user", "Password@12")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowErrorMessageForInvalidCredentials() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldBeAbleToUpdateThePasswordSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "Password@12"));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password@12", "New@password12");
        String changePasswordRequestBodyJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(put("/password")
                        .with(httpBasic("test-user", "Password@12"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordRequestBodyJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotBeAbleToUpdateThePasswordWhenValidationFails() throws Exception {
        userRepository.save(new User("test-user", "Password@12"));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password@12", "New");
        String changePasswordRequestBodyJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(put("/password")
                        .with(httpBasic("test-user", "Password@12"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordRequestBodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotBeAbleToUpdateThePasswordWhenProvidedPasswordMisMatchExistingPassword() throws Exception {
        userRepository.save(new User("test-user", "Password@12"));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("WrongPass@12", "NewPass@12");
        String changePasswordRequestBodyJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(put("/password")
                        .with(httpBasic("test-user", "Password@12"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordRequestBodyJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password Mismatch"))
                .andExpect(jsonPath("$.details[0]").value("Entered current password is not matching with existing password"));
    }
}
