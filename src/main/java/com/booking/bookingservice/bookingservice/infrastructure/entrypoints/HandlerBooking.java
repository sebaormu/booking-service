package com.booking.bookingservice.bookingservice.infrastructure.entrypoints;

import com.booking.bookingservice.bookingservice.domain.model.Booking;
import com.booking.bookingservice.bookingservice.domain.model.EventRabbit;
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
import tools.jackson.databind.ObjectMapper;


@Component
public class HandlerBooking {

    private final BookingUseCase bookingUseCase;
    private static final Logger log =  LoggerFactory.getLogger(HandlerBooking.class);
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;



    public HandlerBooking(BookingUseCase bookingUseCase, EventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.bookingUseCase = bookingUseCase;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
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
                .flatMap(dto -> {
                    String eventString = eventSave(dto, "saveEvent");
                    return eventPublisher.send(eventString, "booking.exchange", "booking.rk")
                            .then(ServerResponse.ok().bodyValue(dto));
                });

    }

    public Mono<ServerResponse> updateBooking(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookingDto.class)
                .flatMap(bookingDto -> {
                    log.info("Comienza el proceso de actualizacion de reserva para el id {}", bookingDto.id());
                    return bookingDto.validateUpdate().thenReturn(bookingDto);
                })
                .flatMap(bookingDto -> bookingDto.toDomain(bookingDto))
                .flatMap(bookingUseCase::updateBooking)
                .flatMap(BookingDto::toDto)
                .flatMap(dto -> {
                    String eventString = eventSave(dto, "updateEvent");
                    return eventPublisher.send(eventString, "booking.exchange", "booking.rk")
                            .then(ServerResponse.ok().bodyValue(dto));
                });
    }

    public Mono<ServerResponse> deleteBooking(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        log.info("Comienza el proceso de eliminacion de reserva para el id {}", id);
        return bookingUseCase.deleteBook(id)
                .flatMap(booking -> eventPublisher.send(booking, "booking.exchange", "booking.rk"))
                .then(ServerResponse.ok().build());
    }

    public String eventSave(BookingDto booking, String type){
        return objectMapper.writeValueAsString(new EventRabbit(booking.id(), type, booking.fkFlight(), booking.reservationAmount()));
    }
}
