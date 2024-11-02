package cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.List;
import lombok.EqualsAndHashCode;

@Converter(autoApply = true)
@EqualsAndHashCode
public class CustomAlertConditionListConverter implements AttributeConverter<List<CustomAlertCondition>,String> {

  private static final Gson gson = new Gson();

  @Override
  public String convertToDatabaseColumn(List<CustomAlertCondition> customAlertConditions) {
    return gson.toJson(customAlertConditions);  // Convert the CustomAlertCondition list to a JSON string
  }

  @Override
  public List<CustomAlertCondition> convertToEntityAttribute(String json) {
    Type type = new TypeToken<List<CustomAlertCondition>>() {}.getType();
    return gson.fromJson(json, type); // Convert the JSON string back to a CustomAlertCondition list
  }

}