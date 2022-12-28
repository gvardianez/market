package ru.alov.market.core.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.api.dto.NumberDto;
import ru.alov.market.api.dto.RecalculateCartRequestDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.api.exception.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PromotionServiceIntegration {

    private final WebClient promotionServiceWebClient;

    public Mono<NumberDto> getProductDiscount(Long productId, LocalDateTime start, LocalDateTime end) {
        return promotionServiceWebClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                .path("/api/v1/promotions/discount/" + productId)
                                                .queryParam("start", start)
                                                .queryParam("end", end)
                                                .build())
                                        .header("role", RoleStatus.ROLE_ADMIN.toString())
                                        .retrieve()
                                        .onStatus(HttpStatus::is4xxClientError,
                                                getClientResponseMonoFunction())
                                        .onStatus(HttpStatus::is5xxServerError,
                                                clientResponse -> Mono.error(new PromotionServiceIntegrationException(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_INTERNAL_EXCEPTION.name())))
                                        .bodyToMono(NumberDto.class);
    }

    public Mono<CartDto> getRecalculateCart(RecalculateCartRequestDto recalculateCartRequestDto) {
        return promotionServiceWebClient.post()
                                        .uri("/api/v1/promotions/recalculate-cart/")
                                        .header("role", RoleStatus.ROLE_ADMIN.toString())
                                        .bodyValue(recalculateCartRequestDto)
                                        .retrieve()
                                        .onStatus(HttpStatus::is4xxClientError,
                                                getClientResponseMonoFunction())
                                        .onStatus(HttpStatus::is5xxServerError,
                                                clientResponse -> Mono.error(new PromotionServiceIntegrationException(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_INTERNAL_EXCEPTION.name())))
                                        .bodyToMono(CartDto.class);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse -> clientResponse.bodyToMono(PromotionServiceAppError.class).map(
                body -> {
                    if (body.getCode().equals(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_RESOURCE_NOT_FOUND.name())) {
                        return new PromotionServiceIntegrationException(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_RESOURCE_NOT_FOUND.name()+ ": " + body.getMessage());
                    }
                    return new PromotionServiceIntegrationException(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_BAD_REQUEST.name()+ ": " + body.getMessage());
                }
        );
    }


}
