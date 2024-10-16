package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import lombok.Getter;

public enum WeatherCondition {
    Cloudy("Cloudy", "images/partlycloudy.png");

    @Getter
    final String name;
    @Getter
    final String imagePath;

    public static WeatherCondition fromReading(DailyForecast forecast) {
        return WeatherCondition.Cloudy;
    }

    public static WeatherCondition fromReading(HourlyForecast forecast) {
        return WeatherCondition.Cloudy;
    }

    WeatherCondition(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
}
