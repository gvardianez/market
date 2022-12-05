package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель токена безопасности")
public class JwtResponse {

    @Schema(description = "Строковое представление токена", required = true, example = "fsdffsdfsd.dfsdfsdfsdfdsfsdf.213wefwe3q2rwefserfwe")
    private String accessToken;

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtResponse() {
    }

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
