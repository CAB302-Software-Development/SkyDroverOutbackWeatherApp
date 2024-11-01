package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationCreateModel {
    String name;
    double latitude;
    double longitude;

    public LocationCreateModel(double latitude, double longitude) {
        this.name = null;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationCreateModel(Location location) {
        this.name = location.getName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public LocationCreateModel(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location build(Account account) {
        Location newLocation = new Location(
                account,
                getLongitude(),
                getLatitude(),
                getName());
        return newLocation;
    }
}
