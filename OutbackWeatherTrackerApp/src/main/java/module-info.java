module cab302softwaredevelopment.oubackweathertrackerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    requires org.testfx.junit5;
    requires javafx.graphics;

    // Open and export your packages
    opens cab302softwaredevelopment.oubackweathertrackerapp to javafx.fxml;
    exports cab302softwaredevelopment.oubackweathertrackerapp;

    exports cab302softwaredevelopment.oubackweathertrackerapp.controllers;
    opens cab302softwaredevelopment.oubackweathertrackerapp.controllers to javafx.fxml;
}
