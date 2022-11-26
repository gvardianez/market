package ru.alov.market.auth.validators;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.ChangePasswordDto;
import ru.alov.market.auth.exceptions.FieldValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangePasswordValidator {

    public void validate(ChangePasswordDto updateUserPasswordDto) {
        List<String> errors = new ArrayList<>();
        if (updateUserPasswordDto.getNewPassword() == null || updateUserPasswordDto.getConfirmNewPassword() == null || updateUserPasswordDto.getOldPassword() == null ||
                updateUserPasswordDto.getNewPassword().isBlank() || updateUserPasswordDto.getConfirmNewPassword().isBlank() || updateUserPasswordDto.getOldPassword().isBlank()) {
            errors.add("Поля с паролями должны быть заполнены");
        }
        if (updateUserPasswordDto.getNewPassword() != null && updateUserPasswordDto.getConfirmNewPassword() != null && !updateUserPasswordDto.getNewPassword().equals(updateUserPasswordDto.getConfirmNewPassword())) {
            errors.add("Введеные пароли не совпадают");
        }
        if (!errors.isEmpty()) {
            throw new FieldValidationException(errors);
        }
    }

}
