package cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.List;
import lombok.EqualsAndHashCode;

/**
 * Converts a list of Locations to a JSON string and vice versa.
 */
@Converter(autoApply = true)
@EqualsAndHashCode
public class LocationListConverter implements AttributeConverter<List<Location>,String> {

  private static final Gson gson = new Gson();

  /**
   * Converts a list of Locations to a JSON string.
   *
   * @param locations The list of Locations to convert.
   * @return The JSON string representation of the Locations list.
   */
  @Override
  public String convertToDatabaseColumn(List<Location> locations) {
    return gson.toJson(locations);  // Convert the Locations list to a JSON string
  }

  /**
   * Converts a JSON string to a list of Locations.
   *
   * @param json The JSON string to convert.
   * @return The Locations list representation of the JSON string.
   */
  @Override
  public List<Location> convertToEntityAttribute(String json) {
    Type type = new TypeToken<List<Location>>() {}.getType();
    return gson.fromJson(json, type); // Convert the JSON string back to a Locations list
  }

}