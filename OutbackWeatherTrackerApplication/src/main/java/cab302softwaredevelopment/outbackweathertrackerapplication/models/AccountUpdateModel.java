package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.UUID;

/**
 * A model class for the Account entity.
 */
@Getter @Setter
public class AccountUpdateModel {
    /**
     * The unique identifier for the account.
     */
    private UUID id = null;

    /**
     * The email address associated with the account.
     */
    private String email = null;

    /**
     * The password for the account.
     */
    private String password = null;

    /**
     * Preference for temperature unit (Celsius or Fahrenheit).
     */
    private Boolean preferCelsius = null;

    /**
     * The current theme of the account.
     */
    private Theme currentTheme = null;

    /**
     * The selected layout for the account.
     */
    private String selectedLayout = null;

    /**
     * The dashboard layouts for the account.
     */
    private HashMap<String, WidgetInfo[]> dashboardLayouts = null;
}