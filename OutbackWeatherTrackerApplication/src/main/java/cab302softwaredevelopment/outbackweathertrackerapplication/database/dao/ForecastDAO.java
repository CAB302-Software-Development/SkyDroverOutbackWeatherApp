package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 * An abstract Data Access Object for forecast entities.
 * The class of the model that this DAO is for
 * This is needed to create the correct type of query object
 */
public abstract class ForecastDAO<T> {

  private final Class<T> forecastClass;

  /**
   * Creates a new ForecastDAO for the specified model class.
   *
   * @param modelClass The class of the model that this DAO is for.
   */
  public ForecastDAO(Class<T> modelClass) {
    this.forecastClass = modelClass;
  }

  /**
   * Inserts a new Forecast into the database.
   *
   * @param forecast The Forecast object to be inserted
   *                <p>
   *                This method begins a transaction and attempts to save the provided Forecast
   *                object to the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void insert(T forecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.save(forecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }

  /**
   * Updates an existing Forecast in the database.
   *
   * @param forecast the Forecast object to be updated
   *                <p>
   *                This method begins a transaction and attempts to update the provided Forecast
   *                object in the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void update(T forecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.update(forecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }

  /**
   * Deletes an Forecast from the database by its ID.
   *
   * @param id The ID of the Forecast to be deleted
   *                <p>
   *                This method begins a transaction and attempts to delete the Forecast object with the
   *                specified ID from the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is rolled
   *                back and the exception stack trace is printed.
   */
  public void delete(int id) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      T forecast = session.get(forecastClass, id);
      session.delete(forecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }

  /**
   * Deletes an Forecast from the database
   *
   * @param forecast The Forecast to be deleted
   *                <p>
   *                This method begins a transaction and attempts to delete the provided Forecast
   *                object from the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
  public void delete(T forecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.delete(forecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }
}

/**
 * An abstract query builder class for forecast entities
 * supports common query operations for forecast entities
 */
abstract class ForecastQuery<T,K> {
  CriteriaQuery<T> criteria;
  CriteriaBuilder builder;
  List<Predicate> predicates;
  Root<T> root;
  Class<K> childClass;

  /**
   * Creates a new ForecastQuery for the specified model class.
   *
   * @param modelClass The class of the model that this query is for.
   * @param childClass The class of the child query builder class that this query is for.
   */
  public ForecastQuery(Class<T> modelClass, Object childClass) {
    Session session = DatabaseConnection.getSession();
    builder = session.getCriteriaBuilder();
    criteria = builder.createQuery(modelClass);
    predicates = new ArrayList<>();
    root = criteria.from(modelClass);
    criteria.select(root);
    this.childClass = (Class<K>) childClass.getClass();
  }


  /**
   * Returns the query object itself.
   * Needed for the ForecastQuery to return the correct type of query object.
   * @return The query object itself
   *
   */
  public abstract K self();

  /**
   * Adds a field filter to the query.
   *
   * @param field The field to filter by
   * @param value The value to filter for
   */
  public void whereField(String field, Object value) {
    predicates.add(builder.equal(root.get(field), value));
  }

  /**
   * Adds a field filter less than or equal to filter to the query.
   *
   * @param <Y>   The type of the field and value being compared.
   * @param field The field to filter by
   * @param value The largest value to filter for
   */
  public <Y extends Comparable<? super Y>> void whereFieldLE(String field, Y value) {
    predicates.add(builder.lessThanOrEqualTo(root.get(field), value));
  }

  /**
   * Adds a field filter greater than or equal to filter to the query.
   *
   * @param field The field to filter by
   * @param value The smallest value to filter for
   */
  public <Y extends Comparable<? super Y>> void whereFieldGE(String field, Y value) {
    predicates.add(builder.greaterThanOrEqualTo(root.get(field), value));
  }

  /**
   * Adds an associated location_id filter to the query.
   *
   * @param location_id The associated location ID to filter by
   * @return This ForecastQuery object
   */
  public K whereLocationId(long location_id) {
    predicates.add(builder.equal(root.get("location").get("id"), location_id));
    return self();
  }

  /**
   * Adds an associated location filter to the query.
   *
   * @param location The associated location to filter by
   * @return This ForecastQuery object
   */
  public K whereLocation(Location location) {
    predicates.add(builder.equal(root.get("location"), location));
    return self();
  }

  /**
   * Adds an id filter to the query.
   *
   * @param id The ID to filter by
   * @return This ForecastQuery object
   */
  public K whereId(int id) {
    whereField("id", id);
    return self();
  }

  /**
   * Adds a timestamp filter to the query.
   *
   * @param timestamp The timestamp to filter by
   * @return This ForecastQuery object
   */
  public K whereTimestamp(int timestamp) {
    whereField("timestamp", timestamp);
    return self();
  }

  /**
   * Adds a timestamp less than or equal filter to the query.
   *
   * @param timestamp The maximum timestamp to filter by
   * @return This ForecastQuery object
   */
  public K whereTimestampLE(int timestamp) {
    whereFieldLE("timestamp", timestamp);
    return self();
  }

  /**
   * Adds a timestamp greater than or equal filter to the query.
   *
   * @param timestamp The minimum timestamp to filter by
   * @return This ForecastQuery object
   */
  public K whereTimestampGE(int timestamp) {
    whereFieldGE("timestamp", timestamp);
    return self();
  }

  /**
   * Adds an order by clause to the query.
   * The results will be ordered in ascending order by the specified field.
   *
   * @param field The field to order by
   * @return This ForecastQuery object
   */
  public K addOrderAsc(String field) {
    criteria.orderBy(builder.asc(root.get(field)));
    return self();
  }

  /**
   * Adds an order by clause to the query.
   * The results will be ordered in descending order by the specified field.
   *
   * @param field The field to order by
   * @return This ForecastQuery object
   */
  public K addOrderDesc(String field) {
    criteria.orderBy(builder.desc(root.get(field)));
    return self();
  }

  /**
   * Executes the current filters and returns the results.
   *
   * @return A list of Forecast objects that match the provided filters
   */
  public List<T> getResults() {
    Session session = DatabaseConnection.getSession();
    Predicate[] predicateArray = new Predicate[predicates.size()];
    predicates.toArray(predicateArray);
    List<T> forecasts = session.createQuery(criteria.where(predicateArray)).getResultList();
    session.close();
    return forecasts;
  }

  /**
   * Executes the current filters and returns the first result.
   *
   * @return The first Forecast object that matches the provided filters or null if no results are found
   */
  public T getSingleResult() {
    Session session = DatabaseConnection.getSession();
    Predicate[] predicateArray = new Predicate[predicates.size()];
    predicates.toArray(predicateArray);
    T forecast = session.createQuery(criteria.where(predicateArray)).getResultStream().findFirst().orElse(null);
    session.close();
    return forecast;
  }
}
