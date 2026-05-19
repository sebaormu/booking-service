package com.booking.bookingservice.bookingservice.domain.model;

public record EventDelete(
        String id,
        String eventType,
        String fkFlight,
        int reservationAmount
) {
}
