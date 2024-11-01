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
     * Create a new marker.
     *
     * @param data the data to create a marker
     * @return CrowdsourcedDataModel with created data
     * @throws Exception
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
     * Get marker by user name.
     *
     * @param username the user name to search
     * @return Optional of CrowdsourcedDataModel
     * @throws Exception
     */
    public Optional<CrowdsourcedModel> getMarkerByUserName(String username) throws Exception {
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
     * Get markers by temperature range.
     *
     * @param minTemp minimum temperature
     * @param maxTemp maximum temperature
     * @return Optional list of CrowdsourcedDataModel within range
     * @throws Exception
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
     * Delete marker by ID.
     *
     * @param id the marker ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws Exception
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
     * Get markers by geographic range.
     *
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @return Optional list of CrowdsourcedDataModel within range
     * @throws Exception
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
     * Get the latest filtered markers.
     *
     * @return List of CrowdsourcedDataDTO with latest data
     * @throws Exception
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