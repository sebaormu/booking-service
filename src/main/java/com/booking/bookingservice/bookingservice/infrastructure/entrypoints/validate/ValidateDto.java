package com.booking.bookingservice.bookingservice.infrastructure.entrypoints.validate;

import com.booking.bookingservice.bookingservice.domain.usecase.exception.BadRequestException;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ValidateDto {
    private static final String ERROR_STRING = "Error al ingresar el dato %s, max size %s, min size %s ";
    private static final String ERROR_UUID = "El dato %s no tiene el formato correctamente";

    public static Mono<Void> validateString(String data, String field, int minSize, int maxSize){
        return Mono.just(data)
                .filter(d -> d.length() >= minSize && d.length() <= maxSize )
                .switchIfEmpty(Mono.error(new BadRequestException(String.format(ERROR_STRING, field, maxSize, minSize))))
                .then();
    }

    public static Mono<Void> validateUUID(String uuid, String field){
        return Mono.just(uuid)
                .map(UUID::fromString)
                .onErrorResume(e -> Mono.error(new BadRequestException(String.format(ERROR_UUID, field))))
                .then();
    }
}

