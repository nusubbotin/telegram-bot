package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private LocalDateTime dateTime;
    private String notify;

    public NotificationTask() {
    }

    public NotificationTask(Long id, Long chatId, LocalDateTime dateTime, String notify) {
        this.id = id;
        this.chatId = chatId;
        this.dateTime = dateTime;
        this.notify = notify;
    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getNotify() {
        return notify;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }
}
