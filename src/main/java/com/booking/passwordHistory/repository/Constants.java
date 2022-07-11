package com.booking.passwordHistory.repository;

public enum Constants {
    THREE(3);

    private final int value;

    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
