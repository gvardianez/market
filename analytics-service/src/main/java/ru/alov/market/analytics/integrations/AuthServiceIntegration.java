package ru.alov.market.analytics.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.exception.AuthServiceAppError;
import ru.alov.market.api.exception.AuthServiceIntegrationException;
import ru.alov.market.api.exception.CoreServiceAppError;
import ru.alov.market.api.exception.CoreServiceIntegrationException;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthServiceIntegration {

    private final WebClient authServiceWebClient;

    public Mono<UserProfileDto> getUserProfileDto(String username) {
        return authServiceWebClient.get()
                                   .uri("/api/v1/account")
                                   .header("username", username)
                                   .retrieve()
                                   .onStatus(HttpStatus::is4xxClientError,
                                           getClientResponseMonoFunction())
                                   .onStatus(HttpStatus::is5xxServerError,
                                           clientResponse -> Mono.error(new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_INTERNAL_EXCEPTION.name())))
                                   .bodyToMono(UserProfileDto.class);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse -> clientResponse.bodyToMono(AuthServiceAppError.class).map(
                body -> {
                    if (body.getCode().equals(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_RESOURCE_NOT_FOUND.name())) {
                        return new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_RESOURCE_NOT_FOUND.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name())) {
                        return new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_SECURITY.name())) {
                        return new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_SECURITY.name() + ": " + body.getMessage());
                    }
                    if (body.getCode().equals(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_CREDENTIALS.name())) {
                        return new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_CREDENTIALS.name() + ": " + body.getMessage());
                    }
                    return new AuthServiceIntegrationException(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name() + ": " + body.getMessage());
                }
        );
    }

}
