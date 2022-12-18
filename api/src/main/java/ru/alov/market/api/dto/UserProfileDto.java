package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    @NotNull
    private Long userId;

    @NotBlank
    private String username;

    @NotNull
    @Email
    private String email;

    @NotEmpty
    private List<String> roles;

    @NotBlank
    private String emailStatus;

    public UserProfileDto(String username, String email, List<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
