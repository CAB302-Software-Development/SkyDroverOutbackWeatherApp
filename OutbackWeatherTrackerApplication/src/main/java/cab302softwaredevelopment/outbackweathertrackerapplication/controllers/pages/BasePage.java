package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

public abstract class BasePage implements ISwapPanel {
    @Override
    public void unregister() {
        PageFactory.getPageManager().unregisterPage(this);
    }

    public abstract void updateData();
}
