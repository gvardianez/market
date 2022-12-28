package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Модель корзины")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    @NotNull
    @Schema(description = "Список элементов корзины", required = true, example = "1 Конфеты 2 100.00 200.00")
    private List<CartItemDto> items;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Общая стоимость корзины", required = true, example = "500.00")
    private BigDecimal totalPrice;

}