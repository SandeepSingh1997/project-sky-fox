package com.booking.users.view;

import com.booking.App;
import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
        passwordHistoryRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        passwordHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    void shouldNotBeAbleToUpdateThePasswordWhenProvidedNewPasswordMatchesWithLastThreePasswords() throws Exception {
        User user = new User("test-user", "Password@12");
        userRepository.save(user);
        List<PasswordHistory> passwords = new ArrayList<>();
        Timestamp instant = Timestamp.from(Instant.now());
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@1"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@2"), instant));
        instant = Timestamp.valueOf(instant.toLocalDateTime().plusDays(1));
        passwords.add(new PasswordHistory(new PasswordHistoryPK(user, "Password@3"), instant));
        passwordHistoryRepository.saveAll(passwords);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password@12", "Password@2");
        String changePasswordRequestBodyJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(put("/password")
                        .with(httpBasic("test-user", "Password@12"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordRequestBodyJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password matches with last three passwords"))
                .andExpect(jsonPath("$.details[0]").value("Entered new password matches with recent three passwords"));
    }
}
