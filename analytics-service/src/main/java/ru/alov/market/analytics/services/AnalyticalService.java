package ru.alov.market.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.alov.market.analytics.integrations.CoreServiceIntegration;
import ru.alov.market.analytics.repositories.AnalyticalRepository;
import ru.alov.market.analytics.repositories.projections.AnalyticalProductsQuantityRating;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.dto.ProductQuantityRatingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticalService {

    private final AnalyticalRepository analyticalRepository;
    private final CoreServiceIntegration coreServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

    public Mono<List<ProductQuantityRatingDto>> getProductQuantityRating(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
        List<ProductQuantityRatingDto> productQuantityRating = (List<ProductQuantityRatingDto>) redisTemplate.opsForValue().get(LocalDate.from(localDateTimeStart).toString());
        if (productQuantityRating == null) {
            List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings = analyticalRepository.findProductQuantityRating(localDateTimeStart, localDateTimeEnd);
            List<Long> productIds = analyticalProductsQuantityRatings.stream().map(AnalyticalProductsQuantityRating::getProductId).collect(Collectors.toList());
            Mono<List<ProductQuantityRatingDto>> listMono = coreServiceIntegration.getListProductDto(new ListDto<>(productIds)).collectList().map(productDtos -> {
                List<ProductQuantityRatingDto> productQuantityRatingDtoList = new ArrayList<>();
                for (ProductDto productDto : productDtos) {
                    for (AnalyticalProductsQuantityRating analyticalProductsQuantityRating :
                            analyticalProductsQuantityRatings) {
                        if (productDto.getId().equals(analyticalProductsQuantityRating.getProductId())) {
                            productQuantityRatingDtoList.add(new ProductQuantityRatingDto(productDto, analyticalProductsQuantityRating.getQuantity()));
                            break;
                        }
                    }
                }
                return productQuantityRatingDtoList;
            });
            return listMono.doOnSuccess(productQuantityRatingDtos -> redisTemplate.opsForValue().set(LocalDate.from(localDateTimeStart).toString(), productQuantityRatingDtos));
        } else return Mono.just(productQuantityRating);
    }

}
