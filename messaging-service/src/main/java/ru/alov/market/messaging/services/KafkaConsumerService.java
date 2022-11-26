package ru.alov.market.messaging.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.OrderDto;
import ru.alov.market.api.dto.RecoverPasswordDto;
import ru.alov.market.api.dto.UserProfileDto;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MailService mailService;
    private static final String USER_PROFILE_DTO_TOPIC = "USER_PROFILE_DTO";
    private static final String ORDER_DTO_TOPIC = "ORDER_DTO";
    private static final String RECOVER_PASSWORD_DTO_TOPIC = "RECOVER_PASSWORD_DTO";
    private static final String CONFIRM_EMAIL_TOPIC = "CONFIRM_EMAIL";

    @KafkaListener(topics = USER_PROFILE_DTO_TOPIC, groupId = "registration.server", containerFactory = "userProfileDtoContainerFactory")
    public void userDtoListener(UserProfileDto userProfileDto) throws MessagingException {
        mailService.sendRegistrationMail(userProfileDto);
    }

    @KafkaListener(topics = CONFIRM_EMAIL_TOPIC, groupId = "confirm-email.server", containerFactory = "userProfileDtoContainerFactory")
    public void confirmEmailListener(UserProfileDto userProfileDto) throws MessagingException {
        mailService.sendConfirmEmailMail(userProfileDto);
    }

    @KafkaListener(topics = ORDER_DTO_TOPIC, groupId = "order.server", containerFactory = "orderDtoContainerFactory")
    public void listenerOrder(OrderDto orderDto) throws MessagingException {
        mailService.sendCheckOnOrderMail(orderDto);
    }

    @KafkaListener(topics = RECOVER_PASSWORD_DTO_TOPIC, groupId = "recover-password.server", containerFactory = "recoverPasswordDtoContainerFactory")
    public void listenerRecoverPassword(RecoverPasswordDto recoverPasswordDto) throws MessagingException {
        mailService.sendRecoverPasswordMail(recoverPasswordDto);
    }

}
