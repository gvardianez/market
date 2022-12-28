package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingDto {

    private ProductDto productDto;

    private Integer quantity;

    private BigDecimal cost;

    public ProductRatingDto(ProductDto productDto, Integer quantity) {
        this.productDto = productDto;
        this.quantity = quantity;
    }

}
