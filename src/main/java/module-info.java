module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.example.project to javafx.fxml;
    opens com.example.project.models to javafx.fxml;
    exports com.example.project;
    exports com.example.project.models;
}