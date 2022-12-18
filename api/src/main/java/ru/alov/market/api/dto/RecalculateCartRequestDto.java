package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecalculateCartRequestDto {

    @NotNull
    private CartDto cartDto;

    @NotNull
    @FutureOrPresent
    private LocalDateTime localDateTime;

}
