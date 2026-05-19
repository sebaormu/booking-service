package com.booking.bookingservice.bookingservice.applications.config;

import com.booking.bookingservice.bookingservice.domain.usecase.gateway.BookingRepository;
import com.booking.bookingservice.bookingservice.infrastructure.entrypoints.exception.GlobalErrorHandler;
import com.booking.bookingservice.bookingservice.infrastructure.publisher.EventPublisher;
import com.booking.bookingservice.bookingservice.domain.usecase.usecase.BookingUseCase;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class UseCaseConfig {

    @Bean
    public BookingUseCase bookingUseCase(BookingRepository repository, ObjectMapper objectMapper){
        return new BookingUseCase(repository, objectMapper);
    }

    @Bean
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate){
        return new EventPublisher(rabbitTemplate);
    }

    @Bean
    public GlobalErrorHandler globalErrorHandler(ObjectMapper objectMapper){
        return new GlobalErrorHandler(objectMapper);
    }
}
