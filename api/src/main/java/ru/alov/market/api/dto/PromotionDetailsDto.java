package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailsDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @FutureOrPresent
    private LocalDateTime startedAt;

    @Future
    private LocalDateTime endedAt;

}
