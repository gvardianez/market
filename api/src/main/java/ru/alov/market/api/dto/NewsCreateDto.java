package ru.alov.market.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateDto {

    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    public enum SubjectTypes {

        NEW_PRODUCTS("Новые продукты");

        private final String subject;

        SubjectTypes(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }
    }

}
