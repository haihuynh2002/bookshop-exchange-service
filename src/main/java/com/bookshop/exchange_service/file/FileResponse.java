package com.bookshop.exchange_service.file;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FileResponse {
    Long id;
    Long ownerId;
    String filename;
    String contentType;
    String filePath;
    String originalFilename;
    ImageType type;
    Instant createdDate;
    Instant lastModifiedDate;
}