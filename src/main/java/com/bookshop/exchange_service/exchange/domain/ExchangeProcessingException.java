package com.bookshop.exchange_service.exchange.domain;

public class ExchangeProcessingException extends RuntimeException {
    public ExchangeProcessingException(String message) {
        super("Exchange processing failed: " + message);
    }

    public ExchangeProcessingException(String message, Throwable cause) {
        super("Exchange processing failed: " + message, cause);
    }
}
