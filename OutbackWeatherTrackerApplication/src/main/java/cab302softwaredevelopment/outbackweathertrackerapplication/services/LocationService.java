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

/**
 * Service class responsible for managing user locations, including retrieval, addition, deletion,
 * and geocoding operations. Provides methods for querying location data based on user accounts
 * and for interacting with external geolocation APIs to convert between coordinates and addresses.
 */
public class LocationService {
    @Getter
    private static LocationService instance = new LocationService();

    private LocationDAO locationDAO = new LocationDAO();

    /**
     * Deletes a location from the database based on its ID.
     *
     * @param id The ID of the location to delete.
     */
    public void deleteLocation(long id) {
        locationDAO.delete(id);
    }

    /**
     * Deletes a specified location from the database.
     *
     * @param location The Location object to delete.
     */
    public void deleteLocation(Location location) {
        locationDAO.delete(location);
    }

    /**
     * Adds a new location for the currently signed-in user.
     *
     * @param location The location data to add for the current user.
     */
    public void addLocationForCurrentUser(LocationCreateModel location) {
        addLocationForUser(UserService.getInstance().getCurrentAccount(), location);
    }

    /**
     * Adds a new location for a specified user account.
     *
     * @param account  The user account for which to add the location.
     * @param location The location data to add.
     */
    public void addLocationForUser(Account account, LocationCreateModel location) {
        Location newLocation = location.build(account);
        locationDAO.insert(newLocation);
        ForecastService.getInstance().updateForecastsForLocation(newLocation,7,2);
    }

    /**
     * Retrieves all locations associated with the current user.
     *
     * @return A list of Location objects for the current user.
     */
    public List<Location> getCurrentUserLocations() {
        Account currentAccount = UserService.getInstance().getCurrentAccount();
        if (currentAccount == null) {
            // No user logged in, return empty list
            return List.of();
        }
        return getLocationsForUser(currentAccount);
    }

    /**
     * Retrieves all locations associated with a specific user account.
     *
     * @param account The account for which to retrieve locations.
     * @return A list of Location objects for the specified account.
     */
    public List<Location> getLocationsForUser(Account account) {
        return (new LocationDAO.LocationQuery())
                .whereAccount(account)
                .getResults();
    }

    /**
     * Retrieves a human-readable address for the specified coordinates (latitude and longitude)
     * by making a reverse geocoding API call to the OpenStreetMap Nominatim service.
     *
     * @param location The LocationCreateModel containing the latitude and longitude.
     * @return The address as a String, or throws an exception if the request fails.
     * @throws Exception if the HTTP request to retrieve the address fails.
     */
    public String getAddressFromCoordinates(LocationCreateModel location) throws Exception {
        return getAddressFromCoordinates(location.getLatitude(), location.getLongitude());
    }

    /**
     * Retrieves a human-readable address for the specified coordinates (latitude and longitude)
     * by making a reverse geocoding API call to the OpenStreetMap Nominatim service.
     *
     * @param location The Location object containing the latitude and longitude.
     * @return The address as a String, or throws an exception if the request fails.
     * @throws Exception if the HTTP request to retrieve the address fails.
     */
    public String getAddressFromCoordinates(Location location) throws Exception {
        return getAddressFromCoordinates(location.getLatitude(), location.getLongitude());
    }

    /**
     * Retrieves a human-readable address for the specified coordinates (latitude and longitude)
     * by making a reverse geocoding API call to the OpenStreetMap Nominatim service.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return The address as a String.
     * @throws Exception if the HTTP request to retrieve the address fails.
     */
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

    /**
     * Retrieves the state information based on latitude and longitude.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return The state as a String.
     * @throws Exception if the HTTP request to retrieve the state fails.
     */
    public String getStateFromCoordinates(double lat, double lon) throws Exception {
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

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonObject addressObject = jsonObject.getAsJsonObject("address");

            if (addressObject != null && addressObject.has("state")) {
                String state = addressObject.get("state").getAsString();
                return state;
            } else {
                throw new Exception("State information not found in response");
            }
        } else {
            throw new Exception("HTTP Request failed with code: " + responseCode);
        }
    }

    /**
     * Retrieves coordinates for a given address by geocoding the address.
     *
     * @param address The address to geocode.
     * @return A LocationCreateModel containing the latitude and longitude of the address, or null if the request fails.
     */
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

                return new LocationCreateModel(null, latitude, longitude);
            } else {
                throw new Exception("HTTP Request failed with code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes all locations associated with the current user.
     *
     * @return True if deletion was successful; false otherwise.
     */
    public boolean deleteAllUserLocations() {
        return deleteLocationsForUser(UserService.getInstance().getCurrentAccount());
    }

    /**
     * Retrieves a location by its unique ID.
     *
     * @param location The ID of the location to retrieve.
     * @return The Location object corresponding to the given ID.
     */
    public Location getById(long location) {
        Location locationObj = new LocationDAO.LocationQuery().whereId(location).getSingleResult();
        return locationObj;
    }

    /**
     * Deletes all locations associated with a specific user account.
     *
     * @param account The account for which to delete locations.
     * @return True if all locations were deleted successfully; false otherwise.
     */
    public boolean deleteLocationsForUser(Account account) {
        try {
            List<Location> locations = (new LocationDAO.LocationQuery()).whereAccount(account).getResults();
            for (Location location : locations) {
                locationDAO.delete(location);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a shortened name for a given location, limited to a specified length.
     *
     * @param location The location whose name is to be shortened.
     * @param maxLength The maximum allowed length for the shortened name.
     * @return A shortened location name, truncated to maxLength if necessary.
     */
    public static String getShortName(Location location, int maxLength) {
        return  location.getName().length() > maxLength ?
                location.getName().substring(0, maxLength) + "..." :
                location.getName();
    }

    /**
     * Returns a shortened name for a given location with a default length limit.
     *
     * @param location The location whose name is to be shortened.
     * @return A shortened location name, truncated to the default length of 30 characters if necessary.
     */
    public static String getShortName(Location location) {
        return getShortName(location, 100);
    }
}
