package ru.alov.market.core.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.api.exception.CartServiceAppError;
import ru.alov.market.api.exception.CartServiceIntegrationException;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CartServiceIntegration {
    private final WebClient cartServiceWebClient;

    public Mono<CartDto> getCurrentUserCart(String username) {
        return cartServiceWebClient.get()
                                   .uri("/api/v1/cart/0")
                                   .header("username", username)
                                   .retrieve()
                                   .onStatus(
                                           HttpStatus::is4xxClientError,
                                           getClientResponseMonoFunction()
                                   ).onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_INTERNAL_EXCEPTION.name())))
                                   .bodyToMono(CartDto.class);
    }

    public Mono<ResponseEntity<Void>> clearCart(String username) {
        return cartServiceWebClient.get()
                                   .uri("/api/v1/cart/0/clear")
                                   .header("username", username)
                                   .retrieve()
                                   .onStatus(HttpStatus::is4xxClientError,
                                           getClientResponseMonoFunction())
                                   .onStatus(HttpStatus::is5xxServerError,
                                           clientResponse -> Mono.error(new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_INTERNAL_EXCEPTION.name())))
                                   .toBodilessEntity();
    }

    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse -> clientResponse.bodyToMono(CartServiceAppError.class).map(
                body -> {
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_SERVICE_RESOURCE_NOT_FOUND.name())) {
                        return new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_RESOURCE_NOT_FOUND.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_SERVICE_WEBCLIENT_REQUEST.name())) {
                        return new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_WEBCLIENT_REQUEST.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name())) {
                        return new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name() + ": " + body.getMessage());
                    }
                    return new CartServiceIntegrationException(CartServiceAppError.CartServiceErrors.CART_SERVICE_BAD_REQUEST.name() + ": " + body.getMessage());
                }
        );
    }

}
