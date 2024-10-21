package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateData {
    @Getter
    private long dayStartEpoch;
    @Getter
    private long dayEndEpoch;
    private final ZonedDateTime timeNow;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateData(LocalDateTime date, int daysSpan) {
        timeNow = date.atZone(ZoneId.systemDefault());
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(daysSpan).toEpochSecond();
    }

    public DateData(LocalDateTime date) {
        timeNow = date.atZone(ZoneId.systemDefault());
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(1).toEpochSecond();
    }

    public static long getNearestHourEpoch(ZonedDateTime now) {
        if (now.getMinute() >= 30) now = now.plusHours(1);
        return now.truncatedTo(ChronoUnit.HOURS).toEpochSecond();
    }

    public static long getNearestMinuteEpoch(ZonedDateTime now) {
        if (now.getMinute() >= 30) now = now.plusMinutes(1);
        return now.truncatedTo(ChronoUnit.MINUTES).toEpochSecond();
    }

    public long getCurrentEpoch() {
        return timeNow.toEpochSecond();
    }

    @Override
    public String toString() {
        return timeNow.format(formatter);
    }
}
