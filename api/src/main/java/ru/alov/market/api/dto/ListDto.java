package ru.alov.market.api.dto;

import java.util.List;

public class ListDto<T> {

    private List<T> content;

    public ListDto() {
    }

    public ListDto(List<T> content) {
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
