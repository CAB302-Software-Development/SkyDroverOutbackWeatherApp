package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.CrowdsourcedModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CrowdsourcedDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

/**
 * Service class for interacting with the crowdsourced data API.
 * Provides methods for creating, retrieving, filtering, and deleting markers on a map,
 * which are representations of user-submitted data points.
 */
public class CrowdsourcedApiService {
    @Getter
    private static CrowdsourcedApiService instance = new CrowdsourcedApiService();
    private static final String BASE_URL = "http://localhost:8080/api/crowdsourced";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public CrowdsourcedApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Creates a new marker with the specified data.
     *
     * @param data The data to create the marker.
     * @return The created CrowdsourcedModel containing the marker details.
     * @throws Exception if an error occurs during the creation request.
     */
    public CrowdsourcedModel createMarker(CrowdsourcedDTO data) throws Exception {
        String requestBody = objectMapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/createMarker"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), CrowdsourcedModel.class);
        } else {
            throw new Exception("Error creating marker: " + response.body());
        }
    }

    /**
     * Retrieves a marker by the specified username.
     *
     * @param username the user name to search
     * @return Optional of CrowdsourcedDataModel
     * @throws Exception
     */
    public Optional<CrowdsourcedModel> getMarkerByUsername(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/" + username))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(), CrowdsourcedModel.class));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves markers within a specified temperature range.
     *
     * @param minTemp The minimum temperature.
     * @param maxTemp The maximum temperature.
     * @return An Optional containing a list of CrowdsourcedModel objects within the specified range.
     * @throws Exception if an error occurs during the request.
     */
    public Optional<List<CrowdsourcedModel>> getMarkerByTempRange(int minTemp, int maxTemp) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/temperature?minTemp=" + minTemp + "&maxTemp=" + maxTemp))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CrowdsourcedModel.class)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Deletes a marker by its unique identifier.
     *
     * @param id The unique identifier of the marker to delete.
     * @return True if the deletion was successful, false otherwise.
     * @throws Exception if an error occurs during the request.
     */
    public boolean deleteMarkerById(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete-marker/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    /**
     * Retrieves markers within a specified geographic range.
     *
     * @param minLat The minimum latitude.
     * @param maxLat The maximum latitude.
     * @param minLon The minimum longitude.
     * @param maxLon The maximum longitude.
     * @return An Optional containing a list of CrowdsourcedModel objects within the specified geographic range.
     * @throws Exception if an error occurs during the request.
     */
    public Optional<List<CrowdsourcedModel>> getMarkerByGeoRange(double minLat, double maxLat, double minLon, double maxLon) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/marker-georange?minLat=" + minLat + "&maxLat=" + maxLat + "&minLon=" + minLon + "&maxLon=" + maxLon))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Optional.of(objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CrowdsourcedModel.class)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the latest filtered markers.
     *
     * @return A list of CrowdsourcedDTO objects containing the latest crowdsourced data.
     * @throws Exception if an error occurs during the request.
     */
    public List<CrowdsourcedDTO> getLatestFilteredData() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/latest-markers"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CrowdsourcedDTO.class));
        } else {
            throw new Exception("Error fetching latest data: " + response.body());
        }
    }
}