package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class ForecastController implements ISwapPanel {
    @FXML
    Panel pnlRoot;

    public void initialize(Pane parent) {
        pnlRoot.prefWidthProperty().bind(parent.widthProperty());
        pnlRoot.prefHeightProperty().bind(parent.heightProperty());
    }
}