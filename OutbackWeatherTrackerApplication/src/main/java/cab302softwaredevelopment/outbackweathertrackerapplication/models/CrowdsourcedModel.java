package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CrowdsourcedModel {
    private String id;

    private String location;

    private double latitude;

    private double longitude;

    private int actualTemp;

    private int feelsLikeTemp;

    private String username;

    private Date createdDate = new Date();
}
