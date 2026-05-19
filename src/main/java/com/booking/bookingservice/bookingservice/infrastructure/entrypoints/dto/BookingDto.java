package com.booking.bookingservice.bookingservice.infrastructure.entrypoints.dto;

import com.booking.bookingservice.bookingservice.domain.model.Booking;
import com.booking.bookingservice.bookingservice.infrastructure.entrypoints.validate.ValidateDto;
import reactor.core.publisher.Mono;

public record BookingDto(
        String id,
        String fkFlight,
        String userName,
        int reservationAmount,
        String status
) {

    public Mono<Void> validateSave() {
        return ValidateDto.validateString(userName, "usernam", 5, 20)
                .switchIfEmpty(ValidateDto.validateUUID(fkFlight, "fkFlight"))
                .then();
    }

    public Mono<Void> validateUpdate(){
        return validateSave()
                .switchIfEmpty(ValidateDto.validateUUID(id, "id"));
    }

    public Mono<Booking> toDomain(BookingDto bookingDto) {
        return Mono.just(bookingDto)
                .map(bDto -> new Booking(bDto.id(), bDto.fkFlight(), bDto.userName(),bDto.reservationAmount(), bDto.status()) );
    }

    public static Mono<BookingDto> toDto(Booking booking){
        return Mono.just(booking)
                .map(bDto -> new BookingDto(bDto.id(), bDto.fkFlight(), bDto.userName(),bDto.reservationAmount(), bDto.status()) );
    }
}
