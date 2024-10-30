package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AlertsWidgetController extends BaseWidgetController {
    @FXML
    public ImageView imgAlert1, imgAlert2;
    @FXML
    public Text txtAlertTitle, txtAlertField1, txtAlertField2, txtAlertField3, txtAlertValue1, txtAlertValue2, txtAlertValue3;
    @FXML
    public Button btnNotification;
    @FXML
    public VBox vbClose;


    @Override
    public void updateWidget() {

    }
}
