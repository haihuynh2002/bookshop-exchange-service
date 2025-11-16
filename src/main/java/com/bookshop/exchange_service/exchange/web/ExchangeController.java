package com.bookshop.exchange_service.exchange.web;

import com.bookshop.exchange_service.exchange.domain.Exchange;
import com.bookshop.exchange_service.exchange.domain.ExchangeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("exchanges")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping
    public Flux<ExchangeResponse> getExchanges() {
        return exchangeService.getAllExchanges();
    }

    @GetMapping("/my-exchanges")
    public Flux<ExchangeResponse> getMyExchanges(@AuthenticationPrincipal Jwt jwt) {
        return exchangeService.getMyExchanges(jwt);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Exchange> requestExchange(
            @ModelAttribute @Valid ExchangeRequest request,
            @RequestPart(value = "image", required = false) Flux<FilePart> imageFile,
            @AuthenticationPrincipal Jwt jwt) {
        return exchangeService.createExchange(request, imageFile, jwt);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Exchange> updateExchange(
            @PathVariable Long id,
            @ModelAttribute @Valid ExchangeUpdateRequest request,
            @RequestPart(value = "image", required = false) Flux<FilePart> imageFile) {
        return exchangeService.updateExchange(id, request, imageFile);
    }

    @GetMapping("/{id}")
    public Mono<ExchangeResponse> getExchange(@PathVariable Long id) {
        return exchangeService.getExchange(id);
    }
}
