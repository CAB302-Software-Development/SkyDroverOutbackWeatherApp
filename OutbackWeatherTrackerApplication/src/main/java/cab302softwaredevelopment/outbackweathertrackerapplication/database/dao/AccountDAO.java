package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
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

  public List<Account> getAll() {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<Account> criteria = builder.createQuery(Account.class);
    criteria.from(Account.class);

    List<Account> accounts = session.createQuery(criteria).getResultList();
    session.close();
    return accounts;
  }

  public Account getById(int id) {
    Session session = DatabaseConnection.getSession();
    Account account = session.get(Account.class, id);
    session.close();
    return account;
  }

  public Account getByEmail(String email) {
    Session session = DatabaseConnection.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Criteria
    CriteriaQuery<Account> criteria = builder.createQuery(Account.class);
    Root<Account> root = criteria.from(Account.class);
    criteria.select(root);

    // Apply the email filter
    criteria.where(builder.equal(root.get("email"), email));

    Account account = null;
    try {
      account = session.createQuery(criteria).getSingleResult();
    } catch (NoResultException e) {
      // No account found, account remains null
    } finally {
      session.close();
    }

    return account;
  }

}
