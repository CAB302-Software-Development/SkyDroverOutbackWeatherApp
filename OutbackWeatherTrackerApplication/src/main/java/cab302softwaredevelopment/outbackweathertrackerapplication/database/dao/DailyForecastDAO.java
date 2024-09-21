package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.List;

public class DailyForecastDAO extends ForecastDAO<DailyForecast> {

  public DailyForecastDAO() {
    super(DailyForecast.class);
  }

  public static class DailyForecastQuery extends ForecastQuery<DailyForecast> {
    public DailyForecastQuery() {
      super(DailyForecast.class);
    }
  }

  @Deprecated
  public List<DailyForecast> getAll() {
    return new DailyForecastQuery()
        .getResults();
  }

  @Deprecated
  public List<DailyForecast> getByLocationId(int location_id) {
    return new DailyForecastQuery()
        .whereLocationId(location_id)
        .getResults();
  }

  @Deprecated
  public List<DailyForecast> getByLocation(Location location) {
    return new DailyForecastQuery()
        .whereLocation(location)
        .getResults();
  }

  @Deprecated
  public DailyForecast getById(int id) {
    return (DailyForecast) new DailyForecastQuery()
        .whereId(id)
        .getSingleResult();
  }

}
