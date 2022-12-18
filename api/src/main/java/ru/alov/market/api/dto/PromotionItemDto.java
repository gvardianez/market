package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionItemDto {

    private Long id;

    @NotNull
    private Long promotionId;

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    @DecimalMax("99.99")
    private float discount;

    public PromotionItemDto(Long productId, float discount) {
        this.productId = productId;
        this.discount = discount;
    }

}
