package ru.alov.market.api.dto;

public class StringResponseDto {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringResponseDto() {
    }

    public StringResponseDto(String value) {
        this.value = value;
    }
}
