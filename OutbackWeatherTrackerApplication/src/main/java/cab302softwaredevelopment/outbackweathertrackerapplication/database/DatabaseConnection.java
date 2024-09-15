package cab302softwaredevelopment.outbackweathertrackerapplication.database;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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

  public static Session getSession() {
    if (sessionFactory == null) {
      new DatabaseConnection();
    }
    // Open a new session
    return sessionFactory.openSession();
  }

  public static void close() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }
}