package com.bookshop.exchange_service.exchange.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRepository extends ReactiveCrudRepository<Exchange, Long> {
    Mono<Exchange> findByOrderId(Long orderId);
    Flux<Exchange> findByCreatedBy(String createdBy);
}
