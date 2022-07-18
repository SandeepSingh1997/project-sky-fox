package com.booking.users.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {

    @JsonProperty
    @NotBlank(message = "Current password must be provided")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-@#~$%^&+=/`(){|}\\[\\],.<>?!*;:_'\"/\\\\])(?=\\S+$).{8,16}$", message = "Current password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters")
    private String currentPassword;

    @JsonProperty
    @NotBlank(message = "New password must be provided")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-@#~$%^&+=/`(){|}\\[\\],.<>?!*;:_'\"/\\\\])(?=\\S+$).{8,16}$", message = "New password must contain atleast one Capital Letter, one Special character, one Digit, Minimum of 8 and Maximum of 16 characters")
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
