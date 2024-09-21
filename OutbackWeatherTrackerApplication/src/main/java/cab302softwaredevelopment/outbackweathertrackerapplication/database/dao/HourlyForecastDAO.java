package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.List;

public class HourlyForecastDAO extends ForecastDAO<HourlyForecast> {

  public HourlyForecastDAO() {
    super(HourlyForecast.class);
  }

  public static class HourlyForecastQuery extends ForecastQuery<HourlyForecast> {
    public HourlyForecastQuery() {
      super(HourlyForecast.class);
    }
  }

  public List<HourlyForecast> getAll() {
    return new HourlyForecastQuery()
        .getResults();
  }

  public List<HourlyForecast> getByLocationId(int location_id) {
    return new HourlyForecastQuery()
        .whereLocationId(location_id)
        .getResults();
  }

  public List<HourlyForecast> getByLocation(Location location) {
    return new HourlyForecastQuery()
        .whereLocation(location)
        .getResults();
  }

  public HourlyForecast getById(int id) {
    return (HourlyForecast) new HourlyForecastQuery()
        .whereId(id)
        .getSingleResult();
  }

}
