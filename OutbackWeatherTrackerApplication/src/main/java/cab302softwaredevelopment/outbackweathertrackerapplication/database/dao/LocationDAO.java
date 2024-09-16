package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import org.hibernate.Session;

public class LocationDAO {

  public LocationDAO() {
  }

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

  public void delete(int id) {
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

  public List<Location> getAll() {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
    criteria.from(Location.class);

    List<Location> locations = session.createQuery(criteria).getResultList();
    session.close();
    return locations;
  }

  public List<Location> getByAccountId(int account_id) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
    Root<Location> root = criteria.from(Location.class);
    criteria.select(root);

    // Apply the account filter
    criteria.where(builder.equal(root.get("account").get("id"), account_id));

    List<Location> locations = session.createQuery(criteria).getResultList();
    session.close();
    return locations;
  }

  public List<Location> getByAccount(Account account) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
    Root<Location> root = criteria.from(Location.class);
    criteria.select(root);

    // Apply the account filter
    criteria.where(builder.equal(root.get("account"), account));

    List<Location> locations = session.createQuery(criteria).getResultList();
    session.close();

    return locations;
  }

  public Location getById(int id) {
    Session session = DatabaseConnection.getSession();
    Location location = session.get(Location.class, id);
    session.close();
    return location;
  }

}
