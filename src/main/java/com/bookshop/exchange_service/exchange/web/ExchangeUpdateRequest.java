package com.bookshop.exchange_service.exchange.web;

import com.bookshop.exchange_service.exchange.domain.ExchangeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExchangeUpdateRequest {
        String condition;
        String reason;
        ExchangeStatus status;
}
