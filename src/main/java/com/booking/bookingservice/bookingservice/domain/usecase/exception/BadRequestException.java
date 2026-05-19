package com.booking.bookingservice.bookingservice.domain.usecase.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}

