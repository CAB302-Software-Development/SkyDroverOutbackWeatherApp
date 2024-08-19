package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

public class CustomLogger {
  public void logIt(String... args) {
    System.out.println(String.join(" ", args));
  }
}
