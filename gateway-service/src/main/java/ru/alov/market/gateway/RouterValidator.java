package ru.alov.market.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RouterValidator {

    public static final List<String> needAuthenticatedApiEndpoints = List.of(
            "core/api/v1/orders"
    );

    public static final Map<String, String> needRoleApiEndpoints = Map.of(
//            "core/api/v1/orders", "ROLE_ADMIN"
    );

    public boolean needAuthorization(ServerHttpRequest request) {
        return needAuthenticatedApiEndpoints.stream().anyMatch(uri -> request.getURI().getPath().contains(uri));
    }

    public boolean needAnyRole(ServerHttpRequest request) {
        return needRoleApiEndpoints.entrySet().stream().anyMatch(stringStringEntry -> request.getURI().getPath().contains(stringStringEntry.getKey()));
    }

    public boolean checkRoles(ServerHttpRequest request, List<String> roles) {
        for (Map.Entry<String, String> entry : needRoleApiEndpoints.entrySet()) {
            if (request.getURI().getPath().contains(entry.getKey())) {
                if (!roles.contains(entry.getValue())) return false;
            }
        }
        return true;
    }
}
