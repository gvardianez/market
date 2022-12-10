package ru.alov.market.messaging.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alov.market.api.dto.NewsDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.messaging.services.NewsService;

import javax.mail.MessagingException;
import javax.management.relation.Role;
import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public void sendNews(@RequestHeader String role, @RequestBody NewsDto newsDto) throws MessagingException, TelegramApiException {
        checkRole(role,List.of(RoleStatus.ROLE_ADMIN.toString()));
        newsService.sendAnyNews(newsDto);
    }

    private void checkRole(String role, List<String> roleStatusList ){
        if (!roleStatusList.contains(role)) {
            throw new SecurityException("Недостаточно прав доступа");
        }
    }


}
