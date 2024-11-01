package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FilterValue {
    private String field;
    private Boolean isGreaterOrEqual;
    private String value;

    public FilterValue(String field, Boolean isGreaterOrEqual, String value) {
        this.field = field;
        this.isGreaterOrEqual = isGreaterOrEqual;
        this.value = value;
    }
}
