package ru.alov.market.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alov.market.api.dto.ChangePasswordDto;
import ru.alov.market.api.dto.RecoverPasswordDto;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.converters.UserConverter;
import ru.alov.market.auth.services.KafkaProducerService;
import ru.alov.market.auth.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Tag(name = "Личный кабинет", description = "Методы для работы в личном кабинете пользователя")
public class AccountController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping()
    public UserProfileDto enterAccount(@RequestHeader String username) {
        return userConverter.entityToDto(userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("User '%s' not found", username))));
    }

    @PutMapping("/change_password")
    public void changePassword(@RequestHeader String username, @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(username, changePasswordDto);
    }

    @GetMapping("/recover_password")
    public void recoverPassword(@RequestHeader String username, @RequestHeader String email) {
        kafkaProducerService.sendRecoverPasswordDto(KafkaTopic.RECOVER_PASSWORD_DTO.toString(), new RecoverPasswordDto(email, userService.recoverPassword(username)));
    }

    @GetMapping("/confirm_email")
    public void confirmEmail(@RequestHeader String username, @RequestHeader String email) {
        kafkaProducerService.sendUserProfileDto(KafkaTopic.CONFIRM_EMAIL.toString(), new UserProfileDto(username, email));
    }

}
