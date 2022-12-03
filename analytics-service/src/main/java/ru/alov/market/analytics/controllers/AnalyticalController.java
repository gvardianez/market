package ru.alov.market.analytics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.alov.market.analytics.services.AnalyticalService;
import ru.alov.market.api.dto.ProductRatingDto;
import ru.alov.market.api.dto.RequestRatingDto;
import ru.alov.market.api.dto.UserProductsRatingDto;

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
    public Mono<List<ProductRatingDto>> getYesterdayProductQuantityRating() {
        LocalDateTime localDateTimeStart = LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(0, 0));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        return analyticalService.getProductQuantityRatingYesterday(localDateTimeStart, localDateTimeEnd);
    }

    @PostMapping("/product_quantity_rating_period")
    public Mono<List<ProductRatingDto>> getProductQuantityRatingForPeriod(@RequestBody RequestRatingDto requestRatingDto) {
        return analyticalService.getProductQuantityRatingPeriod(requestRatingDto);
    }

    @PostMapping("/product_quantity_cost_rating_period")
    public Mono<List<ProductRatingDto>> getProductQuantityAndCostRatingForPeriod(@RequestBody RequestRatingDto requestRatingDto) {
        return analyticalService.getProductQuantityAndCostRating(requestRatingDto);
    }

    @PostMapping("/user_products_rating_period")
    public Mono<UserProductsRatingDto> getUserProductsRatingForPeriod(@RequestBody RequestRatingDto requestRatingDto) {
        return analyticalService.getUserProductsStatistic(requestRatingDto.getUsername(),requestRatingDto.getLocalDateTimeStart(), requestRatingDto.getLocalDateTimeEnd());
    }

}
