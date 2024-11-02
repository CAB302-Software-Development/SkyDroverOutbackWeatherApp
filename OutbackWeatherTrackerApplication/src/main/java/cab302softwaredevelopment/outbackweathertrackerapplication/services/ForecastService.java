package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service class responsible for managing weather forecast data.
 * Provides methods to retrieve, update, and query forecast data for daily and hourly forecasts
 * based on user locations and specified date ranges.
 */
public class ForecastService {
    /**
     * Singleton instance of ForecastService for global access.
     */
    @Getter
    private static ForecastService instance = new ForecastService();

    /**
     * Retrieves the latest hourly forecast for a given location.
     * Uses cached data if the application is offline; otherwise, fetches live data.
     *
     * @param location The location for which to retrieve the forecast.
     * @return The latest HourlyForecast for the specified location.
     */
    public HourlyForecast getLatestHourlyForecast(Location location) {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());

        if (ConnectionService.getInstance().isOffline()) {
            HourlyForecast latest = new HourlyForecastDAO.HourlyForecastQuery()
                    .addOrderDesc("timestamp")
                    .getSingleResult();
            if (latest != null && nowEpoch > latest.getTimestamp()) {
                return latest;
            }
        }

        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereLocation(location)
                .whereTimestamp(nowEpoch)
                .getSingleResult();
        return currentForecast;
    }

    /**
     * Retrieves today's daily forecast for a given location.
     * Uses cached data if the application is offline; otherwise, fetches live data.
     *
     * @param location The location for which to retrieve the forecast.
     * @return The DailyForecast for today at the specified location.
     */
    public DailyForecast getTodayForecast(Location location) {
        int nowEpoch = (int) (new DateData(LocalDateTime.now())).getDayStartEpoch();

        if (ConnectionService.getInstance().isOffline()) {
            DailyForecast latest = new DailyForecastDAO.DailyForecastQuery()
                    .addOrderDesc("timestamp")
                    .getSingleResult();
            if (nowEpoch > latest.getTimestamp()) {
                return latest;
            }
        }

        DailyForecast todayForecast = new DailyForecastDAO.DailyForecastQuery()
                .whereTimestamp(nowEpoch)
                .getSingleResult();
        return todayForecast;
    }

    /**
     * Updates daily and hourly forecasts for all locations associated with a given user account.
     *
     * @param account The account for which to update forecasts.
     * @param futureDays The number of future days to include in the forecast.
     * @param pastDays The number of past days to include in the forecast.
     * @return True if the update was successful; false otherwise.
     */
    public boolean updateForecastsForUser(Account account, int futureDays, int pastDays) {
        try {
            Sdk sdk = new Sdk();
            for (Location location : LocationService.getInstance().getCurrentUserLocations()) {
                sdk.updateDailyForecast(location, futureDays, pastDays);
                sdk.updateHourlyForecast(location, futureDays, pastDays);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates daily and hourly forecasts for the current user's locations.
     *
     * @param futureDays The number of future days to include in the forecast.
     * @param pastDays The number of past days to include in the forecast.
     * @return True if the update was successful; false otherwise.
     */
    public boolean updateForecastsForCurrentUser(int futureDays, int pastDays) {
        return updateForecastsForUser(UserService.getInstance().getCurrentAccount(), futureDays, pastDays);
    }

    /**
     * Updates both daily and hourly forecasts for a specific location.
     *
     * @param location The location for which to update forecasts.
     * @param futureDays The number of future days to include in the forecast.
     * @param pastDays The number of past days to include in the forecast.
     * @return True if the update was successful; false otherwise.
     */
    public boolean updateForecastsForLocation(Location location, int futureDays, int pastDays) {
        try {
            Sdk sdk = new Sdk();
            sdk.updateDailyForecast(location, futureDays, pastDays);
            sdk.updateHourlyForecast(location, futureDays, pastDays);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves daily forecasts within a specified time range for a given location.
     *
     * @param location The location for which to retrieve daily forecasts.
     * @param dateRange The date range specifying the start and end of the forecast period.
     * @return A list of DailyForecasts for the specified location within the given date range.
     */
    public List<DailyForecast> getDailyForecastByTimeRange(Location location, DateData dateRange) {
        List<DailyForecast> results = (new DailyForecastDAO.DailyForecastQuery())
                .whereLocationId(location.getId())
                .whereTimestampGE((int) dateRange.getDayStartEpoch())
                .whereTimestampLE((int) dateRange.getDayEndEpoch())
                .getResults();
        return results;
    }
}
