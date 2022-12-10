package ru.alov.market.messaging.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.NewsDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.messaging.integrations.CoreServiceIntegration;
import ru.alov.market.messaging.telegram.TelegramBot;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final MailService mailService;
    private final TelegramBot telegramBot;
    private final CoreServiceIntegration coreServiceIntegration;

    public void sendAnyNews(NewsDto newsDto) throws TelegramApiException, MessagingException {
            telegramBot.sendMessageInChannel(newsDto.getMessage());
            mailService.sendNewsMessage(newsDto);
    }

    @Scheduled(cron = "${news.products.scheduler}")
    public void sendNewsNewProductsForYesterday() throws MessagingException, TelegramApiException {
        LocalDateTime localDateTimeStart = LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(0, 0));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        List<ProductDto> listDto = coreServiceIntegration.getProductsByCreatedPeriod(localDateTimeStart, localDateTimeEnd).getContent();
        if (!listDto.isEmpty()) {
            NewsDto newsDto = new NewsDto();
            newsDto.setSubject(NewsDto.SubjectTypes.NEW_PRODUCTS.getSubject());
            StringBuilder message = new StringBuilder();
            listDto.forEach(productDto -> message
                            .append(productDto.getTitle())
                            .append(", Цена: ")
                            .append(productDto.getPrice())
                            .append(" ,Категория: ")
                            .append(productDto.getCategoryTitle())
                            .append("\n\n"));
            newsDto.setMessage(message.toString());

            mailService.sendNewsMessage(newsDto);
            telegramBot.sendMessageInChannel(newsDto.getSubject() + ":\n" + newsDto.getMessage());
        }
    }

    private void createNewNews(String subject, String c) {
    }

}
