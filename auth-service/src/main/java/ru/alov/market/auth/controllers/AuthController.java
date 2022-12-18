package ru.alov.market.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alov.market.api.dto.*;
import ru.alov.market.api.exception.AppError;
import ru.alov.market.auth.services.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
@Tag(name = "Авторизация", description = "Методы работы с аторизацией пользователя")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Запрос на создание токена безопасности для пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            description = "Некорректный логин или пароль", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest) {
        JwtResponseDto jwtResponseDto = authService.getJwtTokens(authRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<JwtResponseDto> refreshTokens(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        return ResponseEntity.ok(authService.refreshJwtTokens(refreshJwtRequest.getRefreshToken()));
    }

}
