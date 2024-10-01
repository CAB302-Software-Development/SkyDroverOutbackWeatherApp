package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
/**
 * A Data Access Object for the Location entity.
 */
public class LocationDAO {

  public LocationDAO() {
  }

  /**
   * Inserts a new Location into the database.
   *
   * @param location the Location object to be inserted
   *                <p>
   *                This method begins a transaction and attempts to save the provided Location
   *                object to the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void insert(Location location) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.save(location);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  /**
   * Updates an existing Location in the database.
   *
   * @param location the Location object to be updated
   *                <p>
   *                This method begins a transaction and attempts to update the provided Location
   *                object in the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void update(Location location) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.update(location);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  /**
   * Deletes an Location from the database by its ID.
   *
   * @param id The ID of the Location to be deleted
   *           <p>
   *           This method begins a transaction and attempts to delete the Location object with the
   *           specified ID from the database. If the operation is successful, the transaction is
   *           committed. If an exception occurs during the operation, the transaction is rolled
   *           back and the exception stack trace is printed.
   */
  public void delete(long id) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      Location location = session.get(Location.class, id);
      session.delete(location);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  /**
   * Deletes an Location from the database
   *
   * @param location The Location to be deleted
   *                <p>
   *                This method begins a transaction and attempts to delete the provided Location
   *                object from the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void delete(Location location) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.delete(location);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  /**
   * A query builder class for the Location entity.
   */
  public static class LocationQuery {
    CriteriaQuery<Location> criteria;
    CriteriaBuilder builder;
    Root<Location> root;

    public LocationQuery() {
      Session session = DatabaseConnection.getSession();
      builder = session.getCriteriaBuilder();
      criteria = builder.createQuery(Location.class);
      root = criteria.from(Location.class);
      criteria.select(root);
    }

    /**
     * Adds an id filter to the query.
     *
     * @param id The ID to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereId(long id) {
      criteria.where(builder.equal(root.get("id"), id));
      return this;
    }

    /**
     * Adds an associated account_id filter to the query.
     *
     * @param account_id The associated account ID to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereAccountId(int account_id) {
      criteria.where(builder.equal(root.get("account").get("id"), account_id));
      return this;
    }

    /**
     * Adds an associated account filter to the query.
     *
     * @param account The associated account to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereAccount(Account account) {
      criteria.where(builder.equal(root.get("account"), account));
      return this;
    }

    /**
     * Adds a name filter to the query.
     *
     * @param name The name to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereName(String name) {
      criteria.where(builder.equal(root.get("name"), name));
      return this;
    }

    /**
     * Adds a name contains filter to the query.
     *
     * @param name The string to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereNameLike(String name) {
      criteria.where(builder.like(root.get("name"), "%"+ name + "%"));
      return this;
    }

    /**
     * Adds a longitude filter to the query.
     *
     * @param longitude The longitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLongitude(Double longitude) {
      criteria.where(builder.equal(root.get("longitude"), longitude));
      return this;
    }

    /**
     * Adds a longitude greater than or equal to filter to the query.
     *
     * @param longitude The minimum longitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLongitudeGE(Double longitude) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("longitude"), longitude));
      return this;
    }

    /**
     * Adds a longitude less than or equal to filter to the query.
     *
     * @param longitude The maximum longitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLongitudeLE(Double longitude) {
      criteria.where(builder.lessThanOrEqualTo(root.get("longitude"), longitude));
      return this;
    }

    /**
     * Adds a latitude filter to the query.
     *
     * @param latitude The latitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLatitude(Double latitude) {
      criteria.where(builder.equal(root.get("latitude"), latitude));
      return this;
    }

    /**
     * Adds a latitude greater than or equal to filter to the query.
     *
     * @param latitude The minimum latitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLatitudeGE(Double latitude) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("latitude"), latitude));
      return this;
    }

    /**
     * Adds a latitude less than or equal to filter to the query.
     *
     * @param latitude The maximum latitude to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereLatitudeLE(Double latitude) {
      criteria.where(builder.lessThanOrEqualTo(root.get("latitude"), latitude));
      return this;
    }

    /**
     * Adds an elevation filter to the query.
     *
     * @param elevation The elevation to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereElevation(Double elevation) {
      criteria.where(builder.equal(root.get("elevation"), elevation));
      return this;
    }

    /**
     * Adds an elevation greater than or equal to filter to the query.
     *
     * @param elevation The minimum elevation to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereElevationGE(Double elevation) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("elevation"), elevation));
      return this;
    }

    /**
     * Adds an elevation less than or equal to filter to the query.
     *
     * @param elevation The maximum elevation to filter by
     * @return This LocationQuery object
     */
    public LocationQuery whereElevationLE(Double elevation) {
      criteria.where(builder.lessThanOrEqualTo(root.get("elevation"), elevation));
      return this;
    }

    /**
     * Executes the current filters and returns the results.
     *
     * @return A list of Location objects that match the provided filters
     */
    public List<Location> getResults() {
      Session session = DatabaseConnection.getSession();
      List<Location> locations = session.createQuery(criteria).getResultList();
      session.close();
      return locations;
    }

    /**
     * Executes the current filters and returns the first result.
     *
     * @return The first Location object that matches the provided filters or null if no results are found
     */
    public Location getSingleResult() {
      Session session = DatabaseConnection.getSession();
      Location location = session.createQuery(criteria).getResultStream().findFirst().orElse(null);
      session.close();
      return location;
    }
  }

  /**
   * Retrieves all Location objects from the database.
   *
   * @return A list of all Location objects in the database
   */
  @Deprecated
  public List<Location> getAll() {
    return new LocationQuery()
        .getResults();
  }

  /**
   * Retrieves all Location objects from the database that are associated with a specific location.
   *
   * @param account_id The ID of the location to retrieve Location objects for.
   * @return A list of all Location objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<Location> getByAccountId(int account_id) {
    return new LocationQuery()
        .whereAccountId(account_id)
        .getResults();
  }

  /**
   * Retrieves all Location objects from the database that are associated with a specific location.
   *
   * @param account The location to retrieve Location objects for.
   * @return A list of all Location objects in the database that are associated with the specified location.
   */
  @Deprecated
  public List<Location> getByAccount(Account account) {
    return new LocationQuery()
        .whereAccount(account)
        .getResults();
  }

  /**
   * Retrieves a Location object from the database by its ID.
   *
   * @param id The ID of the Location to retrieve.
   * @return The Location object with the specified ID or null if no Location is found.
   */
  @Deprecated
  public Location getById(long id) {
    return new LocationQuery()
        .whereId(id)
        .getSingleResult();
  }
}
