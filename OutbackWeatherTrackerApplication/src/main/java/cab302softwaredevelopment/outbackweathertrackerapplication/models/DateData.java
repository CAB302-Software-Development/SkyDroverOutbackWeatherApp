package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class DateData {
    @Getter
    private long dayStartEpoch;
    @Getter
    private long dayEndEpoch;

    public DateData(LocalDate date, int daysSpan) {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(daysSpan).toEpochSecond();
    }

    public DateData(LocalDate date) {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(1).toEpochSecond();
    }

    public static long getNearestHourEpoch(ZonedDateTime now) {
        if (now.getMinute() >= 30) now = now.plusHours(1);
        return now.truncatedTo(ChronoUnit.HOURS).toEpochSecond();
    }
}
