package ru.alov.market.analytics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.alov.market.analytics.services.AnalyticalService;
import ru.alov.market.api.dto.ProductQuantityRatingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytical")
@RequiredArgsConstructor
public class AnalyticalController {

    private final AnalyticalService analyticalService;

    @GetMapping("/product_quantity_rating_yesterday")
    public Mono<List<ProductQuantityRatingDto>> getYesterdayProductQuantityRating() {
        LocalDateTime localDateTimeStart = LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(0, 0));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        return analyticalService.getProductQuantityRating(localDateTimeStart, localDateTimeEnd);
    }

}
