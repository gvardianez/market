package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProductsRatingDto {

    private UserProfileDto userProfileDto;

    private List<ProductRatingDto> productRatingDtoList;

}
