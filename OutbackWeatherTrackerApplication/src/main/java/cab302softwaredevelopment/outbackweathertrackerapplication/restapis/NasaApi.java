package cab302softwaredevelopment.outbackweathertrackerapplication.restapis;

public class NasaApi {
  String apiKey = System.getenv("NASA_API_KEY");

  public void SetupApi() throws IllegalAccessException {
    System.out.println(apiKey);
  }
}
