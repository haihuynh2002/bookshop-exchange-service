package com.bookshop.exchange_service.exchange.domain;

public class ExchangeNotFoundException extends RuntimeException {
    public ExchangeNotFoundException(Long id) {
        super("Exchange not found with id: " + id);
    }
}
