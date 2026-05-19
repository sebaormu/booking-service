package com.booking.bookingservice.bookingservice.infrastructure.drivradapters;

import com.booking.bookingservice.bookingservice.infrastructure.drivradapters.booking.BookingEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveRepositoryData extends ReactiveCrudRepository<BookingEntity, UUID> {

    @Query(
            value = "UPDATE booking SET status = 'CANCELADO' WHERE ID = :id")
    Mono<Void> updateStatus(@Param("id") UUID id);
}
