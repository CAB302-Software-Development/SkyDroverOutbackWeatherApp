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
}
