package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;

/**
 * A Data Access Object for the Account entity.
 */
public class AccountDAO {

  public AccountDAO() {
  }

  /**
   * Inserts a new Account into the database.
   *
   * @param account the Account object to be inserted
   *                <p>
   *                This method begins a transaction and attempts to save the provided Account
   *                object to the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
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

  /**
   * Updates an existing Account in the database.
   *
   * @param account the Account object to be updated
   *                <p>
   *                This method begins a transaction and attempts to update the provided Account
   *                object in the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
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

  /**
   * Deletes an Account from the database by its ID.
   *
   * @param id The ID of the Account to be deleted
   *           <p>
   *           This method begins a transaction and attempts to delete the Account object with the
   *           specified ID from the database. If the operation is successful, the transaction is
   *           committed. If an exception occurs during the operation, the transaction is rolled
   *           back and the exception stack trace is printed.
   */
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

  /**
   * Deletes an Account from the database
   *
   * @param account The Account to be deleted
   *                <p>
   *                This method begins a transaction and attempts to delete the provided Account
   *                object from the database. If the operation is successful, the transaction is
   *                committed. If an exception occurs during the operation, the transaction is
   *                rolled back and the exception stack trace is printed.
   */
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

  /**
   * Retrieves all Account objects from the database.
   *
   * @return A list of all Account objects in the database
   */
  @Deprecated
  public List<Account> getAll() {
    return new AccountQuery()
        .getResults();
  }

  /**
   * Retrieves an Account object from the database by its ID.
   *
   * @param id The ID of the Account to retrieve
   * @return The Account object with the specified ID or null if no Account is found
   */
  @Deprecated
  public Account getById(int id) {
    return new AccountQuery()
        .whereId(id)
        .getSingleResult();
  }

  /**
   * Retrieves an Account object from the database by its email.
   *
   * @param email The email of the Account to retrieve
   * @return The Account object with the specified email or null if no Account is found
   */
  @Deprecated
  public Account getByEmail(String email) {
    return new AccountQuery()
        .whereEmail(email)
        .getSingleResult();
  }

  /**
   * A query builder class for the Account entity.
   */
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

    /**
     * Adds an id filter to the query.
     *
     * @param id The ID to filter by
     * @return This AccountQuery object
     */
    public AccountQuery whereId(int id) {
      criteria.where(builder.equal(root.get("id"), id));
      return this;
    }

    /**
     * Adds an email filter to the query.
     *
     * @param email The email to filter by
     * @return This AccountQuery object
     */
    public AccountQuery whereEmail(String email) {
      criteria.where(builder.equal(root.get("email"), email));
      return this;
    }

    /**
     * Adds an email contains filter to the query.
     *
     * @param email The string to filter by
     * @return This AccountQuery object
     */
    public AccountQuery whereEmailLike(String email) {
      criteria.where(builder.like(root.get("email"), "%" + email + "%"));
      return this;
    }

    /**
     * Adds a Celsius preference filter to the query.
     *
     * @param preferCelsius The Celsius preference to filter for
     * @return This AccountQuery object
     */
    public AccountQuery wherePreferCelsius(boolean preferCelsius) {
      criteria.where(builder.equal(root.get("preferCelsius"), preferCelsius));
      return this;
    }

    /**
     * Executes the current filters and returns the results.
     *
     * @return A list of Account objects that match the provided filters
     */
    public List<Account> getResults() {
      Session session = DatabaseConnection.getSession();
      List<Account> accounts = session.createQuery(criteria).getResultList();
      session.close();
      return accounts;
    }

    /**
     * Executes the current filters and returns the first result.
     *
     * @return The first Account object that matches the provided filters or null if no results are found
     */
    public Account getSingleResult() {
      Session session = DatabaseConnection.getSession();
      Account account = session.createQuery(criteria).getResultStream().findFirst().orElse(null);
      session.close();
      return account;
    }
  }

}
