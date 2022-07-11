package com.booking.exceptions;

public class PasswordMatchesWithLastThreePasswordsException extends Exception {
    public PasswordMatchesWithLastThreePasswordsException(String message) {
        super(message);
    }
}
