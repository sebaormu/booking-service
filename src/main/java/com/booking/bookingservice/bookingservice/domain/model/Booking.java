package com.booking.bookingservice.bookingservice.domain.model;

public record Booking(
        String id,
        String fkFlight,
        String userName,
        int reservationAmount,
        String status
) {
}
