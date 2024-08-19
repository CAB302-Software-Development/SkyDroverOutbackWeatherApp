module cab302softwaredevelopment.outbackweathertrackerapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
  requires jdk.jdi;
  requires java.sql;

  opens cab302softwaredevelopment.outbackweathertrackerapplication to javafx.fxml;
    exports cab302softwaredevelopment.outbackweathertrackerapplication;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.controllers;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.controllers to javafx.fxml;
}