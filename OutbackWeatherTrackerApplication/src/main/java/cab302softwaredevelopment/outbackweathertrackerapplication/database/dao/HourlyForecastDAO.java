package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;

public class HourlyForecastDAO {

  public HourlyForecastDAO() {
  }

  public void insert(HourlyForecast hourlyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.save(hourlyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void update(HourlyForecast hourlyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.update(hourlyForecast);
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
      HourlyForecast hourlyForecast = session.get(HourlyForecast.class, id);
      session.delete(hourlyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void delete(HourlyForecast hourlyForecast) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.delete(hourlyForecast);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public List<HourlyForecast> getAll() {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<HourlyForecast> criteria = builder.createQuery(HourlyForecast.class);
    criteria.from(HourlyForecast.class);

    List<HourlyForecast> hourlyForecasts = session.createQuery(criteria).getResultList();
    session.close();
    return hourlyForecasts;
  }

  public List<HourlyForecast> getByLocationId(int location_id) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<HourlyForecast> criteria = builder.createQuery(HourlyForecast.class);
    Root<HourlyForecast> root = criteria.from(HourlyForecast.class);
    criteria.select(root);

    // Apply the location filter
    criteria.where(builder.equal(root.get("location").get("id"), location_id));

    List<HourlyForecast> hourlyForecasts = session.createQuery(criteria).getResultList();
    session.close();
    return hourlyForecasts;
  }
  public List<HourlyForecast> getByLocation(Location location) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<HourlyForecast> criteria = builder.createQuery(HourlyForecast.class);
    Root<HourlyForecast> root = criteria.from(HourlyForecast.class);
    criteria.select(root);

    // Apply the location filter
    criteria.where(builder.equal(root.get("location"), location));

    List<HourlyForecast> hourlyForecasts = session.createQuery(criteria).getResultList();
    session.close();

    return hourlyForecasts;
  }

  public HourlyForecast getById(int id) {
    Session session = DatabaseConnection.getSession();
    HourlyForecast hourlyForecast = session.get(HourlyForecast.class, id);
    session.close();
    return hourlyForecast;
  }

}
