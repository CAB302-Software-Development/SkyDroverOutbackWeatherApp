package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private static final String RESET = "\033[0m"; // Reset to default
  private static final String RED = "\033[0;31m";
  private static final String GREEN = "\033[0;32m";
  private static final String YELLOW = "\033[0;33m";
  private static final String BLUE = "\033[0;34m";
  private static final String MAGENTA = "\033[0;35m";
  private static final String CYAN = "\033[0;36m";
  private static final String WHITE = "\033[0;37m";

  public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final String LOG_INFO_LEVEL = "[INFO] ";

  @SafeVarargs
  public static <T> void printLog(T... args) {
    String formattedDateTime = LocalDateTime.now().format(formatter);
    for(T arg : args) {
      System.out.print(GREEN + "[" + formattedDateTime + "] " +
          RESET +
          MAGENTA +
          LOG_INFO_LEVEL +
          RESET +
          "- " +
          arg +
          '\n'
      );
    }
  }
}
