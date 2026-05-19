package com.booking.bookingservice.bookingservice.domain.model;

public record EventRabbit(
        String id,
        String eventType,
        String fkFlight,
        int reservationAmount
) {
}
