package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    private Long productId;

    private String username;

    private Integer grade;

    private String description;

    private LocalDateTime createdAt;

}
