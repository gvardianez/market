package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecalculateCartRequestDto {

    private CartDto cartDto;

    private LocalDateTime localDateTime;

}
