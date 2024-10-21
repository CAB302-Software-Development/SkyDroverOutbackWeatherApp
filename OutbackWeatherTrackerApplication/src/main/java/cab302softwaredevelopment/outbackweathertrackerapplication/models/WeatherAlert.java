package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeatherAlert {
    private String title;
    private String link;
    private String pubDate;

    public WeatherAlert(String title, String link, String pubDate) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
    }
}
