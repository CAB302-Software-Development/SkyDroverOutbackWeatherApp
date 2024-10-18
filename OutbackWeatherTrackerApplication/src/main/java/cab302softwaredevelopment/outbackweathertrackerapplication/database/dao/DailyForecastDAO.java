package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.List;

/**
 * A Data Access Object for the DailyForecast entity.
 */
public class DailyForecastDAO extends ForecastDAO<DailyForecast> {

  /**
   * Default constructor for the DailyForecastDAO class.
   */
  public DailyForecastDAO() {
    // The forecast DAO needs to know what type of forecast it is dealing with
    super(DailyForecast.class);
  }

  /**
   * A query class for DailyForecast objects.
   */
  public static class DailyForecastQuery extends ForecastQuery<DailyForecast,DailyForecastQuery> {
    /**
     * Default constructor for the DailyForecastQuery class.
     */
    public DailyForecastQuery() {
      super(DailyForecast.class, DailyForecastQuery.class);
    }
    /**
     * Sets the location ID to filter by.
     *
     * @param location_id The location ID to filter by.
     * @return The DailyForecastQuery object.
     */
    @Override
    public DailyForecastQuery self() {
      return this;
    }
  }

  /**
   * Retrieves all DailyForecast objects from the database.
   * @return A list of all DailyForecast objects in the database.
   */
  @Deprecated
  public List<DailyForecast> getAll() {
    return new DailyForecastQuery()
        .getResults();
  }

  /**
   * Retrieves all DailyForecast objects from the database that are associated with a specific location.
   *
   * @param location_id The ID of the location to retrieve DailyForecast objects for.
   * @return A list of all DailyForecast objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<DailyForecast> getByLocationId(long location_id) {
    return new DailyForecastQuery()
        .whereLocationId(location_id)
        .getResults();
  }

  /**
   * Retrieves all DailyForecast objects from the database that are associated with a specific location.
   *
   * @param location The location to retrieve DailyForecast objects for.
   * @return A list of all DailyForecast objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<DailyForecast> getByLocation(Location location) {
    return new DailyForecastQuery()
        .whereLocation(location)
        .getResults();
  }

  /**
   * Retrieves a DailyForecast object from the database by its ID.
   *
   * @param id The ID of the DailyForecast to retrieve.
   * @return The DailyForecast object with the specified ID or null if no DailyForecast is found.
   */
  @Deprecated
  public DailyForecast getById(int id) {
    return (DailyForecast) new DailyForecastQuery()
        .whereId(id)
        .getSingleResult();
  }

}
