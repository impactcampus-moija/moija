package impact.moija.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    private ZonedDateTime getCurrentSeoulTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public String getCreatedDateTimeToString() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getCreatedTimeToString() {
        return createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getFormattedCreatedAt() {
        ZonedDateTime now = getCurrentSeoulTime();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() == 0) {
            return "방금 전";
        }
        if (duration.toMinutes() < 60) {
            long minutes = duration.toMinutes();
            return minutes + "분 전";
        }
        if (duration.toHours() < 24) {
            long hours = duration.toHours();
            return hours + "시간 전";
        }
        if (duration.toDays() < 5) {
            long days = duration.toDays();
            return days + "일 전";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return createdAt.format(formatter);
    }
}