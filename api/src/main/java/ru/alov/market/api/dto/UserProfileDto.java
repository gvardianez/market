package ru.alov.market.api.dto;

import java.util.List;

public class UserProfileDto {

    private Long userId;

    private String username;

    private String email;

    private List<String> roles;

    private String emailStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public UserProfileDto(){
    }

    public UserProfileDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserProfileDto(String username, String email, List<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UserProfileDto(Long userId, String username, String email, List<String> roles, String emailStatus) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.emailStatus = emailStatus;
    }
}
