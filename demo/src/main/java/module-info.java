module cab302softwaredevelopment.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens cab302softwaredevelopment.demo to javafx.fxml;
    exports cab302softwaredevelopment.demo;
}