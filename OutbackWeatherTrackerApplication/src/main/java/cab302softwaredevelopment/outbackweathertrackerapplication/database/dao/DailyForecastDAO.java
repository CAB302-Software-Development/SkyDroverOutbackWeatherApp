package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import org.hibernate.Session;

public class DailyForecastDAO {

  public DailyForecastDAO() {
  }

  public void insert(DailyForecast dailyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.save(dailyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void update(DailyForecast dailyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.update(dailyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }
  public void delete(int id) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      DailyForecast dailyForecast = session.get(DailyForecast.class, id);
      session.delete(dailyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void delete(DailyForecast dailyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.delete(dailyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public List<DailyForecast> getAll() {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<DailyForecast> criteria = builder.createQuery(DailyForecast.class);
    criteria.from(DailyForecast.class);

    List<DailyForecast> dailyForecasts = session.createQuery(criteria).getResultList();
    session.close();
    return dailyForecasts;
  }

  public List<DailyForecast> getByLocationId(int location_id) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<DailyForecast> criteria = builder.createQuery(DailyForecast.class);
    Root<DailyForecast> root = criteria.from(DailyForecast.class);
    criteria.select(root);

    // Apply the location filter
    criteria.where(builder.equal(root.get("location").get("id"), location_id));

    List<DailyForecast> dailyForecasts = session.createQuery(criteria).getResultList();
    session.close();
    return dailyForecasts;
  }

  public List<DailyForecast> getByLocation(Location location) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<DailyForecast> criteria = builder.createQuery(DailyForecast.class);
    Root<DailyForecast> root = criteria.from(DailyForecast.class);
    criteria.select(root);

    // Apply the location filter
    criteria.where(builder.equal(root.get("location"), location));

    List<DailyForecast> dailyForecasts = session.createQuery(criteria).getResultList();
    session.close();
    return dailyForecasts;
  }

  public DailyForecast getById(int id) {
    Session session = DatabaseConnection.getSession();
    DailyForecast dailyForecast = session.get(DailyForecast.class, id);
    session.close();
    return dailyForecast;
  }

}
