package com.bookshop.exchange_service.exchange.domain;

public class ExchangeAlreadyExistsException extends RuntimeException {
    public ExchangeAlreadyExistsException(Long orderId) {
        super("Exchange already exists for order id: " + orderId);
    }
}
