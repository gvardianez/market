package ru.alov.market.core.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alov.market.api.dto.ReviewDto;
import ru.alov.market.core.converters.ReviewConverter;
import ru.alov.market.core.services.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewConverter reviewConverter;

    @PostMapping
    public ReviewDto createReview(@RequestHeader String username, @RequestBody ReviewDto reviewDto) {
        return reviewConverter.entityToDto(reviewService.createReview(username, reviewDto));
    }


}
