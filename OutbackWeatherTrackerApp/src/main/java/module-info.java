module cab302softwaredevelopment.oubackweathertrackerapp {
    requires javafx.controls;
    requires org.kordamp.bootstrapfx.core;

    // Open and export your packages
    opens cab302softwaredevelopment.oubackweathertrackerapp to javafx.fxml;
    exports cab302softwaredevelopment.oubackweathertrackerapp;

    exports cab302softwaredevelopment.oubackweathertrackerapp.controllers;
    opens cab302softwaredevelopment.oubackweathertrackerapp.controllers to javafx.fxml;
}
