package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель запроса токена безопасности")
public class JwtRequest {

    @Schema(description = "Имя пользователя", required = true, example = "Ivan")
    private String username;

    @Schema(description = "Пароль пользователя пользователя", required = true, example = "1010")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
