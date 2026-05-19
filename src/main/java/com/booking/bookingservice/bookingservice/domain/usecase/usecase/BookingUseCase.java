package com.booking.bookingservice.bookingservice.domain.usecase.usecase;

import com.booking.bookingservice.bookingservice.domain.model.Booking;
import com.booking.bookingservice.bookingservice.domain.model.EventRabbit;
import com.booking.bookingservice.bookingservice.domain.usecase.exception.BadRequestException;
import com.booking.bookingservice.bookingservice.domain.usecase.gateway.BookingRepository;
import com.booking.bookingservice.bookingservice.infrastructure.publisher.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

public class BookingUseCase {

    private final BookingRepository bookingRepository;
    private static final Logger log =  LoggerFactory.getLogger(BookingUseCase.class);
    private final ObjectMapper objectMapper;

    public BookingUseCase(BookingRepository bookingRepository, ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper;
    }

    public Mono<Booking> saveBookig(Booking booking){
        return bookingRepository.saveBooking(booking);
    }

    public Mono<Booking> updateBooking(Booking booking) {
        log.info("Ingresa al proceso de actualizar book para el id {}", booking.id());
        return bookingRepository.existById(booking.id())
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new BadRequestException("No existe la reserva")))
                .flatMap(exist -> bookingRepository.updateBooking(booking));
    }

    public Mono<String> deleteBook(String id) {
        log.info("Ingresa al proceso de eliminar book para el id {}", id);
        return bookingRepository.findById(id)
                .filter(booking -> Objects.nonNull(booking.id()))
                .switchIfEmpty(Mono.error(new BadRequestException("No existe la reserva")))
                .flatMap(booking -> bookingRepository.deleteBooking(id)
                            .thenReturn(eventDelete(booking)));

    }

    public String eventDelete(Booking booking){
        return objectMapper.writeValueAsString(new EventRabbit(booking.id(), "deleteBooking", booking.fkFlight(), booking.reservationAmount()));
    }


}
