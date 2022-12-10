package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionItemDto {

    private Long id;

    private Long promotionId;

    private Long productId;

    private float discount;

    public PromotionItemDto(Long productId, float discount) {
        this.productId = productId;
        this.discount = discount;
    }

}
