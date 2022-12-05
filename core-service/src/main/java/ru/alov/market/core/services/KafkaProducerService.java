package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.OrderDto;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderDto> orderDtoKafkaTemplate;

    public void sendOrderDto(String topic, OrderDto orderDto) {
        orderDtoKafkaTemplate.send(topic, orderDto);
    }

}
