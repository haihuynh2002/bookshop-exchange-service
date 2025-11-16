package com.bookshop.exchange_service.exchange.web;

public record ExchangeDecision (
        Long exchangeId,
        Boolean approved
) {
}
