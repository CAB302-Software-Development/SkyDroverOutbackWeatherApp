package cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.HashMap;
import lombok.EqualsAndHashCode;

/**
 * Converts a HashMap of WidgetInfo arrays to a JSON string and vice versa.
 */
@Converter(autoApply = true)
@EqualsAndHashCode
public class WidgetInfoListConverter implements AttributeConverter<HashMap<String, WidgetInfo[]>, String> {

  private static final Gson gson = new Gson();

  /**
   * Converts a HashMap of WidgetInfo arrays to a JSON string.
   *
   * @param layouts The HashMap of WidgetInfo arrays to convert.
   * @return The JSON string representation of the HashMap of WidgetInfo arrays.
   */
  @Override
  public String convertToDatabaseColumn(HashMap<String, WidgetInfo[]> layouts) {
    return gson.toJson(layouts);  // Convert the Layouts object to a JSON string
  }

  /**
   * Converts a JSON string to a HashMap of WidgetInfo arrays.
   *
   * @param json The JSON string to convert.
   * @return The HashMap of WidgetInfo arrays represented by the JSON string.
   */
  @Override
  public HashMap<String, WidgetInfo[]> convertToEntityAttribute(String json) {
    Type type = new TypeToken<HashMap<String, WidgetInfo[]>>() {}.getType();
    return gson.fromJson(json, type); // Convert the JSON string back to a Layouts object
  }
}