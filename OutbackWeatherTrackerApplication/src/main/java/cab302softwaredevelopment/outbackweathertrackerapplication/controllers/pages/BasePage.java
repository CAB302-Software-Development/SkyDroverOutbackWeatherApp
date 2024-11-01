package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.services.*;

/**
 * Abstract base class for application pages, providing shared service initialization and unregistration methods.
 * This class serves as a foundation for other pages, granting access to various services, including user management,
 * location handling, forecast retrieval, connection management, and alert processing.
 */
public abstract class BasePage implements ISwapPanel {
    UserService userService;
    LocationService locationService;
    ForecastService forecastService;
    ConnectionService connectionService;
    AlertsService alertsService;

    @Override
    public void initialize() {
        userService = UserService.getInstance();
        locationService = LocationService.getInstance();
        forecastService = ForecastService.getInstance();
        connectionService = ConnectionService.getInstance();
        alertsService = AlertsService.getInstance();
    }

    /**
     * Unregisters the current page from the PageManager, releasing any associated resources.
     */
    @Override
    public void unregister() {
        PageFactory.getPageManager().unregisterPage(this);
    }

    /**
     * Abstract method to update page data, intended to be implemented by subclasses.
     * Implementing classes define specific behaviors for refreshing or loading
     * updated content when this method is called.
     */
    public abstract void updateData();
}
