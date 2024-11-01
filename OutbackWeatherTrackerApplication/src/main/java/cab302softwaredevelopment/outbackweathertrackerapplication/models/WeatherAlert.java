package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class WeatherAlert {
    private String title;
    private String message;
    private List<String> data;

    public WeatherAlert(String title, String message, String... data) {
        this.title = title;
        this.message = message;
        this.data = List.of(data);
    }
}
