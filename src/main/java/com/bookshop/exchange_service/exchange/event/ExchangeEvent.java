package com.bookshop.exchange_service.exchange.event;

import com.bookshop.exchange_service.exchange.domain.ExchangeStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExchangeEvent {
    Long id;
    Long orderId;
    String condition;
    String reason;
    ExchangeStatus status;

    String email;
    String phone;
    String firstName;
    String lastName;

    Instant createdDate;
    Instant lastModifiedDate;
    String createdBy;
    String lastModifiedBy;
}
