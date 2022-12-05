package ru.alov.market.api.dto;

public class RecoverPasswordDto {

    private String email;

    private String password;

    public RecoverPasswordDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public RecoverPasswordDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
