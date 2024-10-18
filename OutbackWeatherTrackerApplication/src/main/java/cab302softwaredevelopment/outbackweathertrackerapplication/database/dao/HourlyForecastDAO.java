package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.List;

/**
 * A Data Access Object for the HourlyForecast entity.
 */
public class HourlyForecastDAO extends ForecastDAO<HourlyForecast> {

  /**
   * Constructor for the HourlyForecastDAO.
   */
  public HourlyForecastDAO() {
    // The forecast DAO needs to know what type of forecast it is dealing with
    super(HourlyForecast.class);
  }

  /**
   * A query builder class for the HourlyForecast entity.
   */
  public static class HourlyForecastQuery extends ForecastQuery<HourlyForecast,HourlyForecastQuery> {

    /**
     * Constructor for the HourlyForecastQuery.
     */
    public HourlyForecastQuery() {
      super(HourlyForecast.class, HourlyForecastQuery.class);
    }

    /**
     * Adds a where clause to the query to filter by location ID.
     *
     * @param location_id The ID of the location to filter by.
     * @return The query builder.
     */
    @Override
    public HourlyForecastQuery self() {
      return this;
    }
  }

  /**
   * Retrieves all HourlyForecast objects from the database.
   *
   * @return A list of all HourlyForecast objects in the database.
   */
  @Deprecated
  public List<HourlyForecast> getAll() {
    return new HourlyForecastQuery()
        .getResults();
  }

  /**
   * Retrieves all HourlyForecast objects from the database that are associated with a specific location.
   *
   * @param location_id The ID of the location to retrieve HourlyForecast objects for.
   * @return A list of all HourlyForecast objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<HourlyForecast> getByLocationId(long location_id) {
    return new HourlyForecastQuery()
        .whereLocationId(location_id)
        .getResults();
  }

  /**
   * Retrieves all HourlyForecast objects from the database that are associated with a specific location.
   *
   * @param location The location to retrieve HourlyForecast objects for.
   * @return A list of all HourlyForecast objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<HourlyForecast> getByLocation(Location location) {
    return new HourlyForecastQuery()
        .whereLocation(location)
        .getResults();
  }

  /**
   * Retrieves a HourlyForecast object from the database by its ID.
   *
   * @param id The ID of the HourlyForecast to retrieve.
   * @return The HourlyForecast object with the specified ID or null if no HourlyForecast is found.
   */
  @Deprecated
  public HourlyForecast getById(int id) {
    return (HourlyForecast) new HourlyForecastQuery()
        .whereId(id)
        .getSingleResult();
  }

}
