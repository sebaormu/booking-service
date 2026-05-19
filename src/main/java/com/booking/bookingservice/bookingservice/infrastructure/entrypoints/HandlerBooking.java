package com.booking.bookingservice.bookingservice.infrastructure.entrypoints;

import com.booking.bookingservice.bookingservice.domain.usecase.usecase.BookingUseCase;
import com.booking.bookingservice.bookingservice.infrastructure.entrypoints.dto.BookingDto;
import com.booking.bookingservice.bookingservice.infrastructure.publisher.EventPublisher;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class HandlerBooking {

    private final BookingUseCase bookingUseCase;
    private static final Logger log =  LoggerFactory.getLogger(HandlerBooking.class);
    private final EventPublisher eventPublisher;


    public HandlerBooking(BookingUseCase bookingUseCase, EventPublisher eventPublisher) {
        this.bookingUseCase = bookingUseCase;
        this.eventPublisher = eventPublisher;
    }

    public Mono<ServerResponse> createBooking(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookingDto.class)
                .flatMap(bookingDto -> {
                    log.info("Comienza el proceso de creacion de reserva para el id");
                    return bookingDto
                            .validateSave()
                            .thenReturn(bookingDto);
                })
                .flatMap(bookingDto -> bookingDto.toDomain(bookingDto))
                .flatMap(bookingUseCase::saveBookig)
                .flatMap(BookingDto::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    public Mono<ServerResponse> updateBooking(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookingDto.class)
                .flatMap(bookingDto -> {
                    log.info("Comienza el proceso de actualizacion de reserva para el id {}", bookingDto.id());
                    return bookingDto.validateUpdate().thenReturn(bookingDto);
                })
                .flatMap(bookingDto -> bookingDto.toDomain(bookingDto))
                .flatMap(bookingUseCase::updateBooking)
                .then( ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteBooking(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        log.info("Comienza el proceso de eliminacion de reserva para el id {}", id);
        return bookingUseCase.deleteBook(id)
                .flatMap(booking -> eventPublisher.send(booking, "booking.exchange", "booking.rk"))
                .then(ServerResponse.ok().build());
    }
}
