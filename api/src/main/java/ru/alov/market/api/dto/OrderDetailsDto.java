package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Модель доп. информации о заказе")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {

    @NotBlank
    @Schema(description = "Адрес заказа", required = true, example = "Tula, 2-45")
    private String address;

    @NotBlank
    @Schema(description = "Телефон заказчика", required = true, example = "561461263")
    private String phone;

    private String email;

}
