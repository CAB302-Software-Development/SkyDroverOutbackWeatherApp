package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;

public abstract class ForecastDAO<T> {
  private final Class<T> modelClass;

  public ForecastDAO(Class<T> modelClass) {
    this.modelClass = modelClass;
  }

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
  public void delete(int id) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      T forecast = session.get(modelClass, id);
      session.delete(forecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }

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

abstract class ForecastQuery<T> {
  CriteriaQuery<T> criteria;
  CriteriaBuilder builder;
  Root<T> root;


  public ForecastQuery(Class<T> modelClass) {
    Session session = DatabaseConnection.getSession();
    builder = session.getCriteriaBuilder();
    criteria = builder.createQuery(modelClass);
    root = criteria.from(modelClass);
    criteria.select(root);
  }

  public ForecastQuery<T> whereLocationId(int location_id) {
    criteria.where(builder.equal(root.get("location").get("id"), location_id));
    return this;
  }

  public ForecastQuery<T> whereLocation(Location location) {
    criteria.where(builder.equal(root.get("location"), location));
    return this;
  }

  public ForecastQuery<T> whereId(int id) {
    criteria.where(builder.equal(root.get("id"), id));
    return this;
  }

  public ForecastQuery<T> whereTimestamp(int timestamp) {
    criteria.where(builder.equal(root.get("timestamp"), timestamp));
    return this;
  }

  public ForecastQuery<T> whereTimestampGE(int timestamp) {
    criteria.where(builder.greaterThanOrEqualTo(root.get("timestamp"), timestamp));
    return this;
  }

  public ForecastQuery<T> whereTimestampLE(int timestamp) {
    criteria.where(builder.lessThanOrEqualTo(root.get("timestamp"), timestamp));
    return this;
  }

  public ForecastQuery<T> addOrderAsc(String field) {
    criteria.orderBy(builder.asc(root.get(field)));
    return this;
  }

  public ForecastQuery<T> addOrderDesc(String field) {
    criteria.orderBy(builder.desc(root.get(field)));
    return this;
  }

  public List<T> getResults() {
    Session session = DatabaseConnection.getSession();
    List<T> forecasts = session.createQuery(criteria).getResultList();
    session.close();
    return forecasts;
  }

  public T getSingleResult() {
    Session session = DatabaseConnection.getSession();
    T forecast = session.createQuery(criteria).getResultStream().findFirst().orElse(null);
    session.close();
    return forecast;
  }


}
