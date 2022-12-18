package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRatingDto {

    private String username;

    @NotNull
    private LocalDateTime localDateTimeStart;

    @NotNull
    private LocalDateTime localDateTimeEnd;

    private Long count;


}
