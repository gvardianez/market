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
import ru.alov.market.core.exceptions.CartServiceIntegrationException;

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
                                           HttpStatus::is4xxClientError, // HttpStatus::is4xxClientError
                                           getClientResponseMonoFunction()
                                   )
                                   .bodyToMono(CartDto.class);
    }

    public Mono<ResponseEntity<Void>> clearCart(String username) {
        return cartServiceWebClient.get()
                                   .uri("/api/v1/cart/0/clear")
                                   .header("username", username)
                                   .retrieve()
                                   .onStatus(
                                           HttpStatus::is4xxClientError, // HttpStatus::is4xxClientError
                                           getClientResponseMonoFunction()
                                   )
                                   .toBodilessEntity();
    }

    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse -> clientResponse.bodyToMono(CartServiceAppError.class).map(
                body -> {
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_NOT_FOUND.name())) {
                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                    }
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_IS_BROKEN.name())) {
                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                    }
                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_SERVICE_CONNECT_TIMEOUT_EXCEEDED.name())) {
                        return new CartServiceIntegrationException("Превышено время ожидание ответа от сервиса корзин");
                    }
                    return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                }
        );
    }
}
