package ru.alov.market.analytics.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.CoreServiceAppError;
import ru.alov.market.api.exception.CoreServiceIntegrationException;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CoreServiceIntegration {

    private final WebClient coreServiceWebClient;

    public Mono<ResponseEntity<Void>> getTest() {
        return coreServiceWebClient.get()
                                   .uri("/api/v1/test")
                                   .retrieve()
                                   .onStatus(HttpStatus::is4xxClientError,
                                           getClientResponseMonoFunction())
                                   .onStatus(HttpStatus::is5xxServerError,
                                           clientResponse -> Mono.error(new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_INTERNAL_EXCEPTION.name())))
                                   .toBodilessEntity();
    }


    public Flux<ProductDto> getListProductDto(ListDto<Long> productIdList) {
        return coreServiceWebClient.post()
                                   .uri("/api/v1/products/get-products")
                                   .bodyValue(productIdList)
                                   .retrieve()
                                   .onStatus(HttpStatus::is4xxClientError,
                                           getClientResponseMonoFunction())
                                   .onStatus(HttpStatus::is5xxServerError,
                                           clientResponse -> Mono.error(new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_INTERNAL_EXCEPTION.name())))
                                   .bodyToFlux(ProductDto.class);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse -> clientResponse.bodyToMono(CoreServiceAppError.class).map(
                body -> {
                    if (body.getCode().equals(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_RESOURCE_NOT_FOUND.name())) {
                        return new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_RESOURCE_NOT_FOUND.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_FIELD_VALIDATION.name())) {
                        return new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_FIELD_VALIDATION.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_PROMOTION_INTEGRATION.name())) {
                        return new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_PROMOTION_INTEGRATION.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_CART_INTEGRATION.name())) {
                        return new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_CART_INTEGRATION.name() + ": " + body.getMessage());
                    }
                    return new CoreServiceIntegrationException(CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST.name() + ": " + body.getMessage());
                }
        );
    }

}
