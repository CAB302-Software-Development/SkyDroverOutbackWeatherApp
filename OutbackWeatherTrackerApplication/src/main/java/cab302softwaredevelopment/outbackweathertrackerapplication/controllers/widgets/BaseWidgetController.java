package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public abstract class BaseWidgetController implements IConfigurableWidget {
    Location location;

    @Override
    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();
        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();
        updateWidget();
    }

    public HourlyForecast getLatestHourlyForecast() {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());
        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereLocation(location)
                .whereTimestamp(nowEpoch)
                .getSingleResult();
        return currentForecast;
    }

    public DailyForecast getTodayForecast() {
        DateData now = new DateData(LocalDate.now());
        DailyForecast todayForecast = new DailyForecastDAO.DailyForecastQuery()
                .whereTimestamp((int) now.getDayStartEpoch())
                .getSingleResult();
        return todayForecast;
    }

    public abstract void updateWidget();
}
