package ru.alov.market.analytics.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoreServiceIntegration {

    private final WebClient coreServiceWebClient;

    public Flux<ProductDto> getListProductDto(ListDto<Long> productIdList) {
        return coreServiceWebClient.post()
                                   .uri("/api/v1/products/get_products")
                                   .bodyValue(productIdList)
                                   .retrieve()
                                   .bodyToFlux(ProductDto.class);
    }

//    public Mono<ResponseEntity<Void>> clearCart(String username) {
//        return cartServiceWebClient.get()
//                                   .uri("/api/v1/cart/0/clear")
//                                   .header("username", username)
//                                   .retrieve()
//                                   .toBodilessEntity();
//    }

//    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
//        return clientResponse -> clientResponse.bodyToMono(CartServiceAppError.class).map(
//                body -> {
//                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_NOT_FOUND.name())) {
//                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
//                    }
//                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_IS_BROKEN.name())) {
//                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина сломана");
//                    }
//                    if (body.getCode().equals(CartServiceAppError.CartServiceErrors.CART_SERVICE_CONNECT_TIMEOUT_EXCEEDED.name())) {
//                        return new CartServiceIntegrationException("Превышено время ожидание ответа от сервиса корзин");
//                    }
//                    return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
//                }
//        );
//    }
}
