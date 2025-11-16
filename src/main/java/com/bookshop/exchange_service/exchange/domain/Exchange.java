package com.bookshop.exchange_service.exchange.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Exchange {
    @Id
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be a positive number")
    Long id;

    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID must be a positive number")
    Long orderId;

    String email;
    String phone;
    String firstName;
    String lastName;

    @NotBlank(message = "Condition cannot be blank")
    String condition;

    @NotBlank(message = "Reason cannot be blank")
    String reason;

    @NotNull(message = "Payment status cannot be null")
    ExchangeStatus status;

    @CreatedDate
    Instant createdDate;

    @LastModifiedDate
    Instant lastModifiedDate;

    @CreatedBy
    String createdBy;

    @LastModifiedBy
    String lastModifiedBy;

    @Version
    Long version;
}
