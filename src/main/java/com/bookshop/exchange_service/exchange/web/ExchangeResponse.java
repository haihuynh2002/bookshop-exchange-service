package com.bookshop.exchange_service.exchange.web;

import com.bookshop.exchange_service.exchange.domain.ExchangeStatus;
import com.bookshop.exchange_service.file.FileResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExchangeResponse {
    Long id;
    Long orderId;
    String condition;
    String reason;
    ExchangeStatus status;
    FileResponse image;

    String email;
    String phone;
    String firstName;
    String lastName;

    Instant createdDate;
    Instant lastModifiedDate;
    String createdBy;
    String lastModifiedBy;
    Long version;
}
