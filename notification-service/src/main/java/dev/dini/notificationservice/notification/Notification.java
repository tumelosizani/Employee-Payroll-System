package dev.dini.notificationservice.notification;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    private String Id;

    private NotificationType type;
    private LocalDateTime notificationDate;

}
