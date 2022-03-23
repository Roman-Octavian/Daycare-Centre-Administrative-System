module com.main.daycare_administrative_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.main.daycare_administrative_system to javafx.fxml;
    exports com.main.daycare_administrative_system;
}