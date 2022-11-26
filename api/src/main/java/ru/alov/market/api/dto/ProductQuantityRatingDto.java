package ru.alov.market.api.dto;

public class ProductQuantityRatingDto {

    private ProductDto productDto;

    private Integer quantity;

    public ProductQuantityRatingDto() {
    }

    public ProductQuantityRatingDto(ProductDto productDto, Integer quantity) {
        this.productDto = productDto;
        this.quantity = quantity;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
