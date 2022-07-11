package com.booking.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {

    @JsonProperty
    @NotNull(message = "Current password must be provided")
    @NotBlank(message = "Current password must not be empty value")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$", message = "Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters")
    private String currentPassword;

    @JsonProperty
    @NotNull(message = "New password must be provided")
    @NotBlank(message = "New password must not be empty value")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$", message = "Password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters")
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
