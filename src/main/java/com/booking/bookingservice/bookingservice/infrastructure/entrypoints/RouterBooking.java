package com.booking.bookingservice.bookingservice.infrastructure.entrypoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterBooking {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerBooking handler) {
        return route(POST("/crear-reserva"), handler::createBooking)
                .andRoute(PUT("actualizar-reserva"), handler::updateBooking)
                .andRoute(DELETE("eliminar-reserva/{id}"),handler::deleteBooking);
    }
}
