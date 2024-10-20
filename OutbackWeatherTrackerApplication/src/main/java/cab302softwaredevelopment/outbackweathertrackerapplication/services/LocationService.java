package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class LocationService {
    @Getter
    private static LocationService instance = new LocationService();

    private LocationDAO locationDAO = new LocationDAO();

    public void deleteLocation(long id) {
        locationDAO.delete(id);
    }
    public void deleteLocation(Location location) {
        locationDAO.delete(location);
    }

    public void addLocationForCurrentUser(LocationCreateModel location) {
        addLocationForUser(UserService.getInstance().getCurrentAccount(), location);
    }
    public void addLocationForUser(Account account, LocationCreateModel location) {
        Location newLocation = new Location(
                account,
                location.getLongitude(),
                location.getLatitude(),
                location.getElevation(),
                location.getName());
        locationDAO.insert(newLocation);
    }

    public List<Location> getCurrentUserLocations() {
        return (new LocationDAO.LocationQuery())
                .whereAccount(UserService.getInstance().getCurrentAccount())
                .getResults();
    }

    public String getAddressFromCoordinates(LocationCreateModel location) throws Exception {
        return getAddressFromCoordinates(location.getLatitude(), location.getLongitude());
    }
    public String getAddressFromCoordinates(Location location) throws Exception {
        return getAddressFromCoordinates(location.getLatitude(), location.getLongitude());
    }
    public String getAddressFromCoordinates(double lat, double lon) throws Exception {
        String urlStr = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&addressdetails=1", lat, lon);

        URL url = new URI(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Java Application");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Use Gson to parse the JSON response
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            String address = jsonObject.get("display_name").getAsString();

            return address;
        } else {
            throw new Exception("HTTP Request failed with code: " + responseCode);
        }
    }

    public LocationCreateModel geocodeAddress(String address) {
        try {
            String urlStr = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1",
                    address.replace(" ", "+"));

            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Java Application");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonArray().get(0).getAsJsonObject();
                double latitude = jsonObject.get("lat").getAsDouble();
                double longitude = jsonObject.get("lon").getAsDouble();

                return new LocationCreateModel(null, latitude, longitude, 0);
            } else {
                throw new Exception("HTTP Request failed with code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAllUserLocations() {
        try {
            LocationDAO dao = new LocationDAO();
            for (Location location : getCurrentUserLocations()) {
                dao.delete(location);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
