package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alov.market.api.validation.Marker;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    @NotNull
    private Long productId;

    @NotBlank
    private String username;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer grade;

    @NotBlank
    private String description;

    private LocalDateTime createdAt;

}
