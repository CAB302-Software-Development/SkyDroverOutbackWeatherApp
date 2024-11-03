package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;

@Getter @Setter
public class AccountUpdateModel {
    private String id = null;
    private String username = null;
    private String email = null;
    private String password = null;
    private Boolean preferCelsius = null;
    private Theme currentTheme = null;
    private String selectedLayout = null;
    private String JWTToken = null;
    private HashMap<String, WidgetInfo[]> dashboardLayouts = null;
    private List<CustomAlertCondition> customAlertConditions = null;
}