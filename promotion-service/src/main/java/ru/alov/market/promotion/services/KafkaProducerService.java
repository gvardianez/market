package ru.alov.market.promotion.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.PromotionDto;
import ru.alov.market.api.dto.RecoverPasswordDto;
import ru.alov.market.api.dto.UserProfileDto;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, PromotionDto> promotionDtoKafkaTemplate;

    public void sendPromotionDto(String topic, PromotionDto promotionDto) {
        promotionDtoKafkaTemplate.send(topic, promotionDto);
    }

}
