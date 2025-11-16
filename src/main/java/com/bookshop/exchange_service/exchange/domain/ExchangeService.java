package com.bookshop.exchange_service.exchange.domain;

import com.bookshop.exchange_service.exchange.web.ExchangeRequest;
import com.bookshop.exchange_service.exchange.web.ExchangeResponse;
import com.bookshop.exchange_service.exchange.web.ExchangeUpdateRequest;
import com.bookshop.exchange_service.file.FileClient;
import com.bookshop.exchange_service.file.FileResponse;
import com.bookshop.exchange_service.file.ImageType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExchangeService {

    ExchangeRepository exchangeRepository;
    ExchangeMapper exchangeMapper;
    FileClient fileClient;
    StreamBridge streamBridge;

    public Flux<ExchangeResponse> getAllExchanges() {
        return exchangeRepository.findAll()
                .flatMap(this::enrichExchangeWithImage);
    }

    public Flux<ExchangeResponse> getMyExchanges(Jwt jwt) {
        return exchangeRepository.findByCreatedBy(jwt.getSubject())
                .flatMap(this::enrichExchangeWithImage);
    }

    public Mono<ExchangeResponse> getExchange(Long id) {
        return exchangeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ExchangeNotFoundException(id)))
                .flatMap(this::enrichExchangeWithImage);
    }

    @Transactional
    public Mono<Exchange> createExchange(ExchangeRequest request, Flux<FilePart> imageFile, Jwt jwt) {
        return exchangeRepository.findByOrderId(request.getOrderId())
                .switchIfEmpty(Mono.defer(() -> createNewExchange(request, jwt)))
                .flatMap(existingExchange -> updateExistingExchange(existingExchange, jwt))
                .flatMap(exchangeRepository::save)
                .flatMap(exchange -> uploadExchangeImage(imageFile, exchange.getId())
                        .thenReturn(exchange));
    }

    @Transactional
    public Mono<Exchange> updateExchange(Long id, ExchangeUpdateRequest request, Flux<FilePart> imageFile) {
        return exchangeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ExchangeNotFoundException(id)))
                .map(exchange -> {
                    exchangeMapper.update(exchange, request);
                    return exchange;
                })
                .flatMap(exchangeRepository::save)
                .flatMap(savedExchange -> {
                    return imageFile.hasElements()
                            .flatMap(hasFiles -> {
                                if (hasFiles) {
                                    return updateExchangeImage(imageFile, savedExchange.getId())
                                            .thenReturn(savedExchange);
                                } else {
                                    log.info("No image files provided for exchange update: {}", savedExchange.getId());
                                    return Mono.just(savedExchange);
                                }
                            });
                })
                .doOnNext(this::publishApprovedExchangeEvent);
    }

    private Mono<ExchangeResponse> enrichExchangeWithImage(Exchange exchange) {
        return fileClient.findFilesByOwnerId(exchange.getId(), ImageType.EXCHANGE)
                .next()
                .map(fileResponse -> {
                    ExchangeResponse response = exchangeMapper.toExchangeResponse(exchange);
                    response.setImage(fileResponse);
                    return response;
                });
    }

    private Mono<Exchange> createNewExchange(ExchangeRequest request, Jwt jwt) {
        return Mono.fromCallable(() -> {
            Exchange exchange = exchangeMapper.toExchange(request);
            setUserDetailsFromJwt(exchange, jwt);
            exchange.setStatus(ExchangeStatus.PENDING);
            return exchange;
        });
    }

    private Mono<Exchange> updateExistingExchange(Exchange exchange, Jwt jwt) {
        return Mono.fromCallable(() -> {
            setUserDetailsFromJwt(exchange, jwt);
            exchange.setStatus(ExchangeStatus.PENDING);
            return exchange;
        });
    }

    private void setUserDetailsFromJwt(Exchange exchange, Jwt jwt) {
        exchange.setFirstName(jwt.getClaim(StandardClaimNames.GIVEN_NAME));
        exchange.setLastName(jwt.getClaim(StandardClaimNames.FAMILY_NAME));
        exchange.setEmail(jwt.getClaim(StandardClaimNames.EMAIL));
        exchange.setPhone(jwt.getClaim(StandardClaimNames.PHONE_NUMBER));
    }

    private Mono<Void> uploadExchangeImage(Flux<FilePart> imageFile, Long exchangeId) {
        return Mono.fromRunnable(() -> {
            fileClient.uploadFiles(imageFile, exchangeId, ImageType.EXCHANGE)
                    .subscribe(
                            response -> log.info("Uploaded image for exchange: {}", exchangeId),
                            error -> log.error("Failed to upload image for exchange: {}", exchangeId, error)
                    );
        });
    }

    private Mono<Void> updateExchangeImage(Flux<FilePart> imageFile, Long exchangeId) {
        return Mono.fromRunnable(() -> {
            fileClient.updateFiles(imageFile, exchangeId, ImageType.EXCHANGE)
                    .subscribe(
                            response -> log.info("Updated image for exchange: {}", exchangeId),
                            error -> log.error("Failed to update image for exchange: {}", exchangeId, error)
                    );
        });
    }

    private void publishApprovedExchangeEvent(Exchange exchange) {
        if (!exchange.getStatus().equals(ExchangeStatus.APPROVED)) {
            return;
        }

        var exchangeEvent = exchangeMapper.toExchangeEvent(exchange);
        var result = streamBridge.send("exchange-out-0", exchangeEvent);
        log.info("Result of sending exchange with order id {}: {}", exchange.getOrderId(), result);
    }
}