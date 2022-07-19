package com.booking.exceptions;

public class UserIdDoesNotMatchesWithRequestedUserId extends Exception {
    public UserIdDoesNotMatchesWithRequestedUserId(String message) {
        super(message);
    }
}
