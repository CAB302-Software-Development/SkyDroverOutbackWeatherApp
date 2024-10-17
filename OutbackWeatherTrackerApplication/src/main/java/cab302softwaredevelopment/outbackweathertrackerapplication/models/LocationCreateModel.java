package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationCreateModel {
    String name;
    double latitude;
    double longitude;
    double elevation;

    public LocationCreateModel(String name, double latitude, double longitude, double elevation) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }
}
