package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class MapController {
    @FXML
    Panel pnlRoot;

    public void initialize(Pane parent) {
        pnlRoot.prefWidthProperty().bind(parent.widthProperty());
        pnlRoot.prefHeightProperty().bind(parent.heightProperty());
    }
}