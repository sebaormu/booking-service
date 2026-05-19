package com.booking.bookingservice.bookingservice.infrastructure.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <T> Mono<Void> send(T object, String exchange, String routingKey)  {
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                object));
    }
}
