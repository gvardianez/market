package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailsDto {

    private String title;

    private String description;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

}
