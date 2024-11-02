package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.Getter;

public class ConnectivityApiService {

  @Getter
  private static ConnectivityApiService instance = new ConnectivityApiService();

  private static final String BASE_URL = "http://localhost:8090/api";
  private final HttpClient client;

  public ConnectivityApiService() {
    this.client = HttpClient.newHttpClient();
  }

  /**
   * Test the connection to the server.
   * @return true if the server is reachable, false otherwise.
   */
  public boolean test() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/test"))
        .GET()
        .build();

    try{
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      ConnectionService.getInstance().setRestAPIOffline(response.statusCode() != 200);
      return response.statusCode() == 200;
    } catch (Exception e) {
      ConnectionService.getInstance().setRestAPIOffline(true);
      return false; // If an exception occurs, the server is unreachable or broken.
    }
  }

}