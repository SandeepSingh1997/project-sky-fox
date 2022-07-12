package com.booking.passwordHistory.repository;

import com.booking.users.repository.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Embeddable
public class PasswordHistoryPK implements Serializable {

    @JsonProperty
    @NotNull(message = "User must be provided")
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JsonProperty
    @NotNull(message = "Password must be provided")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$", message = "Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters")
    private String password;

    public PasswordHistoryPK() {
    }

    public PasswordHistoryPK(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
