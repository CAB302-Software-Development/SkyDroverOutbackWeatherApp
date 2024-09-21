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

    public LocationQuery whereId(int id) {
      criteria.where(builder.equal(root.get("id"), id));
      return this;
    }

    public LocationQuery whereAccountId(int account_id) {
      criteria.where(builder.equal(root.get("account").get("id"), account_id));
      return this;
    }

    public LocationQuery whereAccount(Account account) {
      criteria.where(builder.equal(root.get("account"), account));
      return this;
    }

    public LocationQuery whereName(String name) {
      criteria.where(builder.equal(root.get("name"), name));
      return this;
    }

    public LocationQuery whereNameLike(String name) {
      criteria.where(builder.like(root.get("name"), "%"+ name + "%"));
      return this;
    }

    public LocationQuery whereLongitude(Double longitude) {
      criteria.where(builder.equal(root.get("longitude"), longitude));
      return this;
    }

    public LocationQuery whereLongitudeGE(Double longitude) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("longitude"), longitude));
      return this;
    }

    public LocationQuery whereLongitudeLE(Double longitude) {
      criteria.where(builder.lessThanOrEqualTo(root.get("longitude"), longitude));
      return this;
    }

    public LocationQuery whereLatitude(Double latitude) {
      criteria.where(builder.equal(root.get("latitude"), latitude));
      return this;
    }

    public LocationQuery whereLatitudeGE(Double latitude) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("latitude"), latitude));
      return this;
    }

    public LocationQuery whereLatitudeLE(Double latitude) {
      criteria.where(builder.lessThanOrEqualTo(root.get("latitude"), latitude));
      return this;
    }

    public LocationQuery whereElevation(Double elevation) {
      criteria.where(builder.equal(root.get("elevation"), elevation));
      return this;
    }

    public LocationQuery whereElevationGE(Double elevation) {
      criteria.where(builder.greaterThanOrEqualTo(root.get("elevation"), elevation));
      return this;
    }

    public LocationQuery whereElevationLE(Double elevation) {
      criteria.where(builder.lessThanOrEqualTo(root.get("elevation"), elevation));
      return this;
    }

    public List<Location> getResults() {
      Session session = DatabaseConnection.getSession();
      List<Location> locations = session.createQuery(criteria).getResultList();
      session.close();
      return locations;
    }

    public Location getSingleResult() {
      Session session = DatabaseConnection.getSession();
      Location location = session.createQuery(criteria).getResultStream().findFirst().orElse(null);
      session.close();
      return location;
    }
  }

  public List<Location> getAll() {
    return new LocationQuery()
        .getResults();
  }

  public List<Location> getByAccountId(int account_id) {
    return new LocationQuery()
        .whereAccountId(account_id)
        .getResults();
  }

  public List<Location> getByAccount(Account account) {
    return new LocationQuery()
        .whereAccount(account)
        .getResults();
  }

  public Location getById(int id) {
    return new LocationQuery()
        .whereId(id)
        .getSingleResult();
  }
}
