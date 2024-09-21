package cab302softwaredevelopment.outbackweathertrackerapplication.database;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
/**
 * A class for managing the connection to the database.
 */
public class DatabaseConnection {

  private static SessionFactory sessionFactory = null;
  private static final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();

  private DatabaseConnection() {
      sessionFactory =
          new MetadataSources(registry)
              .addAnnotatedClass(Account.class)
              .addAnnotatedClass(Location.class)
              .addAnnotatedClass(HourlyForecast.class)
              .addAnnotatedClass(DailyForecast.class)
              .buildMetadata()
              .buildSessionFactory();

  }

  /**
   * Returns a new session for the database connection.
   * @return A new session for the database connection.
   */
  public static Session getSession() {
    if (sessionFactory == null) {
      new DatabaseConnection();
    }
    // Open a new session
    return sessionFactory.openSession();
  }

  /**
   * Closes the session factory and all open sessions.
   */
  public static void close() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }
}