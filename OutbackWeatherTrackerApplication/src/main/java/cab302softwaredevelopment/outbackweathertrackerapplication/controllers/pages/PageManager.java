package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import java.util.ArrayList;
import java.util.List;

public class PageManager {
    private final List<ISwapPanel> observers = new ArrayList<>();

    public void registerPage(ISwapPanel observer) {
        observers.add(observer);
    }

    public void unregisterPage(ISwapPanel observer) {
        observers.remove(observer);
    }

    public void updatePages() {
        for (ISwapPanel observer : observers) {
            observer.updateData();
        }
    }

    public void unregisterAllPages() {
        observers.clear();
    }
}