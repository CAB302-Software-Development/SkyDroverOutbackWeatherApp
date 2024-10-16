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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class ForecastService {

    public static HourlyForecast getLatestHourlyForecast(Location location) {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());
        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereLocation(location)
                .whereTimestamp(nowEpoch)
                .getSingleResult();
        return currentForecast;
    }

    public static DailyForecast getTodayForecast(Location location) {
        DateData now = new DateData(LocalDate.now());
        DailyForecast todayForecast = new DailyForecastDAO.DailyForecastQuery()
                .whereTimestamp((int) now.getDayStartEpoch())
                .getSingleResult();
        return todayForecast;
    }

    public static void updateForecastsForUser(Account account, int futureDays, int pastDays) {
        try {
            Sdk sdk = new Sdk();
            List<Location> locations = (new LocationDAO.LocationQuery())
                    .whereAccount(LoginState.getCurrentAccount())
                    .getResults();
            for (Location location : locations) {
                sdk.updateDailyForecast(location, futureDays, pastDays);
                sdk.updateHourlyForecast(location, futureDays, pastDays);
            }
            if (LoginState.isOffline()) LoginState.setOffline(false);
        } catch (Exception e) {
            e.printStackTrace();
            if (!LoginState.isOffline()) LoginState.setOffline(true);
        }

    }

    public static void updateForecastsForCurrentUser(int futureDays, int pastDays) {
            updateForecastsForUser(LoginState.getCurrentAccount(), futureDays, pastDays);
    }
}
