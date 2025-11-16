package com.bookshop.exchange_service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FileClient {

    private static final String FILES_ROOT_API = "/files";
    private final WebClient webClient;

    public FileClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<FileResponse> uploadFiles(Flux<FilePart> files, Long ownerId, ImageType type) {
        return buildMultipartBody(files)
                .flatMapMany(bodyInserter -> webClient.post()
                        .uri(builder -> builder
                                .path(FILES_ROOT_API)
                                .queryParam("ownerId", ownerId.toString())
                                .queryParam("type", type.toString())
                                .build()
                        )
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(bodyInserter)
                        .retrieve()
                        .bodyToFlux(FileResponse.class)
                );
    }

    public Flux<FileResponse> updateFiles(Flux<FilePart> files, Long ownerId, ImageType type) {
        return buildMultipartBody(files)
                .flatMapMany(bodyInserter -> webClient.put()
                        .uri(builder -> builder
                                .path(FILES_ROOT_API)
                                .queryParam("ownerId", ownerId.toString())
                                .queryParam("type", type.toString())
                                .build()
                        )
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(bodyInserter)
                        .retrieve()
                        .bodyToFlux(FileResponse.class)
                );
    }

    public Flux<FileResponse> findFilesByOwnerId(Long ownerId, ImageType type) {
        return webClient.get()
                .uri(FILES_ROOT_API + "/{type}/{ownerId}", type, ownerId)
                .retrieve()
                .bodyToFlux(FileResponse.class);
    }

    public Mono<Void> deleteFiles(Long ownerId, ImageType type) {
        return webClient.delete()
                .uri(FILES_ROOT_API  + "/{type}/{ownerId}", type, ownerId)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private Mono<BodyInserters.MultipartInserter> buildMultipartBody(Flux<FilePart> files) {
        return files
                .collectList()
                .map(fileParts -> {
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    for (int i = 0; i < fileParts.size(); i++) {
                        builder.part("files", fileParts.get(i));
                    }
                    return BodyInserters.fromMultipartData(builder.build());
                });
    }
}