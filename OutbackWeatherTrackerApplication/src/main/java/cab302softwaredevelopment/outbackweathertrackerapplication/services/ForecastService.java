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

public class ForecastService {
    @Getter
    private static ForecastService instance = new ForecastService();

    public HourlyForecast getLatestHourlyForecast(Location location) {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());

        if (ConnectionService.getInstance().isOffline()) {
            HourlyForecast latest = new HourlyForecastDAO.HourlyForecastQuery()
                    .addOrderDesc("timestamp")
                    .getSingleResult();
            if (nowEpoch > latest.getTimestamp()) {
                return latest;
            }
        }

        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereLocation(location)
                .whereTimestamp(nowEpoch)
                .getSingleResult();
        return currentForecast;
    }

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

    public boolean updateForecastsForUser(Account account, int futureDays, int pastDays) {
        try {
            Sdk sdk = new Sdk();
            Account currentAccount = UserService.getInstance().getCurrentAccount();
            if (currentAccount == null) {
                return false;
            }
            List<Location> locations = (new LocationDAO.LocationQuery())
                    .whereAccount(currentAccount)
                    .getResults();
            for (Location location : locations) {
                sdk.updateDailyForecast(location, futureDays, pastDays);
                sdk.updateHourlyForecast(location, futureDays, pastDays);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateForecastsForCurrentUser(int futureDays, int pastDays) {
        return updateForecastsForUser(UserService.getInstance().getCurrentAccount(), futureDays, pastDays);
    }

    public HourlyForecast updateForecastsForLocationGetHourly(Location location, int futureDays, int pastDays) {
        boolean result = updateForecastsForLocation(location, futureDays, pastDays);
        if (result) {
            return getLatestHourlyForecast(location);
        } else {
            return null;
        }
    }
    public DailyForecast updateForecastsForLocationGetDaily(Location location, int futureDays, int pastDays) {
        boolean result = updateForecastsForLocation(location, futureDays, pastDays);
        if (result) {
            return getTodayForecast(location);
        } else {
            return null;
        }
    }
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
}
