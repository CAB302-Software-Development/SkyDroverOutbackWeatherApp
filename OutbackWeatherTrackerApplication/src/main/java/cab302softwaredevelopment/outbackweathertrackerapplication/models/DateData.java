package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for managing date and time data relevant to weather and forecasting.
 * Provides methods for calculating epoch times for specific day spans, the start and end of days,
 * and the nearest hour or minute.
 */
public class DateData {
    @Getter
    private long dayStartEpoch;
    @Getter
    private long dayEndEpoch;
    private final ZonedDateTime timeNow;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a DateData object with a specified date and a range of days to define the end epoch.
     *
     * @param date     The starting LocalDateTime to initialize dayStartEpoch.
     * @param daysSpan The number of days after the start date to calculate dayEndEpoch.
     */
    public DateData(LocalDateTime date, int daysSpan) {
        timeNow = date.atZone(ZoneId.systemDefault());
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(daysSpan).toEpochSecond();
    }

    /**
     * Constructs a DateData object with a specified date, defaulting to a 1-day span for dayEndEpoch.
     *
     * @param date The LocalDateTime used to initialize dayStartEpoch.
     */
    public DateData(LocalDateTime date) {
        timeNow = date.atZone(ZoneId.systemDefault());
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        dayStartEpoch = startOfDay.toEpochSecond();
        dayEndEpoch = startOfDay.plusDays(1).toEpochSecond();
    }

    /**
     * Calculates the epoch time (in seconds) for the nearest hour based on the specified ZonedDateTime.
     * Rounds up to the next hour if the current minute is 30 or greater.
     *
     * @param now The ZonedDateTime to round to the nearest hour.
     * @return The epoch time in seconds for the nearest hour.
     */
    public static long getNearestHourEpoch(ZonedDateTime now) {
        if (now.getMinute() >= 30) now = now.plusHours(1);
        return now.truncatedTo(ChronoUnit.HOURS).toEpochSecond();
    }

    /**
     * Calculates the epoch time (in seconds) for the nearest minute based on the specified ZonedDateTime.
     * Rounds up to the next minute if the current second is 30 or greater.
     *
     * @param now The ZonedDateTime to round to the nearest minute.
     * @return The epoch time in seconds for the nearest minute.
     */
    public static long getNearestMinuteEpoch(ZonedDateTime now) {
        if (now.getMinute() >= 30) now = now.plusMinutes(1);
        return now.truncatedTo(ChronoUnit.MINUTES).toEpochSecond();
    }

    /**
     * Retrieves the current epoch time (in seconds) based on the timeNow instance variable.
     *
     * @return The current epoch time in seconds.
     */
    public long getCurrentEpoch() {
        return timeNow.toEpochSecond();
    }

    /**
     * Returns a string representation of the date and time, formatted as "yyyy-MM-dd HH:mm:ss".
     *
     * @return A string representing the current date and time.
     */
    @Override
    public String toString() {
        return timeNow.format(formatter);
    }
}
