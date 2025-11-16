package com.bookshop.exchange_service.exchange.domain;

import com.bookshop.exchange_service.exchange.event.ExchangeEvent;
import com.bookshop.exchange_service.exchange.web.ExchangeRequest;
import com.bookshop.exchange_service.exchange.web.ExchangeResponse;
import com.bookshop.exchange_service.exchange.web.ExchangeUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExchangeMapper {
    Exchange toExchange(ExchangeRequest request);

    ExchangeResponse toExchangeResponse(Exchange exchange);

    ExchangeEvent toExchangeEvent(Exchange exchange);

    void update(@MappingTarget Exchange exchange, ExchangeUpdateRequest request);
}
