package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrowdsourcedDTO {
    private String location;

    private double latitude;

    private double longitude;

    private String userName;

    private int actualTemp;

    private int feelsLikeTemp;
}
