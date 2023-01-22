package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {
    private static final Pattern NOTIFICATION_FORMAT = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    public NotificationService(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    public boolean procNotification(Long chatId, String mess){
        Matcher messMatcher = NOTIFICATION_FORMAT.matcher(mess);
        if (!messMatcher.matches()){
            return false;
        }
        String dateStr = messMatcher.group(1);
        String notify = messMatcher.group(3);
        try {
            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            NotificationTask task = new NotificationTask();
            task.setChatId(chatId);
            task.setDateTime(date);
            task.setNotify(notify);
            notificationTaskRepository.save(task);
            return true;
        }catch (DateTimeParseException e){
            return false;
        }
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotify(){
        LocalDateTime localDateTime;
        List<NotificationTask> tasks = notificationTaskRepository.findByDateTimeEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        tasks.forEach(task -> {
                    telegramBot.execute(new SendMessage(task.getChatId(), task.getNotify()));
        });
        notificationTaskRepository.deleteAll(tasks);
    }
}
