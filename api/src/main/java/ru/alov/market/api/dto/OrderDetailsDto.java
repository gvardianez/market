package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель доп. информации о заказе")
public class OrderDetailsDto {

    @Schema(description = "Адрес заказа", required = true, example = "Tula, 2-45")
    private String address;

    @Schema(description = "Телефон заказчика", required = true, example = "561461263")
    private String phone;

    @Schema(description = "Телефон заказчика", required = true, example = "561461263")
    private String email;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OrderDetailsDto(String address, String phone, String email) {
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public OrderDetailsDto() {
    }
}
