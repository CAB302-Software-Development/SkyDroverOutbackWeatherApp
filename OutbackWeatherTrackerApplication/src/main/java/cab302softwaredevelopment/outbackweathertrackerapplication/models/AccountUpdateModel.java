package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.UUID;

@Getter @Setter
public class AccountUpdateModel {
    private UUID id = null;
    private String email = null;
    private String password = null;
    private Boolean preferCelsius = null;
    private Theme currentTheme = null;
    private String selectedLayout = null;
    private HashMap<String, WidgetInfo[]> dashboardLayouts = null;
}