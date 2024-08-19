package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLogger {
  String LOG_TIME_MESSAGE = "";

  @SafeVarargs
  public static <T> void logIt(T... args) throws RuntimeException{
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String formattedDateTime = currentDateTime.format(formatter);

    for(T arg : args) {
      System.out.print(formattedDateTime + " " + arg + "\n");
    }
  }
}
