package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.RecoverPasswordDto;
import ru.alov.market.api.dto.UserProfileDto;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, UserProfileDto> userProfileDtoKafkaTemplate;
    private final KafkaTemplate<String, RecoverPasswordDto> recoverPasswordDtoKafkaTemplate;

    public void sendUserProfileDto(String topic, UserProfileDto userProfileDto) {
        userProfileDtoKafkaTemplate.send(topic, userProfileDto);
    }

    public void sendRecoverPasswordDto(String topic, RecoverPasswordDto recoverPasswordDto) {
        recoverPasswordDtoKafkaTemplate.send(topic, recoverPasswordDto);
    }

}
