package com.bookshop.exchange_service.exchange.web;

import com.bookshop.exchange_service.exchange.domain.ExchangeNotFoundException;
import com.bookshop.exchange_service.exchange.domain.ExchangeAlreadyExistsException;
import com.bookshop.exchange_service.exchange.domain.ExchangeProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExchangeControllerAdvice {

    @ExceptionHandler(ExchangeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleExchangeNotFoundException(ExchangeNotFoundException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ExchangeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ErrorResponse> handleExchangeAlreadyExistsException(ExchangeAlreadyExistsException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ExchangeProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleExchangeProcessingException(ExchangeProcessingException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }
}