package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Модель заказа")
public class OrderDto {

    @Schema(description = "ID заказа", required = true, example = "1")
    private Long id;

    @Schema(description = "Список элементов заказа", required = true, example = "1 Конфеты 2 100.00 200.00")
    private List<OrderItemDto> items;

    @Schema(description = "Стоимость заказа", required = true, example = "500.00")
    private BigDecimal totalPrice;

    private String userName;

    private String email;

    @Schema(description = "Адресс заказа", required = true, example = "Tula")
    private String address;

    @Schema(description = "Телефон заказчика", required = true, example = "561461263")
    private String phone;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderDto() {
    }

    public OrderDto(Long id, List<OrderItemDto> items, BigDecimal totalPrice, String address, String phone, String status, String userName , String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.userName= userName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
