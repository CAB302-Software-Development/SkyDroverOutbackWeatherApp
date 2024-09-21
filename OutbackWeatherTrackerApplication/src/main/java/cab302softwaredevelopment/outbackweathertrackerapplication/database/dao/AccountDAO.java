package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;

public class AccountDAO {

  public AccountDAO() {
  }

  public void insert(Account account) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.save(account);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void update(Account account) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.update(account);
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
      Account account = session.get(Account.class, id);
      session.delete(account);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public void delete(Account account) {
    Session session = DatabaseConnection.getSession();
    try {
      session.beginTransaction();
      session.delete(account);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

  public static class AccountQuery {
    CriteriaQuery<Account> criteria;
    CriteriaBuilder builder;
    Root<Account> root;

    public AccountQuery() {
      Session session = DatabaseConnection.getSession();
      builder = session.getCriteriaBuilder();
      criteria = builder.createQuery(Account.class);
      root = criteria.from(Account.class);
      criteria.select(root);
    }

    public AccountQuery whereId(int id) {
      criteria.where(builder.equal(root.get("id"), id));
      return this;
    }

    public AccountQuery whereEmail(String email) {
      criteria.where(builder.equal(root.get("email"), email));
      return this;
    }

    public AccountQuery whereEmailLike(String email) {
      criteria.where(builder.like(root.get("email"), "%" + email + "%"));
      return this;
    }

    public AccountQuery wherePreferCelsius(boolean preferCelsius) {
      criteria.where(builder.equal(root.get("preferCelsius"), preferCelsius));
      return this;
    }

    public List<Account> getResults() {
      Session session = DatabaseConnection.getSession();
      List<Account> accounts = session.createQuery(criteria).getResultList();
      session.close();
      return accounts;
    }

    public Account getSingleResult() {
      Session session = DatabaseConnection.getSession();
      Account account = session.createQuery(criteria).getResultStream().findFirst().orElse(null);
      session.close();
      return account;
    }
  }

  @Deprecated
  public List<Account> getAll() {
    return new AccountQuery()
        .getResults();
  }

  @Deprecated
  public Account getById(int id) {
    return new AccountQuery()
        .whereId(id)
        .getSingleResult();
  }

  @Deprecated
  public Account getByEmail(String email) {
    return new AccountQuery()
        .whereEmail(email)
        .getSingleResult();
  }

}
