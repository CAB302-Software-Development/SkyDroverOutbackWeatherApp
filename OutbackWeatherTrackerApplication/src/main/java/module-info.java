module cab302softwaredevelopment.outbackweathertrackerapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
  requires jdk.jdi;
  requires java.sql;
  requires java.net.http;
  requires com.google.gson;
  requires org.hibernate.orm.core;
  requires jakarta.persistence;
  requires java.naming;

  opens cab302softwaredevelopment.outbackweathertrackerapplication.database.dao to org.hibernate.orm.core;

  opens cab302softwaredevelopment.outbackweathertrackerapplication to javafx.fxml;
    exports cab302softwaredevelopment.outbackweathertrackerapplication;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.controllers;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.controllers to javafx.fxml;
  opens cab302softwaredevelopment.outbackweathertrackerapplication.database.model to org.hibernate.orm.core;

  //database testing
  exports cab302softwaredevelopment.outbackweathertrackerapplication.database;
  exports cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;
  exports cab302softwaredevelopment.outbackweathertrackerapplication.database.model;



}