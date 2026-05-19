package com.booking.bookingservice.bookingservice.infrastructure.drivradapters;

import com.booking.bookingservice.bookingservice.domain.model.Booking;
import com.booking.bookingservice.bookingservice.domain.usecase.gateway.BookingRepository;
import com.booking.bookingservice.bookingservice.infrastructure.drivradapters.booking.BookingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Repository
public class RepositoryAdapter implements BookingRepository {

    private final ReactiveRepositoryData reactiveRepositoryData;
    private static final Logger log =  LoggerFactory.getLogger(RepositoryAdapter.class);


    public RepositoryAdapter(ReactiveRepositoryData reactiveRepositoryData) {
        this.reactiveRepositoryData = reactiveRepositoryData;
    }


    @Override
    public Mono<Booking> saveBooking(Booking booking) {
        return toEntity(booking)
                .flatMap(reactiveRepositoryData::save)
                .flatMap(this::toDomain);
    }

    @Override
    public Mono<Booking> updateBooking(Booking booking) {
        log.info("Ingresa al proceso de actualizar book en el repository para el id {}", booking.id());
        return toEntity(booking)
                .flatMap(reactiveRepositoryData::save)
                .flatMap(this::toDomain);
    }

    @Override
    public Mono<Boolean> existById(String id) {
        return reactiveRepositoryData.existsById(UUID.fromString(id));
    }

    public Mono<Booking> findById(String id) {
        return reactiveRepositoryData.findById(UUID.fromString(id))
                .flatMap(this::toDomain);
    }

    @Override
    public Mono<Void> deleteBooking(String id) {
        log.info("Ingresa al proceso de eliminacion en el rpository para el id {}", id);
        return reactiveRepositoryData.updateStatus(UUID.fromString(id));
    }

    public Mono<BookingEntity> toEntity(Booking booking){
        return Mono.just(booking)
                        .map(b -> {
                            if (Objects.isNull(b.id())){
                                return new BookingEntity(null, UUID.fromString(b.fkFlight()), b.userName(), b.status(), b.reservationAmount());
                            }
                            return new BookingEntity(UUID.fromString(b.id()), UUID.fromString(b.fkFlight()), b.userName(), b.status(), b.reservationAmount());});
    }

    public Mono<Booking> toDomain(BookingEntity booking){
        return Mono.just(booking)
                .map(b -> new Booking(b.getId().toString(), b.getFkFlight().toString(), b.getUserName(),b.getReservationAmount(), b.getStatus()));
    }

}
