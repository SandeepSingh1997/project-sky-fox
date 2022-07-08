package com.booking.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {

    @JsonProperty
    private String currentPassword;

    @JsonProperty
    @Pattern(regexp = "(^$|[A-Za-z])", message = "Password must contain atleast one Capital Letter")
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
