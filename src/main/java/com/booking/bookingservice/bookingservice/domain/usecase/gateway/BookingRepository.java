package com.booking.bookingservice.bookingservice.domain.usecase.gateway;

import com.booking.bookingservice.bookingservice.domain.model.Booking;
import reactor.core.publisher.Mono;

public interface BookingRepository {
    Mono<Booking> saveBooking(Booking booking);
    Mono<Booking> updateBooking(Booking booking);
    Mono<Boolean> existById(String id);
    Mono<Void> deleteBooking(String id);
    Mono<Booking> findById(String id);
}
