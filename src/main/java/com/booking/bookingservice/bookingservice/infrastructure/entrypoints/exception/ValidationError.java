package com.booking.bookingservice.bookingservice.infrastructure.entrypoints.exception;

public record ValidationError(
        String message,
        String code
) { }
