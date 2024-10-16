package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;

import java.util.List;

public class LocationService {
    private static LocationDAO locationDAO = new LocationDAO();

    public static void deleteLocation(long id) {
        locationDAO.delete(id);
    }
    public static void deleteLocation(Location location) {
        locationDAO.delete(location);
    }

    public static void addLocationForCurrentUser(LocationCreateModel location) {
        addLocationForUser(LoginState.getCurrentAccount(), location);
    }
    public static void addLocationForUser(Account account, LocationCreateModel location) {
        Location newLocation = new Location(
                account,
                location.getLongitude(),
                location.getLatitude(),
                location.getElevation(),
                location.getName());
        locationDAO.insert(newLocation);
    }

    public static List<Location> getCurrentUserLocations() {
        return (new LocationDAO.LocationQuery())
                .whereAccount(LoginState.getCurrentAccount())
                .getResults();
    }
}
