package ru.alov.market.api.dto;

public class RefreshJwtRequest {

    private String refreshToken;

    public RefreshJwtRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshJwtRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
