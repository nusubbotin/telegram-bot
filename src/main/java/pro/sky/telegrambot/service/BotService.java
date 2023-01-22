package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;

@Service
public class BotService implements UpdatesListener {
    private final NotificationService notificationService;
    private final TelegramBot telegramBot;

    public BotService(NotificationService notificationService, TelegramBot telegramBot) {
        this.notificationService = notificationService;
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> list) {
        list.stream()
                .filter(update -> update.message() != null)
                .filter(update -> update.message().text() != null)
                .forEach(this::procUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    private void procUpdate(Update update) {
        String mess = update.message().text();
        Long chatId = update.message().chat().id();

        if (mess.equals("/start")){
            telegramBot.execute(
                    new SendMessage(
                            chatId, "Привет! хочешь чтобы я тебе напомнил о каком-нибудь событии!?"
                    )
            );
        }else{
            if (notificationService.procNotification(chatId, mess)){
                telegramBot.execute(new SendMessage(chatId, "Событие создано"));
            }else{
                telegramBot.execute(new SendMessage(chatId, "Я не понял формат твоего сообщения! пример формата: '01.01.2022 20:00 Сделать домашнюю работу'"));
            }

        }
    }
}
