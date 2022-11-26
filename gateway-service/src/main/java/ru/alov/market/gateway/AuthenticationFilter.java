package ru.alov.market.gateway;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private RouterValidator routerValidator;
    private JwtUtil jwtUtil;

    @Autowired
    public void setRouterValidator(RouterValidator routerValidator) {
        this.routerValidator = routerValidator;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (request.getHeaders().containsKey("username") || request.getHeaders().containsKey("role")) {
                return this.onError(exchange, "Invalid header username, or role", HttpStatus.BAD_REQUEST);
            }

            if (!isAuthMissing(request)) {
                final String token = getAuthHeader(request);
                if (jwtUtil.isInvalid(token)) {
                    return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
                }
                populateRequestWithHeaders(exchange, token);
            }

            if (routerValidator.needAuthorization(request)) {
                if (isAuthMissing(request)) return this.onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            if (routerValidator.needAnyRole(request)) {
                if (isAuthMissing(request)) return this.onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
                final String token = getAuthHeader(request);
                List<String> roles = jwtUtil.getAllClaimsFromToken(token).get("roles", List.class);
                if (!routerValidator.checkRoles(request, roles)) {
                    return this.onError(exchange,
                            "Insufficient access rights", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey("Authorization")) {
            return true;
        }
        if (!request.getHeaders().getOrEmpty("Authorization").get(0).startsWith("Bearer ")) {
            return true;
        }
        return false;
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("username", claims.getSubject())
                .header("email", (String) claims.get("email"))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}