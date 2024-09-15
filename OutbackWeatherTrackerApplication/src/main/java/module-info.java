module cab302softwaredevelopment.outbackweathertrackerapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jdi;
    requires java.sql;
    requires com.google.gson;
    requires java.net.http;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires org.hibernate.orm.community.dialects;
  requires java.desktop;

  opens cab302softwaredevelopment.outbackweathertrackerapplication.database.dao to org.hibernate.orm.core;

    exports cab302softwaredevelopment.outbackweathertrackerapplication;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.models;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.controllers;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

    exports cab302softwaredevelopment.outbackweathertrackerapplication.services to com.google.gson;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets to javafx.fxml;

    opens cab302softwaredevelopment.outbackweathertrackerapplication to javafx.fxml;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.controllers to com.google.gson, javafx.fxml;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages to com.google.gson, javafx.fxml;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.models to com.google.gson, javafx.fxml;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.services to com.google.gson;
    opens cab302softwaredevelopment.outbackweathertrackerapplication.database.model to org.hibernate.orm.core;

    //database testing
    exports cab302softwaredevelopment.outbackweathertrackerapplication.database;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;
    exports cab302softwaredevelopment.outbackweathertrackerapplication.database.model;
}