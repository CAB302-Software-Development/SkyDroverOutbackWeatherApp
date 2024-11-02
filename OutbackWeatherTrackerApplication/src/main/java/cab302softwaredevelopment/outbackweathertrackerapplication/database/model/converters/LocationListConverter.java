package cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import lombok.EqualsAndHashCode;

@Converter(autoApply = true)
@EqualsAndHashCode
public class LocationListConverter implements AttributeConverter<List<Location>,String> {

  private static final Gson gson = new Gson();

  @Override
  public String convertToDatabaseColumn(List<Location> locations) {
    return gson.toJson(locations);  // Convert the Locations list to a JSON string
  }

  @Override
  public List<Location> convertToEntityAttribute(String json) {
    Type type = new TypeToken<List<Location>>() {}.getType();
    return gson.fromJson(json, type); // Convert the JSON string back to a Locations list
  }

}