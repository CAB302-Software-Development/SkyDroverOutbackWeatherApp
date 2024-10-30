package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.services.*;

public abstract class BasePage implements ISwapPanel {
    UserService userService;
    LocationService locationService;
    ForecastService forecastService;
    ConnectionService connectionService;

    @Override
    public void initialize() {
        userService = UserService.getInstance();
        locationService = LocationService.getInstance();
        forecastService = ForecastService.getInstance();
        connectionService = ConnectionService.getInstance();
    }

    @Override
    public void unregister() {
        PageFactory.getPageManager().unregisterPage(this);
    }

    public abstract void updateData();
}
