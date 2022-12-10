package ru.alov.market.messaging.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.*;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.messaging.integrations.AuthServiceIntegration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;
    private final AuthServiceIntegration authServiceIntegration;

    public void sendRegistrationMail(UserProfileDto userProfileDto) throws MessagingException {
        sendMail(userProfileDto.getEmail(), "Регистрация успешно пройдена", "Имя пользователя " + userProfileDto.getUsername()
                + "\nперейдите по ссылке для подтверждения почты: " + "http://localhost:3000/market-front/#!/confirm_email/" + userProfileDto.getUsername() + "/" + userProfileDto.getEmail());
    }

    public void sendConfirmEmailMail(UserProfileDto userProfileDto) throws MessagingException {
        sendMail(userProfileDto.getEmail(), "Подтверждение почты",
                "\nперейдите по ссылке для подтверждения почты: " + "http://localhost:3000/market-front/#!/confirm_email/" + userProfileDto.getUsername() + "/" + userProfileDto.getEmail());
    }

    public void sendCheckOnOrderMail(OrderDto orderDto) throws MessagingException {
        sendMail(orderDto.getEmail(), "Заказ успешно оплачен", "Номер заказа: " + orderDto.getId());
    }

    public void sendRecoverPasswordMail(RecoverPasswordDto recoverPasswordDto) throws MessagingException {
        sendMail(recoverPasswordDto.getEmail(), "Пароль успешно изменен", "Новый пароль: " + recoverPasswordDto.getPassword());
    }

    public void sendNewsMessage(NewsDto newsDto) throws MessagingException {
        List<String> emails = authServiceIntegration.getSubscribersEmails(RoleStatus.ROLE_ADMIN.toString()).getContent();
        sendMail(emails.toArray(new String[0]), newsDto.getSubject(), newsDto.getMessage());

    }

    public void sendPromotionMessage(PromotionDto promotionDto) throws MessagingException {
        List<String> emails = authServiceIntegration.getSubscribersEmails(RoleStatus.ROLE_ADMIN.toString()).getContent();
        sendMail(emails.toArray(new String[0]), promotionDto.getTitle(), promotionDto.getDescription());
    }

    private void sendMail(String email, String subject, String text) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text, true);
        sender.send(message);
    }

    private void sendMail(String[] email, String subject, String text) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text, true);
        sender.send(message);
    }

}
