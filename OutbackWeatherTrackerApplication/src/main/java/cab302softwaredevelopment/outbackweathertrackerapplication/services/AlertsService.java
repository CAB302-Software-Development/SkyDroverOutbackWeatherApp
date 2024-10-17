package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import lombok.Getter;

public class AlertsService {
    @Getter
    private static AlertsService instance = new AlertsService();

    public void addAlertPreference(String name, DailyForecastDAO.DailyForecastQuery query) {

    }
    public void addAlertPreference(String name, HourlyForecastDAO.HourlyForecastQuery query) {

    }
}
