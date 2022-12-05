package ru.alov.market.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.dto.RegisterUserDto;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.auth.converters.UserConverter;
import ru.alov.market.api.exception.AppError;
import ru.alov.market.auth.services.KafkaProducerService;
import ru.alov.market.auth.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
@Tag(name = "Регистрация", description = "Методы для регистрации нового пользователя")
public class RegistrationController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final KafkaProducerService kafkaProducerService;

    @Operation(
            summary = "Запрос на создание нового пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
                    ),
                    @ApiResponse(
                            description = "Имя пользователя или Email уже используются. Неправильно заполнены поля", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping()
    public UserProfileDto registrationNewUser(@RequestBody RegisterUserDto registerUserDto) {
        UserProfileDto userProfileDto = userConverter.entityToDto(userService.createNewUser(registerUserDto));
        kafkaProducerService.sendUserProfileDto(KafkaTopic.USER_PROFILE_DTO.toString(), userProfileDto);
        return userProfileDto;
    }

    @GetMapping("/confirm_email")
    public void confirmUserEmail(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "email") String email) {
        userService.confirmUserEmail(username, email);
    }

}
