package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {

    private Long id;

    private String title;

    private String description;

    private List<PromotionItemDto> itemDtoListDto;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

}
