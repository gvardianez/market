package ru.alov.market.auth.validators;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.RegisterUserDto;
import ru.alov.market.auth.exceptions.FieldValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegistrationValidator {

    public void validate(RegisterUserDto registerUserDto) {
        List<String> errors = new ArrayList<>();
        if (registerUserDto.getUsername() == null || registerUserDto.getUsername().isBlank()) {
            errors.add("Поле имя пользователя не должно быть пустым");
        }
        if (registerUserDto.getEmail() == null || registerUserDto.getEmail().isBlank()) {
            errors.add("Поле email должно быть заполнено");
        }
        if (registerUserDto.getPassword() == null || registerUserDto.getConfirmPassword() == null ||
                registerUserDto.getPassword().isBlank() || registerUserDto.getConfirmPassword().isBlank()) {
            errors.add("Поля с паролями должны быть заполнены");
        }
        if (registerUserDto.getPassword() != null && registerUserDto.getConfirmPassword() != null && !registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            errors.add("Введеные пароли не совпадают");
        }
        if (!errors.isEmpty()) {
            throw new FieldValidationException(errors);
        }
    }
}
