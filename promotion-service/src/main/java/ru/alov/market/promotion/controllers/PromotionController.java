package ru.alov.market.promotion.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.*;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.promotion.converters.mapstruct.PromotionMapper;
import ru.alov.market.promotion.services.KafkaProducerService;
import ru.alov.market.promotion.services.PromotionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/active")
    public List<PromotionDto> getActivePromotionsByPeriod(@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return promotionService.getActiveByPeriod(start, end)
                               .stream()
                               .map(promotionMapper::entityToDto)
                               .collect(Collectors.toList());
    }

    @GetMapping
    public List<PromotionDto> getAllPromotionsByPeriod(@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return promotionService.getAllByPeriod(start, end)
                               .stream()
                               .map(promotionMapper::entityToDto)
                               .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionDto createPromotion(@RequestBody PromotionDetailsDto promotionDetailsDto) {
        PromotionDto promotionDto = promotionMapper.entityToDto(promotionService.createNewPromotion(promotionDetailsDto));
        kafkaProducerService.sendPromotionDto(KafkaTopic.PROMOTION_DTO.toString(), promotionDto);
        return promotionDto;
    }

    @GetMapping("/add-product")
    public void addProductToNewPromotion(@RequestParam Long productId, @RequestParam(required = false) Float discount) {
        promotionService.addProductToNewPromotion(productId, discount);
    }

    @GetMapping("/remove/{productId}")
    public void removeProductFromNewPromotion(@PathVariable Long productId) {
        promotionService.removeProductFromNewPromotion(productId);
    }

    @GetMapping("/clear")
    public void clearNewPromotion() {
        promotionService.clearNewPromotion();
    }

    @GetMapping("/set-discount")
    public void setDiscountInNewPromotion(@RequestParam Long productId, @RequestParam Float discount) {
        promotionService.setProductDiscountInNewPromotion(productId, discount);
    }

    @GetMapping("/discount/{productId}")
    public Mono<NumberDto<Float>> getProductDiscount(@PathVariable Long productId,
                                                     @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                     @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return promotionService.getDiscount(productId, start, end).map(NumberDto::new);
    }

    @PostMapping("/recalculate-cart/")
    public Mono<CartDto> recalculateCart(@RequestBody RecalculateCartRequestDto recalculateCartRequestDto) {
        return promotionService.recalculateCart(recalculateCartRequestDto.getCartDto(), recalculateCartRequestDto.getLocalDateTime());
    }

    @DeleteMapping("/{promotionId}")
    public void delete(@PathVariable Long promotionId) {
        promotionService.deletePromotion(promotionId);
    }

}
