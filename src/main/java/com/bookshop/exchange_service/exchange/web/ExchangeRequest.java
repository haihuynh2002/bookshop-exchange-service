package com.bookshop.exchange_service.exchange.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExchangeRequest {
        @NotNull(message = "The order ID must be defined.")
        Long orderId;

        @NotBlank
        String condition;

        @NotBlank(message = "The reason must be defined.")
        String reason;
}
