package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateData {
    @Getter
    private long startEpoch;
    @Getter
    private long endEpoch;

    public DateData(LocalDate date, int daysSpan) {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        startEpoch = startOfDay.toEpochSecond();
        endEpoch = startOfDay.plusDays(daysSpan).toEpochSecond();
    }

    public DateData(LocalDate date) {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        startEpoch = startOfDay.toEpochSecond();
        endEpoch = startOfDay.plusDays(1).toEpochSecond();
    }
}
